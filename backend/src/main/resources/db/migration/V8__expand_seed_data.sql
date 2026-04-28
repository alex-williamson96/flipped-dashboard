-- ============================================================
-- V8: Expand seed data for IM2 demo
-- Adds 17 students with diverse engagement patterns for the
-- active lessons (Biology L3, EnvSci L7), plus historical
-- completion data for Biology L1/L2 and EnvSci L6.
-- ============================================================

-- -----------------------------------------------
-- New students
-- -----------------------------------------------
INSERT INTO students (id, name, section_id) VALUES
(11, 'Kai Nakamura',  1),
(12, 'Lily Chen',     1),
(13, 'Marcus Wells',  1),
(14, 'Nadia Torres',  2),
(15, 'Oscar Reyes',   2),
(16, 'Priya Singh',   2),
(17, 'Quinn Adams',   3),
(18, 'Riley Foster',  3),
(19, 'Sam Davis',     3),
(20, 'Taylor Brooks', 4),
(21, 'Uma Patel',     4),
(22, 'Victor Huang',  4),
(23, 'Wendy Clark',   4),
(24, 'Xavier Moore',  5),
(25, 'Yara Jackson',  5),
(26, 'Zoe White',     5),
(27, 'Aaron Green',   5);

-- -----------------------------------------------
-- Active lesson: Biology Lesson 3
-- Patterns: strong (11,12), low-confidence (13),
-- overconfident (14), average (15), drop-off (16),
-- high-watch/zero-score (17), early-drop (18), no-show (19)
-- -----------------------------------------------

INSERT INTO lesson_events (student_id, lesson_id, event_type, occurred_at) VALUES
(11, 3, 'STARTED',   '2026-04-08 19:00:00+00'),
(11, 3, 'COMPLETED', '2026-04-08 19:45:00+00'),
(12, 3, 'STARTED',   '2026-04-08 20:00:00+00'),
(12, 3, 'COMPLETED', '2026-04-08 20:50:00+00'),
(13, 3, 'STARTED',   '2026-04-08 18:30:00+00'),
(13, 3, 'COMPLETED', '2026-04-08 19:20:00+00'),
(14, 3, 'STARTED',   '2026-04-08 21:00:00+00'),
(14, 3, 'COMPLETED', '2026-04-08 21:55:00+00'),
(15, 3, 'STARTED',   '2026-04-08 22:00:00+00'),
(15, 3, 'COMPLETED', '2026-04-08 22:45:00+00'),
(16, 3, 'STARTED',   '2026-04-08 17:30:00+00'),
(17, 3, 'STARTED',   '2026-04-08 23:00:00+00'),
(17, 3, 'COMPLETED', '2026-04-08 23:50:00+00'),
(18, 3, 'STARTED',   '2026-04-09 08:00:00+00');

-- video_watch_events (lesson_item_id=9)
INSERT INTO video_watch_events (student_id, lesson_item_id, watch_percent, marked_at) VALUES
(11, 9,  98, '2026-04-08 19:35:00+00'),
(12, 9,  92, '2026-04-08 20:40:00+00'),
(13, 9,  75, '2026-04-08 19:10:00+00'),
(14, 9,  90, '2026-04-08 21:45:00+00'),
(15, 9,  78, '2026-04-08 22:35:00+00'),
(16, 9,  55, '2026-04-08 17:50:00+00'),
(17, 9, 100, '2026-04-08 23:40:00+00'),
(18, 9,  30, '2026-04-09 08:10:00+00');

-- quiz_responses (Q1 correct=2, Q2 correct=7, Q3 correct=9)
-- Student 11 (Kai): 3/3
-- Student 12 (Lily): 3/3
-- Student 13 (Marcus): 0/3 — low confidence, still confused
-- Student 14 (Nadia): 1/3 — overconfident (Likert=5, mostly wrong)
-- Student 15 (Oscar): 2/3 — missed Glycolysis
-- Student 17 (Quinn): 0/3 — watched 100% of video, still failed
INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at) VALUES
(11, 1, 2,  '2026-04-08 19:43:00+00'),
(11, 2, 7,  '2026-04-08 19:43:00+00'),
(11, 3, 9,  '2026-04-08 19:44:00+00'),
(12, 1, 2,  '2026-04-08 20:48:00+00'),
(12, 2, 7,  '2026-04-08 20:48:00+00'),
(12, 3, 9,  '2026-04-08 20:49:00+00'),
(13, 1, 1,  '2026-04-08 19:18:00+00'),
(13, 2, 6,  '2026-04-08 19:18:00+00'),
(13, 3, 10, '2026-04-08 19:19:00+00'),
(14, 1, 2,  '2026-04-08 21:53:00+00'),
(14, 2, 5,  '2026-04-08 21:53:00+00'),
(14, 3, 10, '2026-04-08 21:54:00+00'),
(15, 1, 2,  '2026-04-08 22:43:00+00'),
(15, 2, 7,  '2026-04-08 22:43:00+00'),
(15, 3, 11, '2026-04-08 22:44:00+00'),
(17, 1, 3,  '2026-04-08 23:48:00+00'),
(17, 2, 6,  '2026-04-08 23:48:00+00'),
(17, 3, 12, '2026-04-08 23:49:00+00');

-- survey_responses (Likert=sq 1, FREE_TEXT=sq 2)
INSERT INTO survey_responses (student_id, survey_question_id, likert_value, free_text, submitted_at) VALUES
(11, 1, 5, NULL, '2026-04-08 19:44:00+00'),
(12, 1, 4, NULL, '2026-04-08 20:49:00+00'),
(13, 1, 1, NULL, '2026-04-08 19:19:00+00'),
(14, 1, 5, NULL, '2026-04-08 21:54:00+00'),
(15, 1, 3, NULL, '2026-04-08 22:44:00+00'),
(17, 1, 1, NULL, '2026-04-08 23:49:00+00'),
(13, 2, NULL, 'I didn''t understand any of the energy terms. It was overwhelming.',    '2026-04-08 19:19:00+00'),
(17, 2, NULL, 'I watched the whole video but I still don''t understand ATP at all.',  '2026-04-08 23:49:00+00');

-- -----------------------------------------------
-- Active lesson: EnvSci Lesson 7
-- Patterns: strong (20,21), low-confidence (22),
-- Q5-weakness (23), overconfident (24), average (25),
-- drop-off (26), no-show (27)
-- -----------------------------------------------

INSERT INTO lesson_events (student_id, lesson_id, event_type, occurred_at) VALUES
(20, 7, 'STARTED',   '2026-04-08 19:30:00+00'),
(20, 7, 'COMPLETED', '2026-04-08 20:15:00+00'),
(21, 7, 'STARTED',   '2026-04-08 20:30:00+00'),
(21, 7, 'COMPLETED', '2026-04-08 21:20:00+00'),
(22, 7, 'STARTED',   '2026-04-08 18:00:00+00'),
(22, 7, 'COMPLETED', '2026-04-08 18:50:00+00'),
(23, 7, 'STARTED',   '2026-04-08 21:30:00+00'),
(23, 7, 'COMPLETED', '2026-04-08 22:20:00+00'),
(24, 7, 'STARTED',   '2026-04-08 17:00:00+00'),
(24, 7, 'COMPLETED', '2026-04-08 17:55:00+00'),
(25, 7, 'STARTED',   '2026-04-08 22:30:00+00'),
(25, 7, 'COMPLETED', '2026-04-08 23:20:00+00'),
(26, 7, 'STARTED',   '2026-04-09 07:00:00+00');

-- video_watch_events (lesson_item_id=25)
INSERT INTO video_watch_events (student_id, lesson_item_id, watch_percent, marked_at) VALUES
(20, 25, 100, '2026-04-08 20:05:00+00'),
(21, 25,  95, '2026-04-08 21:10:00+00'),
(22, 25,  70, '2026-04-08 18:40:00+00'),
(23, 25,  85, '2026-04-08 22:10:00+00'),
(24, 25,  88, '2026-04-08 17:45:00+00'),
(25, 25,  80, '2026-04-08 23:10:00+00'),
(26, 25,  25, '2026-04-09 07:10:00+00');

-- quiz_responses (Q4 correct=14, Q5 correct=19)
-- Student 20 (Taylor): 2/2
-- Student 21 (Uma): 2/2
-- Student 22 (Victor): 0/2 — low confidence
-- Student 23 (Wendy): 1/2 — correct on wind, wrong on greenhouse gas
-- Student 24 (Xavier): 0/2 — overconfident (Likert=4, both wrong)
-- Student 25 (Yara): 1/2 — correct on wind, wrong on greenhouse gas
INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at) VALUES
(20, 4, 14, '2026-04-08 20:13:00+00'),
(20, 5, 19, '2026-04-08 20:13:00+00'),
(21, 4, 14, '2026-04-08 21:18:00+00'),
(21, 5, 19, '2026-04-08 21:18:00+00'),
(22, 4, 13, '2026-04-08 18:48:00+00'),
(22, 5, 17, '2026-04-08 18:48:00+00'),
(23, 4, 14, '2026-04-08 22:18:00+00'),
(23, 5, 17, '2026-04-08 22:18:00+00'),
(24, 4, 15, '2026-04-08 17:53:00+00'),
(24, 5, 18, '2026-04-08 17:53:00+00'),
(25, 4, 14, '2026-04-08 23:18:00+00'),
(25, 5, 17, '2026-04-08 23:18:00+00');

-- survey_responses (Likert=sq 3, FREE_TEXT=sq 4)
INSERT INTO survey_responses (student_id, survey_question_id, likert_value, free_text, submitted_at) VALUES
(20, 3, 5, NULL, '2026-04-08 20:14:00+00'),
(21, 3, 4, NULL, '2026-04-08 21:19:00+00'),
(22, 3, 2, NULL, '2026-04-08 18:49:00+00'),
(23, 3, 3, NULL, '2026-04-08 22:19:00+00'),
(24, 3, 4, NULL, '2026-04-08 17:54:00+00'),
(25, 3, 3, NULL, '2026-04-08 23:19:00+00'),
(23, 4, NULL, 'I understand wind patterns but not sure about greenhouse gases.', '2026-04-08 22:19:00+00');

-- -----------------------------------------------
-- Historical quiz questions and choices
-- No explicit IDs — sequences assign them, and quiz_responses
-- use subqueries keyed on (lesson_item_id, position, choice_text)
-- so the migration is safe regardless of sequence state.
-- -----------------------------------------------

-- Biology Lesson 1 (lesson_item_id=3): avg ~53% triggers HIGH_COMPLETION_LOW_SCORE
INSERT INTO quiz_questions (lesson_item_id, question_text, position, learning_objective) VALUES
(3, 'Which organelle is the site of protein synthesis?',  1, 'Organelle Function'),
(3, 'What is the primary function of the cell membrane?', 2, 'Cell Membrane');

INSERT INTO quiz_choices (quiz_question_id, choice_text, is_correct)
SELECT q.id, v.choice_text, v.is_correct
FROM quiz_questions q
CROSS JOIN (VALUES
  ('Nucleus',                                    false),
  ('Ribosome',                                   true),
  ('Mitochondria',                               false),
  ('Golgi apparatus',                            false)
) AS v(choice_text, is_correct)
WHERE q.lesson_item_id = 3 AND q.position = 1;

INSERT INTO quiz_choices (quiz_question_id, choice_text, is_correct)
SELECT q.id, v.choice_text, v.is_correct
FROM quiz_questions q
CROSS JOIN (VALUES
  ('Energy production',                          false),
  ('Controlling what enters and exits the cell', true),
  ('Storing genetic information',                false),
  ('Producing ribosomes',                        false)
) AS v(choice_text, is_correct)
WHERE q.lesson_item_id = 3 AND q.position = 2;

-- Biology Lesson 2 (lesson_item_id=7): avg ~82%, no flag
INSERT INTO quiz_questions (lesson_item_id, question_text, position, learning_objective) VALUES
(7, 'What is the shape of the DNA molecule?', 1, 'DNA Structure'),
(7, 'Which base pairs with adenine in DNA?',  2, 'Base Pairing');

INSERT INTO quiz_choices (quiz_question_id, choice_text, is_correct)
SELECT q.id, v.choice_text, v.is_correct
FROM quiz_questions q
CROSS JOIN (VALUES
  ('Straight strand', false),
  ('Double helix',    true),
  ('Triple helix',    false),
  ('Ring structure',  false)
) AS v(choice_text, is_correct)
WHERE q.lesson_item_id = 7 AND q.position = 1;

INSERT INTO quiz_choices (quiz_question_id, choice_text, is_correct)
SELECT q.id, v.choice_text, v.is_correct
FROM quiz_questions q
CROSS JOIN (VALUES
  ('Guanine',  false),
  ('Cytosine', false),
  ('Thymine',  true),
  ('Uracil',   false)
) AS v(choice_text, is_correct)
WHERE q.lesson_item_id = 7 AND q.position = 2;

-- EnvSci Lesson 6 (lesson_item_id=23): avg 40%, triggers HIGH_COMPLETION_LOW_SCORE
INSERT INTO quiz_questions (lesson_item_id, question_text, position, learning_objective) VALUES
(23, 'Which of Earth''s spheres includes all living organisms?', 1, 'Earth''s Spheres'),
(23, 'What layer of the atmosphere contains the ozone layer?',  2, 'Atmosphere Layers');

INSERT INTO quiz_choices (quiz_question_id, choice_text, is_correct)
SELECT q.id, v.choice_text, v.is_correct
FROM quiz_questions q
CROSS JOIN (VALUES
  ('Hydrosphere', false),
  ('Biosphere',   true),
  ('Lithosphere', false),
  ('Atmosphere',  false)
) AS v(choice_text, is_correct)
WHERE q.lesson_item_id = 23 AND q.position = 1;

INSERT INTO quiz_choices (quiz_question_id, choice_text, is_correct)
SELECT q.id, v.choice_text, v.is_correct
FROM quiz_questions q
CROSS JOIN (VALUES
  ('Troposphere',  false),
  ('Stratosphere', true),
  ('Mesosphere',   false),
  ('Thermosphere', false)
) AS v(choice_text, is_correct)
WHERE q.lesson_item_id = 23 AND q.position = 2;

-- -----------------------------------------------
-- Historical lesson events: Biology Lesson 1
-- All 17 Biology students (1-8, 11-19), 2026-01-22
-- -----------------------------------------------
INSERT INTO lesson_events (student_id, lesson_id, event_type, occurred_at) VALUES
(1,  1, 'STARTED',   '2026-01-22 18:00:00+00'),
(1,  1, 'COMPLETED', '2026-01-22 18:50:00+00'),
(2,  1, 'STARTED',   '2026-01-22 18:30:00+00'),
(2,  1, 'COMPLETED', '2026-01-22 19:20:00+00'),
(3,  1, 'STARTED',   '2026-01-22 19:00:00+00'),
(3,  1, 'COMPLETED', '2026-01-22 19:50:00+00'),
(4,  1, 'STARTED',   '2026-01-22 19:30:00+00'),
(4,  1, 'COMPLETED', '2026-01-22 20:20:00+00'),
(5,  1, 'STARTED',   '2026-01-22 20:00:00+00'),
(5,  1, 'COMPLETED', '2026-01-22 20:50:00+00'),
(6,  1, 'STARTED',   '2026-01-22 20:30:00+00'),
(6,  1, 'COMPLETED', '2026-01-22 21:20:00+00'),
(7,  1, 'STARTED',   '2026-01-22 17:30:00+00'),
(7,  1, 'COMPLETED', '2026-01-22 18:20:00+00'),
(8,  1, 'STARTED',   '2026-01-22 21:00:00+00'),
(8,  1, 'COMPLETED', '2026-01-22 21:50:00+00'),
(11, 1, 'STARTED',   '2026-01-22 18:15:00+00'),
(11, 1, 'COMPLETED', '2026-01-22 19:05:00+00'),
(12, 1, 'STARTED',   '2026-01-22 19:45:00+00'),
(12, 1, 'COMPLETED', '2026-01-22 20:35:00+00'),
(13, 1, 'STARTED',   '2026-01-22 21:15:00+00'),
(13, 1, 'COMPLETED', '2026-01-22 22:05:00+00'),
(14, 1, 'STARTED',   '2026-01-22 17:45:00+00'),
(14, 1, 'COMPLETED', '2026-01-22 18:35:00+00'),
(15, 1, 'STARTED',   '2026-01-22 20:15:00+00'),
(15, 1, 'COMPLETED', '2026-01-22 21:05:00+00'),
(16, 1, 'STARTED',   '2026-01-22 22:00:00+00'),
(16, 1, 'COMPLETED', '2026-01-22 22:50:00+00'),
(17, 1, 'STARTED',   '2026-01-22 18:45:00+00'),
(17, 1, 'COMPLETED', '2026-01-22 19:35:00+00'),
(18, 1, 'STARTED',   '2026-01-22 21:30:00+00'),
(18, 1, 'COMPLETED', '2026-01-22 22:20:00+00'),
(19, 1, 'STARTED',   '2026-01-22 22:30:00+00'),
(19, 1, 'COMPLETED', '2026-01-22 23:20:00+00');

-- Biology L1 quiz responses (using subqueries to avoid explicit choice IDs)
-- Both correct (Ribosome, Controlling...): 1, 2, 11, 12
-- Q1 correct + Q2 wrong (Ribosome, Energy production): 3, 5, 7, 13, 15
-- Q1 wrong + Q2 correct (Nucleus, Controlling...): 4, 6, 8, 14, 17
-- Both wrong (Mitochondria, Storing...): 16, 18, 19

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (1,  '2026-01-22 18:48:00+00'::timestamptz),
  (2,  '2026-01-22 19:18:00+00'::timestamptz),
  (3,  '2026-01-22 19:48:00+00'::timestamptz),
  (5,  '2026-01-22 20:48:00+00'::timestamptz),
  (7,  '2026-01-22 18:18:00+00'::timestamptz),
  (11, '2026-01-22 19:03:00+00'::timestamptz),
  (12, '2026-01-22 20:33:00+00'::timestamptz),
  (13, '2026-01-22 22:03:00+00'::timestamptz),
  (15, '2026-01-22 21:03:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 3 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Ribosome';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (4,  '2026-01-22 20:18:00+00'::timestamptz),
  (6,  '2026-01-22 21:18:00+00'::timestamptz),
  (8,  '2026-01-22 21:48:00+00'::timestamptz),
  (14, '2026-01-22 18:33:00+00'::timestamptz),
  (17, '2026-01-22 19:33:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 3 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Nucleus';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (16, '2026-01-22 22:48:00+00'::timestamptz),
  (18, '2026-01-22 22:18:00+00'::timestamptz),
  (19, '2026-01-22 23:18:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 3 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Mitochondria';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (1,  '2026-01-22 18:49:00+00'::timestamptz),
  (2,  '2026-01-22 19:19:00+00'::timestamptz),
  (4,  '2026-01-22 20:19:00+00'::timestamptz),
  (6,  '2026-01-22 21:19:00+00'::timestamptz),
  (8,  '2026-01-22 21:49:00+00'::timestamptz),
  (11, '2026-01-22 19:04:00+00'::timestamptz),
  (12, '2026-01-22 20:34:00+00'::timestamptz),
  (14, '2026-01-22 18:34:00+00'::timestamptz),
  (17, '2026-01-22 19:34:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 3 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Controlling what enters and exits the cell';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (3,  '2026-01-22 19:49:00+00'::timestamptz),
  (5,  '2026-01-22 20:49:00+00'::timestamptz),
  (7,  '2026-01-22 18:19:00+00'::timestamptz),
  (13, '2026-01-22 22:04:00+00'::timestamptz),
  (15, '2026-01-22 21:04:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 3 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Energy production';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (16, '2026-01-22 22:49:00+00'::timestamptz),
  (18, '2026-01-22 22:19:00+00'::timestamptz),
  (19, '2026-01-22 23:19:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 3 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Storing genetic information';

-- Biology L1 survey responses (survey_question_id=5 Likert)
INSERT INTO survey_responses (student_id, survey_question_id, likert_value, free_text, submitted_at) VALUES
(1,  5, 4, NULL, '2026-01-22 18:49:00+00'),
(2,  5, 5, NULL, '2026-01-22 19:19:00+00'),
(3,  5, 3, NULL, '2026-01-22 19:49:00+00'),
(4,  5, 3, NULL, '2026-01-22 20:19:00+00'),
(5,  5, 3, NULL, '2026-01-22 20:49:00+00'),
(6,  5, 2, NULL, '2026-01-22 21:19:00+00'),
(7,  5, 3, NULL, '2026-01-22 18:19:00+00'),
(8,  5, 2, NULL, '2026-01-22 21:49:00+00'),
(11, 5, 4, NULL, '2026-01-22 19:04:00+00'),
(12, 5, 4, NULL, '2026-01-22 20:34:00+00'),
(13, 5, 3, NULL, '2026-01-22 22:04:00+00'),
(14, 5, 3, NULL, '2026-01-22 18:34:00+00'),
(15, 5, 3, NULL, '2026-01-22 21:04:00+00'),
(16, 5, 1, NULL, '2026-01-22 22:49:00+00'),
(17, 5, 3, NULL, '2026-01-22 19:34:00+00'),
(18, 5, 2, NULL, '2026-01-22 22:19:00+00'),
(19, 5, 1, NULL, '2026-01-22 23:19:00+00');

-- -----------------------------------------------
-- Historical lesson events: Biology Lesson 2
-- All 17 Biology students, 2026-02-05
-- -----------------------------------------------
INSERT INTO lesson_events (student_id, lesson_id, event_type, occurred_at) VALUES
(1,  2, 'STARTED',   '2026-02-05 18:00:00+00'),
(1,  2, 'COMPLETED', '2026-02-05 18:50:00+00'),
(2,  2, 'STARTED',   '2026-02-05 18:30:00+00'),
(2,  2, 'COMPLETED', '2026-02-05 19:20:00+00'),
(3,  2, 'STARTED',   '2026-02-05 19:00:00+00'),
(3,  2, 'COMPLETED', '2026-02-05 19:50:00+00'),
(4,  2, 'STARTED',   '2026-02-05 19:30:00+00'),
(4,  2, 'COMPLETED', '2026-02-05 20:20:00+00'),
(5,  2, 'STARTED',   '2026-02-05 20:00:00+00'),
(5,  2, 'COMPLETED', '2026-02-05 20:50:00+00'),
(6,  2, 'STARTED',   '2026-02-05 20:30:00+00'),
(6,  2, 'COMPLETED', '2026-02-05 21:20:00+00'),
(7,  2, 'STARTED',   '2026-02-05 17:30:00+00'),
(7,  2, 'COMPLETED', '2026-02-05 18:20:00+00'),
(8,  2, 'STARTED',   '2026-02-05 21:00:00+00'),
(8,  2, 'COMPLETED', '2026-02-05 21:50:00+00'),
(11, 2, 'STARTED',   '2026-02-05 18:15:00+00'),
(11, 2, 'COMPLETED', '2026-02-05 19:05:00+00'),
(12, 2, 'STARTED',   '2026-02-05 19:45:00+00'),
(12, 2, 'COMPLETED', '2026-02-05 20:35:00+00'),
(13, 2, 'STARTED',   '2026-02-05 21:15:00+00'),
(13, 2, 'COMPLETED', '2026-02-05 22:05:00+00'),
(14, 2, 'STARTED',   '2026-02-05 17:45:00+00'),
(14, 2, 'COMPLETED', '2026-02-05 18:35:00+00'),
(15, 2, 'STARTED',   '2026-02-05 20:15:00+00'),
(15, 2, 'COMPLETED', '2026-02-05 21:05:00+00'),
(16, 2, 'STARTED',   '2026-02-05 22:00:00+00'),
(16, 2, 'COMPLETED', '2026-02-05 22:50:00+00'),
(17, 2, 'STARTED',   '2026-02-05 18:45:00+00'),
(17, 2, 'COMPLETED', '2026-02-05 19:35:00+00'),
(18, 2, 'STARTED',   '2026-02-05 21:30:00+00'),
(18, 2, 'COMPLETED', '2026-02-05 22:20:00+00'),
(19, 2, 'STARTED',   '2026-02-05 22:30:00+00'),
(19, 2, 'COMPLETED', '2026-02-05 23:20:00+00');

-- Biology L2 quiz responses
-- Both correct (Double helix, Thymine): 1,2,3,4,5,7,11,12,13,14,15,17
-- Q1 correct + Q2 wrong (Double helix, Guanine): 6,8,16,18
-- Both wrong (Straight strand, Guanine): 19

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (1,  '2026-02-05 18:48:00+00'::timestamptz),
  (2,  '2026-02-05 19:18:00+00'::timestamptz),
  (3,  '2026-02-05 19:48:00+00'::timestamptz),
  (4,  '2026-02-05 20:18:00+00'::timestamptz),
  (5,  '2026-02-05 20:48:00+00'::timestamptz),
  (6,  '2026-02-05 21:18:00+00'::timestamptz),
  (7,  '2026-02-05 18:18:00+00'::timestamptz),
  (8,  '2026-02-05 21:48:00+00'::timestamptz),
  (11, '2026-02-05 19:03:00+00'::timestamptz),
  (12, '2026-02-05 20:33:00+00'::timestamptz),
  (13, '2026-02-05 22:03:00+00'::timestamptz),
  (14, '2026-02-05 18:33:00+00'::timestamptz),
  (15, '2026-02-05 21:03:00+00'::timestamptz),
  (16, '2026-02-05 22:48:00+00'::timestamptz),
  (17, '2026-02-05 19:33:00+00'::timestamptz),
  (18, '2026-02-05 22:18:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 7 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Double helix';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (19, '2026-02-05 23:18:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 7 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Straight strand';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (1,  '2026-02-05 18:49:00+00'::timestamptz),
  (2,  '2026-02-05 19:19:00+00'::timestamptz),
  (3,  '2026-02-05 19:49:00+00'::timestamptz),
  (4,  '2026-02-05 20:19:00+00'::timestamptz),
  (5,  '2026-02-05 20:49:00+00'::timestamptz),
  (7,  '2026-02-05 18:19:00+00'::timestamptz),
  (11, '2026-02-05 19:04:00+00'::timestamptz),
  (12, '2026-02-05 20:34:00+00'::timestamptz),
  (13, '2026-02-05 22:04:00+00'::timestamptz),
  (14, '2026-02-05 18:34:00+00'::timestamptz),
  (15, '2026-02-05 21:04:00+00'::timestamptz),
  (17, '2026-02-05 19:34:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 7 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Thymine';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (6,  '2026-02-05 21:19:00+00'::timestamptz),
  (8,  '2026-02-05 21:49:00+00'::timestamptz),
  (16, '2026-02-05 22:49:00+00'::timestamptz),
  (18, '2026-02-05 22:19:00+00'::timestamptz),
  (19, '2026-02-05 23:19:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 7 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Guanine';

-- Biology L2 survey responses (survey_question_id=7 Likert)
INSERT INTO survey_responses (student_id, survey_question_id, likert_value, free_text, submitted_at) VALUES
(1,  7, 5, NULL, '2026-02-05 18:49:00+00'),
(2,  7, 5, NULL, '2026-02-05 19:19:00+00'),
(3,  7, 4, NULL, '2026-02-05 19:49:00+00'),
(4,  7, 4, NULL, '2026-02-05 20:19:00+00'),
(5,  7, 4, NULL, '2026-02-05 20:49:00+00'),
(6,  7, 3, NULL, '2026-02-05 21:19:00+00'),
(7,  7, 4, NULL, '2026-02-05 18:19:00+00'),
(8,  7, 3, NULL, '2026-02-05 21:49:00+00'),
(11, 7, 5, NULL, '2026-02-05 19:04:00+00'),
(12, 7, 4, NULL, '2026-02-05 20:34:00+00'),
(13, 7, 4, NULL, '2026-02-05 22:04:00+00'),
(14, 7, 4, NULL, '2026-02-05 18:34:00+00'),
(15, 7, 4, NULL, '2026-02-05 21:04:00+00'),
(16, 7, 3, NULL, '2026-02-05 22:49:00+00'),
(17, 7, 4, NULL, '2026-02-05 19:34:00+00'),
(18, 7, 3, NULL, '2026-02-05 22:19:00+00'),
(19, 7, 2, NULL, '2026-02-05 23:19:00+00');

-- -----------------------------------------------
-- Historical lesson events: EnvSci Lesson 6
-- All 10 EnvSci students (9,10,20-27), 2026-01-29
-- -----------------------------------------------
INSERT INTO lesson_events (student_id, lesson_id, event_type, occurred_at) VALUES
(9,  6, 'STARTED',   '2026-01-29 18:00:00+00'),
(9,  6, 'COMPLETED', '2026-01-29 18:50:00+00'),
(10, 6, 'STARTED',   '2026-01-29 18:30:00+00'),
(10, 6, 'COMPLETED', '2026-01-29 19:20:00+00'),
(20, 6, 'STARTED',   '2026-01-29 19:00:00+00'),
(20, 6, 'COMPLETED', '2026-01-29 19:50:00+00'),
(21, 6, 'STARTED',   '2026-01-29 19:30:00+00'),
(21, 6, 'COMPLETED', '2026-01-29 20:20:00+00'),
(22, 6, 'STARTED',   '2026-01-29 20:00:00+00'),
(22, 6, 'COMPLETED', '2026-01-29 20:50:00+00'),
(23, 6, 'STARTED',   '2026-01-29 20:30:00+00'),
(23, 6, 'COMPLETED', '2026-01-29 21:20:00+00'),
(24, 6, 'STARTED',   '2026-01-29 17:30:00+00'),
(24, 6, 'COMPLETED', '2026-01-29 18:20:00+00'),
(25, 6, 'STARTED',   '2026-01-29 21:00:00+00'),
(25, 6, 'COMPLETED', '2026-01-29 21:50:00+00'),
(26, 6, 'STARTED',   '2026-01-29 17:00:00+00'),
(26, 6, 'COMPLETED', '2026-01-29 17:50:00+00'),
(27, 6, 'STARTED',   '2026-01-29 21:30:00+00'),
(27, 6, 'COMPLETED', '2026-01-29 22:20:00+00');

-- EnvSci L6 quiz responses
-- Both correct (Biosphere, Stratosphere): 9, 20, 21
-- Q1 correct + Q2 wrong (Biosphere, Troposphere): 10, 22
-- Both wrong (Hydrosphere, Troposphere): 23, 24, 25, 26, 27

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (9,  '2026-01-29 18:48:00+00'::timestamptz),
  (10, '2026-01-29 19:18:00+00'::timestamptz),
  (20, '2026-01-29 19:48:00+00'::timestamptz),
  (21, '2026-01-29 20:18:00+00'::timestamptz),
  (22, '2026-01-29 20:48:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 23 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Biosphere';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (23, '2026-01-29 21:18:00+00'::timestamptz),
  (24, '2026-01-29 17:18:00+00'::timestamptz),
  (25, '2026-01-29 21:48:00+00'::timestamptz),
  (26, '2026-01-29 17:48:00+00'::timestamptz),
  (27, '2026-01-29 22:18:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 23 AND q.position = 1
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Hydrosphere';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (9,  '2026-01-29 18:49:00+00'::timestamptz),
  (20, '2026-01-29 19:49:00+00'::timestamptz),
  (21, '2026-01-29 20:19:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 23 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Stratosphere';

INSERT INTO quiz_responses (student_id, quiz_question_id, quiz_choice_id, submitted_at)
SELECT s.sid, q.id, c.id, s.ts
FROM (VALUES
  (10, '2026-01-29 19:19:00+00'::timestamptz),
  (22, '2026-01-29 20:49:00+00'::timestamptz),
  (23, '2026-01-29 21:19:00+00'::timestamptz),
  (24, '2026-01-29 17:19:00+00'::timestamptz),
  (25, '2026-01-29 21:49:00+00'::timestamptz),
  (26, '2026-01-29 17:49:00+00'::timestamptz),
  (27, '2026-01-29 22:19:00+00'::timestamptz)
) AS s(sid, ts)
JOIN quiz_questions q ON q.lesson_item_id = 23 AND q.position = 2
JOIN quiz_choices c ON c.quiz_question_id = q.id AND c.choice_text = 'Troposphere';

-- EnvSci L6 survey responses (survey_question_id=13 Likert)
INSERT INTO survey_responses (student_id, survey_question_id, likert_value, free_text, submitted_at) VALUES
(9,  13, 5, NULL, '2026-01-29 18:49:00+00'),
(10, 13, 3, NULL, '2026-01-29 19:19:00+00'),
(20, 13, 5, NULL, '2026-01-29 19:49:00+00'),
(21, 13, 4, NULL, '2026-01-29 20:19:00+00'),
(22, 13, 3, NULL, '2026-01-29 20:49:00+00'),
(23, 13, 2, NULL, '2026-01-29 21:19:00+00'),
(24, 13, 2, NULL, '2026-01-29 17:19:00+00'),
(25, 13, 2, NULL, '2026-01-29 21:49:00+00'),
(26, 13, 1, NULL, '2026-01-29 17:49:00+00'),
(27, 13, 1, NULL, '2026-01-29 22:19:00+00');

-- -----------------------------------------------
-- Reset sequences
-- -----------------------------------------------
SELECT setval('students_id_seq',           (SELECT MAX(id) FROM students));
SELECT setval('quiz_questions_id_seq',     (SELECT MAX(id) FROM quiz_questions));
SELECT setval('quiz_choices_id_seq',       (SELECT MAX(id) FROM quiz_choices));
SELECT setval('lesson_events_id_seq',      (SELECT MAX(id) FROM lesson_events));
SELECT setval('video_watch_events_id_seq', (SELECT MAX(id) FROM video_watch_events));
SELECT setval('quiz_responses_id_seq',     (SELECT MAX(id) FROM quiz_responses));
SELECT setval('survey_responses_id_seq',   (SELECT MAX(id) FROM survey_responses));
