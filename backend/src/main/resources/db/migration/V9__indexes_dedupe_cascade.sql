-- FK indexes on every foreign-key column.
-- Postgres does not create these automatically and analytics queries filter on them.
CREATE INDEX IF NOT EXISTS idx_sections_course_id              ON sections(course_id);
CREATE INDEX IF NOT EXISTS idx_lessons_course_id               ON lessons(course_id);
CREATE INDEX IF NOT EXISTS idx_lesson_items_lesson_id          ON lesson_items(lesson_id);
CREATE INDEX IF NOT EXISTS idx_quiz_questions_lesson_item_id   ON quiz_questions(lesson_item_id);
CREATE INDEX IF NOT EXISTS idx_quiz_choices_quiz_question_id   ON quiz_choices(quiz_question_id);
CREATE INDEX IF NOT EXISTS idx_survey_questions_lesson_item_id ON survey_questions(lesson_item_id);
CREATE INDEX IF NOT EXISTS idx_students_section_id             ON students(section_id);
CREATE INDEX IF NOT EXISTS idx_lesson_events_student_id        ON lesson_events(student_id);
CREATE INDEX IF NOT EXISTS idx_lesson_events_lesson_id         ON lesson_events(lesson_id);
CREATE INDEX IF NOT EXISTS idx_video_watch_events_student_id   ON video_watch_events(student_id);
CREATE INDEX IF NOT EXISTS idx_video_watch_events_item_id      ON video_watch_events(lesson_item_id);
CREATE INDEX IF NOT EXISTS idx_quiz_responses_student_id       ON quiz_responses(student_id);
CREATE INDEX IF NOT EXISTS idx_quiz_responses_question_id      ON quiz_responses(quiz_question_id);
CREATE INDEX IF NOT EXISTS idx_quiz_responses_choice_id        ON quiz_responses(quiz_choice_id);
CREATE INDEX IF NOT EXISTS idx_survey_responses_student_id     ON survey_responses(student_id);
CREATE INDEX IF NOT EXISTS idx_survey_responses_question_id    ON survey_responses(survey_question_id);

-- Composite index for the hot-path completion-check repository query.
CREATE INDEX IF NOT EXISTS idx_lesson_events_student_lesson_type
    ON lesson_events(student_id, lesson_id, event_type);

-- Unique constraints preventing duplicate submissions.
-- Pair with service-layer existence checks; the constraint is the ultimate guard.
-- If a dev DB already contains duplicates, reset with `docker compose down -v`.
ALTER TABLE quiz_responses
    ADD CONSTRAINT uq_quiz_responses_student_question
    UNIQUE (student_id, quiz_question_id);

ALTER TABLE survey_responses
    ADD CONSTRAINT uq_survey_responses_student_question
    UNIQUE (student_id, survey_question_id);

ALTER TABLE video_watch_events
    ADD CONSTRAINT uq_video_watch_events_student_item
    UNIQUE (student_id, lesson_item_id);

ALTER TABLE lesson_events
    ADD CONSTRAINT uq_lesson_events_student_lesson_type
    UNIQUE (student_id, lesson_id, event_type);

-- ON DELETE CASCADE across the course hierarchy so a hard-delete on a course
-- removes its sections, lessons, items, and student-generated response rows.
-- Soft-delete via is_archived remains the preferred path for active courses.
ALTER TABLE sections         DROP CONSTRAINT sections_course_id_fkey;
ALTER TABLE sections         ADD  CONSTRAINT sections_course_id_fkey
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE;

ALTER TABLE lessons          DROP CONSTRAINT lessons_course_id_fkey;
ALTER TABLE lessons          ADD  CONSTRAINT lessons_course_id_fkey
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE;

ALTER TABLE lesson_items     DROP CONSTRAINT lesson_items_lesson_id_fkey;
ALTER TABLE lesson_items     ADD  CONSTRAINT lesson_items_lesson_id_fkey
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE;

ALTER TABLE quiz_questions   DROP CONSTRAINT quiz_questions_lesson_item_id_fkey;
ALTER TABLE quiz_questions   ADD  CONSTRAINT quiz_questions_lesson_item_id_fkey
    FOREIGN KEY (lesson_item_id) REFERENCES lesson_items(id) ON DELETE CASCADE;

ALTER TABLE quiz_choices     DROP CONSTRAINT quiz_choices_quiz_question_id_fkey;
ALTER TABLE quiz_choices     ADD  CONSTRAINT quiz_choices_quiz_question_id_fkey
    FOREIGN KEY (quiz_question_id) REFERENCES quiz_questions(id) ON DELETE CASCADE;

ALTER TABLE survey_questions DROP CONSTRAINT survey_questions_lesson_item_id_fkey;
ALTER TABLE survey_questions ADD  CONSTRAINT survey_questions_lesson_item_id_fkey
    FOREIGN KEY (lesson_item_id) REFERENCES lesson_items(id) ON DELETE CASCADE;

ALTER TABLE students         DROP CONSTRAINT students_section_id_fkey;
ALTER TABLE students         ADD  CONSTRAINT students_section_id_fkey
    FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE;

ALTER TABLE lesson_events      DROP CONSTRAINT lesson_events_student_id_fkey;
ALTER TABLE lesson_events      ADD  CONSTRAINT lesson_events_student_id_fkey
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE;

ALTER TABLE lesson_events      DROP CONSTRAINT lesson_events_lesson_id_fkey;
ALTER TABLE lesson_events      ADD  CONSTRAINT lesson_events_lesson_id_fkey
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE;

ALTER TABLE video_watch_events DROP CONSTRAINT video_watch_events_student_id_fkey;
ALTER TABLE video_watch_events ADD  CONSTRAINT video_watch_events_student_id_fkey
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE;

ALTER TABLE video_watch_events DROP CONSTRAINT video_watch_events_lesson_item_id_fkey;
ALTER TABLE video_watch_events ADD  CONSTRAINT video_watch_events_lesson_item_id_fkey
    FOREIGN KEY (lesson_item_id) REFERENCES lesson_items(id) ON DELETE CASCADE;

ALTER TABLE quiz_responses     DROP CONSTRAINT quiz_responses_student_id_fkey;
ALTER TABLE quiz_responses     ADD  CONSTRAINT quiz_responses_student_id_fkey
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE;

ALTER TABLE quiz_responses     DROP CONSTRAINT quiz_responses_quiz_question_id_fkey;
ALTER TABLE quiz_responses     ADD  CONSTRAINT quiz_responses_quiz_question_id_fkey
    FOREIGN KEY (quiz_question_id) REFERENCES quiz_questions(id) ON DELETE CASCADE;

ALTER TABLE quiz_responses     DROP CONSTRAINT quiz_responses_quiz_choice_id_fkey;
ALTER TABLE quiz_responses     ADD  CONSTRAINT quiz_responses_quiz_choice_id_fkey
    FOREIGN KEY (quiz_choice_id) REFERENCES quiz_choices(id) ON DELETE CASCADE;

ALTER TABLE survey_responses   DROP CONSTRAINT survey_responses_student_id_fkey;
ALTER TABLE survey_responses   ADD  CONSTRAINT survey_responses_student_id_fkey
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE;

ALTER TABLE survey_responses   DROP CONSTRAINT survey_responses_survey_question_id_fkey;
ALTER TABLE survey_responses   ADD  CONSTRAINT survey_responses_survey_question_id_fkey
    FOREIGN KEY (survey_question_id) REFERENCES survey_questions(id) ON DELETE CASCADE;
