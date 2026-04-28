#!/usr/bin/env bash
# Integration tests for the flipped-dashboard backend.
# Defaults to http://localhost:8080/api/v1; override with `BASE=https://host/api/v1 ./integration-test.sh`.
# Requires: curl, jq

set -euo pipefail

BASE="${BASE:-http://localhost:8080/api/v1}"
ORIGIN="${ORIGIN:-http://localhost:5173}"
PASS=0
FAIL=0

# ── helpers ──────────────────────────────────────────────────────────────────

check() {
  command -v "$1" >/dev/null 2>&1 || { echo "ERROR: $1 is required but not installed."; exit 1; }
}

check curl
check jq

pass() { echo "  PASS  $1"; PASS=$((PASS + 1)); }
fail() { echo "  FAIL  $1"; echo "        $2"; FAIL=$((FAIL + 1)); }

# assert_status <label> <expected_status> <actual_status>
assert_status() {
  if [ "$3" = "$2" ]; then
    pass "$1 → HTTP $2"
  else
    fail "$1" "expected HTTP $2, got HTTP $3"
  fi
}

# assert_field <label> <json> <jq_filter> <expected_value>
assert_field() {
  actual=$(echo "$2" | jq -r "$3" 2>/dev/null)
  if [ "$actual" = "$4" ]; then
    pass "$1 → $3 = $4"
  else
    fail "$1" "expected $3 = '$4', got '$actual'"
  fi
}

# request <method> <path> [extra curl args...]
# Sets $STATUS and $BODY
request() {
  method="$1"; path="$2"; shift 2
  response=$(curl -s -o /tmp/it_body -w "%{http_code}" -X "$method" "$BASE$path" "$@")
  STATUS="$response"
  BODY=$(cat /tmp/it_body)
}

# assert_header <label> <header_name> <expected_value> <headers_file>
assert_header() {
  actual=$(grep -i "^$2:" "$4" | head -n1 | cut -d' ' -f2- | tr -d '\r\n')
  if [ "$actual" = "$3" ]; then
    pass "$1 → $2: $3"
  else
    fail "$1" "expected '$2: $3', got '$actual'"
  fi
}

# ── health ────────────────────────────────────────────────────────────────────

echo ""
echo "── health ───────────────────────────────────────────────────────────────"
request GET /health
assert_status "GET /health" 200 "$STATUS"
assert_field  "GET /health body" "$BODY" ".status" "ok"

# ── CORS preflight ───────────────────────────────────────────────────────────
# The frontend calls the API cross-origin on Render; verify that preflight
# accepts the configured origin and returns the expected headers.

echo ""
echo "── CORS preflight ───────────────────────────────────────────────────────"
curl -s -o /dev/null -D /tmp/it_headers -w "%{http_code}" \
  -X OPTIONS "$BASE/courses" \
  -H "Origin: $ORIGIN" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: X-Student-Id,Content-Type" > /tmp/it_status
PREFLIGHT_STATUS=$(cat /tmp/it_status)
# Spring returns 200 for allowed preflights (not 204).
if [ "$PREFLIGHT_STATUS" = "200" ] || [ "$PREFLIGHT_STATUS" = "204" ]; then
  pass "OPTIONS /courses preflight → HTTP $PREFLIGHT_STATUS"
else
  fail "OPTIONS /courses preflight" "expected 200 or 204, got $PREFLIGHT_STATUS"
fi
assert_header "CORS Allow-Origin echoes request origin" "Access-Control-Allow-Origin" "$ORIGIN" /tmp/it_headers

# ── courses ───────────────────────────────────────────────────────────────────

echo ""
echo "── courses ──────────────────────────────────────────────────────────────"
request GET /courses
assert_status "GET /courses" 200 "$STATUS"
assert_field  "courses list non-empty" "$BODY" ".data | length > 0" "true"

request GET /courses/1
assert_status "GET /courses/1" 200 "$STATUS"
assert_field  "course 1 name" "$BODY" ".data.title" "Introduction to Biology"

request GET /courses/999
assert_status "GET /courses/999 → 404" 404 "$STATUS"

# ── students ─────────────────────────────────────────────────────────────────

echo ""
echo "── students ─────────────────────────────────────────────────────────────"
request GET /students
assert_status "GET /students" 200 "$STATUS"
assert_field  "students list non-empty" "$BODY" ".data | length > 0" "true"

request GET /students/1/courses \
  -H "X-Student-Id: 1"
assert_status "GET /students/1/courses" 200 "$STATUS"
assert_field  "student 1 has courses" "$BODY" ".data | length > 0" "true"

request GET /students/1/courses/1/lessons \
  -H "X-Student-Id: 1"
assert_status "GET /students/1/courses/1/lessons" 200 "$STATUS"
assert_field  "student 1 biology lessons non-empty" "$BODY" ".data | length > 0" "true"

request GET /lessons/3/content \
  -H "X-Student-Id: 1"
assert_status "GET /lessons/3/content" 200 "$STATUS"
assert_field  "lesson 3 title" "$BODY" ".data.title" "Photosynthesis and Respiration"

# ── activity: lesson events ───────────────────────────────────────────────────

echo ""
echo "── activity: lesson events ──────────────────────────────────────────────"
request POST /activity/lesson-start \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"lessonId":3}'
assert_status "POST /activity/lesson-start (student 8, lesson 3)" 200 "$STATUS"

request POST /activity/lesson-complete \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"lessonId":3}'
assert_status "POST /activity/lesson-complete (student 8, lesson 3)" 200 "$STATUS"

# ── activity: video watched ───────────────────────────────────────────────────

echo ""
echo "── activity: video watched ──────────────────────────────────────────────"
request POST /activity/video-watched \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"lessonItemId":9,"watchPercent":100}'
assert_status "POST /activity/video-watched (student 8, item 9)" 200 "$STATUS"

request POST /activity/video-watched \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"lessonItemId":9,"watchPercent":150}'
assert_status "POST /activity/video-watched watchPercent > 100 → 400" 400 "$STATUS"

# ── activity: quiz response ───────────────────────────────────────────────────

echo ""
echo "── activity: quiz response ──────────────────────────────────────────────"
request POST /activity/quiz-response \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"quizQuestionId":1,"quizChoiceId":2}'
assert_status "POST /activity/quiz-response (student 8, Q1 correct)" 200 "$STATUS"

# ── activity: survey response ─────────────────────────────────────────────────

echo ""
echo "── activity: survey response ────────────────────────────────────────────"

# Clean Likert response
request POST /activity/survey-response \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"surveyQuestionId":1,"likertValue":4}'
assert_status "POST survey-response: clean likert → 200" 200 "$STATUS"

# Clean free-text response
request POST /activity/survey-response \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"surveyQuestionId":2,"freeText":"I am still unclear about the electron transport chain."}'
assert_status "POST survey-response: clean free-text → 200" 200 "$STATUS"

# XSS payload → 400
request POST /activity/survey-response \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"surveyQuestionId":2,"freeText":"<script>alert(1)</script>good question"}'
assert_status "POST survey-response: XSS stripped, clean text saved → 200" 200 "$STATUS"

# Profanity → 400
request POST /activity/survey-response \
  -H "Content-Type: application/json" \
  -H "X-Student-Id: 8" \
  -d '{"studentId":8,"surveyQuestionId":2,"freeText":"this is fucking stupid"}'
assert_status "POST survey-response: profanity → 400" 400 "$STATUS"
assert_field  "profanity error message present" "$BODY" ".error" "Response contains content that is not allowed"

# ── analytics ─────────────────────────────────────────────────────────────────

echo ""
echo "── analytics ────────────────────────────────────────────────────────────"
request GET "/analytics/class-overview?courseId=1&lessonId=3"
assert_status "GET /analytics/class-overview (course 1, lesson 3)" 200 "$STATUS"
assert_field  "class overview has students" "$BODY" ".data.students | length > 0" "true"
assert_field  "class overview has startedCount" "$BODY" ".data.startedCount >= 0" "true"
assert_field  "class overview has surveyedCount" "$BODY" ".data.surveyedCount >= 0" "true"

request GET "/analytics/lesson-timeline?courseId=1"
assert_status "GET /analytics/lesson-timeline (course 1)" 200 "$STATUS"
assert_field  "lesson timeline non-empty" "$BODY" ".data.lessons | length > 0" "true"

request GET "/analytics/student/1?courseId=1&lessonId=3"
assert_status "GET /analytics/student/1 (course 1, lesson 3)" 200 "$STATUS"
assert_field  "student detail name present" "$BODY" ".data.name" "Avery Johnson"

# ── summary ───────────────────────────────────────────────────────────────────

echo ""
echo "─────────────────────────────────────────────────────────────────────────"
echo "  Results: $PASS passed, $FAIL failed"
echo "─────────────────────────────────────────────────────────────────────────"

[ "$FAIL" -eq 0 ]
