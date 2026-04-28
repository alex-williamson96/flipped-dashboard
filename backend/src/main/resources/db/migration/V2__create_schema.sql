CREATE TABLE courses (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE sections (
    id        BIGSERIAL PRIMARY KEY,
    course_id BIGINT       NOT NULL REFERENCES courses(id),
    name      VARCHAR(100) NOT NULL
);

CREATE TABLE lessons (
    id        BIGSERIAL PRIMARY KEY,
    course_id BIGINT       NOT NULL REFERENCES courses(id),
    title     VARCHAR(255) NOT NULL,
    position  INT          NOT NULL,
    is_active BOOLEAN      NOT NULL DEFAULT false
);

CREATE TABLE lesson_items (
    id        BIGSERIAL   PRIMARY KEY,
    lesson_id BIGINT      NOT NULL REFERENCES lessons(id),
    position  INT         NOT NULL,
    type      VARCHAR(20) NOT NULL,
    content   JSONB
);

CREATE TABLE quiz_questions (
    id                  BIGSERIAL PRIMARY KEY,
    lesson_item_id      BIGINT    NOT NULL REFERENCES lesson_items(id),
    question_text       TEXT      NOT NULL,
    position            INT       NOT NULL,
    learning_objective  TEXT
);

CREATE TABLE quiz_choices (
    id               BIGSERIAL PRIMARY KEY,
    quiz_question_id BIGINT  NOT NULL REFERENCES quiz_questions(id),
    choice_text      TEXT    NOT NULL,
    is_correct       BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE survey_questions (
    id             BIGSERIAL   PRIMARY KEY,
    lesson_item_id BIGINT      NOT NULL REFERENCES lesson_items(id),
    question_text  TEXT        NOT NULL,
    type           VARCHAR(20) NOT NULL,
    position       INT         NOT NULL
);

CREATE TABLE students (
    id         BIGSERIAL    PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    section_id BIGINT       NOT NULL REFERENCES sections(id)
);

CREATE TABLE lesson_events (
    id          BIGSERIAL   PRIMARY KEY,
    student_id  BIGINT      NOT NULL REFERENCES students(id),
    lesson_id   BIGINT      NOT NULL REFERENCES lessons(id),
    event_type  VARCHAR(20) NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE video_watch_events (
    id             BIGSERIAL   PRIMARY KEY,
    student_id     BIGINT      NOT NULL REFERENCES students(id),
    lesson_item_id BIGINT      NOT NULL REFERENCES lesson_items(id),
    watch_percent  INT         NOT NULL,
    marked_at      TIMESTAMPTZ NOT NULL
);

CREATE TABLE quiz_responses (
    id               BIGSERIAL   PRIMARY KEY,
    student_id       BIGINT      NOT NULL REFERENCES students(id),
    quiz_question_id BIGINT      NOT NULL REFERENCES quiz_questions(id),
    quiz_choice_id   BIGINT      NOT NULL REFERENCES quiz_choices(id),
    submitted_at     TIMESTAMPTZ NOT NULL
);

CREATE TABLE survey_responses (
    id                 BIGSERIAL   PRIMARY KEY,
    student_id         BIGINT      NOT NULL REFERENCES students(id),
    survey_question_id BIGINT      NOT NULL REFERENCES survey_questions(id),
    likert_value       INT,
    free_text          TEXT,
    submitted_at       TIMESTAMPTZ NOT NULL
);
