-- courses
INSERT INTO courses (id, name, description) VALUES
(1, 'Introduction to Biology', 'A foundational biology course covering cell biology, genetics, and ecosystems.'),
(2, 'Environmental Science', 'An introduction to environmental systems, sustainability, and ecological impact.');

-- sections
INSERT INTO sections (id, course_id, name) VALUES
(1, 1, 'Period 1'),
(2, 1, 'Period 2'),
(3, 1, 'Period 3'),
(4, 2, 'Section 001'),
(5, 2, 'Section 002');

-- lessons
INSERT INTO lessons (id, course_id, title, position, is_active) VALUES
(1,  1, 'Cell Structure and Function',      1, false),
(2,  1, 'DNA and Genetics',                 2, false),
(3,  1, 'Photosynthesis and Respiration',   3, true),
(4,  1, 'Ecosystems and Biodiversity',      4, false),
(5,  1, 'Evolution and Natural Selection',  5, false),
(6,  2, 'Earth''s Systems',                 1, false),
(7,  2, 'Climate and Weather',              2, true),
(8,  2, 'Water Cycle and Resources',        3, false),
(9,  2, 'Biodiversity and Habitats',        4, false),
(10, 2, 'Human Impact and Sustainability',  5, false);

-- lesson_items (4 per lesson: VIDEO, TEXT, QUIZ, SURVEY)
INSERT INTO lesson_items (id, lesson_id, position, type, content) VALUES
-- Lesson 1
(1,  1, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=PNTU8V3TiZU"}'),
(2,  1, 2, 'TEXT',   '{"body":"Explore the structure of the eukaryotic cell and the specialized functions of organelles such as the nucleus, mitochondria, and endoplasmic reticulum."}'),
(3,  1, 3, 'QUIZ',   NULL),
(4,  1, 4, 'SURVEY', NULL),
-- Lesson 2
(5,  2, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=TNKWgcFPHqw"}'),
(6,  2, 2, 'TEXT',   '{"body":"Examine the structure of DNA, the mechanism of replication, and the principles of Mendelian inheritance that govern genetic traits."}'),
(7,  2, 3, 'QUIZ',   NULL),
(8,  2, 4, 'SURVEY', NULL),
-- Lesson 3 (active Biology lesson)
(9,  3, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=2Jld_y_OXfk"}'),
(10, 3, 2, 'TEXT',   '{"body":"Compare the processes of photosynthesis and cellular respiration, tracing how cells capture light energy and convert glucose into usable ATP."}'),
(11, 3, 3, 'QUIZ',   NULL),
(12, 3, 4, 'SURVEY', NULL),
-- Lesson 4
(13, 4, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=dQw4w9WgXcQ"}'),
(14, 4, 2, 'TEXT',   '{"body":"Survey the major terrestrial and aquatic biomes, the flow of energy through food webs, and the importance of biodiversity for ecosystem stability."}'),
(15, 4, 3, 'QUIZ',   NULL),
(16, 4, 4, 'SURVEY', NULL),
-- Lesson 5
(17, 5, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=GhHOjC4oxh8"}'),
(18, 5, 2, 'TEXT',   '{"body":"Investigate the mechanisms of natural selection, genetic drift, and speciation that drive evolutionary change over generations."}'),
(19, 5, 3, 'QUIZ',   NULL),
(20, 5, 4, 'SURVEY', NULL),
-- Lesson 6
(21, 6, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=uQuhO9QLC1Q"}'),
(22, 6, 2, 'TEXT',   '{"body":"Describe the interactions among Earth''s lithosphere, hydrosphere, atmosphere, and biosphere and how energy flows between them."}'),
(23, 6, 3, 'QUIZ',   NULL),
(24, 6, 4, 'SURVEY', NULL),
-- Lesson 7 (active EnvSci lesson)
(25, 7, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=83S9GJIqHUQ"}'),
(26, 7, 2, 'TEXT',   '{"body":"Analyze how differential solar heating and Earth''s rotation produce global wind patterns and the major climate zones found across the planet."}'),
(27, 7, 3, 'QUIZ',   NULL),
(28, 7, 4, 'SURVEY', NULL),
-- Lesson 8
(29, 8, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=NCKJMbMCqoU"}'),
(30, 8, 2, 'TEXT',   '{"body":"Trace the continuous movement of water through evaporation, condensation, and precipitation, and examine freshwater scarcity as a global resource challenge."}'),
(31, 8, 3, 'QUIZ',   NULL),
(32, 8, 4, 'SURVEY', NULL),
-- Lesson 9
(33, 9, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=QkPa6hqK4ok"}'),
(34, 9, 2, 'TEXT',   '{"body":"Identify key terrestrial and aquatic habitats, the organisms they support, and the consequences of habitat loss for global biodiversity."}'),
(35, 9, 3, 'QUIZ',   NULL),
(36, 9, 4, 'SURVEY', NULL),
-- Lesson 10
(37, 10, 1, 'VIDEO',  '{"url":"https://www.youtube.com/watch?v=5aH2Ppjpno0"}'),
(38, 10, 2, 'TEXT',   '{"body":"Evaluate how human activities such as deforestation, pollution, and fossil fuel combustion alter ecological systems and discuss pathways toward sustainability."}'),
(39, 10, 3, 'QUIZ',   NULL),
(40, 10, 4, 'SURVEY', NULL);

-- quiz_questions (active lessons only: lesson_item_id=11 and 27)
INSERT INTO quiz_questions (id, lesson_item_id, question_text, position, learning_objective) VALUES
(1, 11, 'What is the primary function of mitochondria?',                  1, 'Organelle Function'),
(2, 11, 'Which molecule carries energy in cells?',                        2, 'Cellular Energy Currency'),
(3, 11, 'What is the net ATP yield of glycolysis?',                       3, 'Glycolysis'),
(4, 27, 'What drives global wind patterns?',                              1, 'Atmospheric Circulation'),
(5, 27, 'Which greenhouse gas is most abundant in Earth''s atmosphere?',  2, 'Greenhouse Gases');

-- quiz_choices
INSERT INTO quiz_choices (id, quiz_question_id, choice_text, is_correct) VALUES
-- Q1
(1,  1, 'Protein synthesis',        false),
(2,  1, 'Energy production (ATP)',  true),
(3,  1, 'Cell division',            false),
(4,  1, 'Waste removal',            false),
-- Q2
(5,  2, 'DNA',     false),
(6,  2, 'RNA',     false),
(7,  2, 'ATP',     true),
(8,  2, 'Glucose', false),
-- Q3
(9,  3, '2 ATP',  true),
(10, 3, '4 ATP',  false),
(11, 3, '36 ATP', false),
(12, 3, '38 ATP', false),
-- Q4
(13, 4, 'Ocean currents',                        false),
(14, 4, 'Earth''s rotation and uneven heating',  true),
(15, 4, 'Lunar gravity',                         false),
(16, 4, 'Volcanic activity',                     false),
-- Q5
(17, 5, 'Carbon dioxide', false),
(18, 5, 'Methane',        false),
(19, 5, 'Water vapor',    true),
(20, 5, 'Ozone',          false);

-- survey_questions (active lessons only: lesson_item_id=12 and 28)
INSERT INTO survey_questions (id, lesson_item_id, question_text, type, position) VALUES
(1, 12, 'How confident are you in your understanding of cellular energy processes?', 'LIKERT',    1),
(2, 12, 'What aspect of photosynthesis or respiration is still unclear to you?',     'FREE_TEXT', 2),
(3, 28, 'How confident are you in your understanding of climate systems?',           'LIKERT',    1),
(4, 28, 'What questions do you have about weather patterns or climate?',             'FREE_TEXT', 2);

-- students
INSERT INTO students (id, name, section_id) VALUES
(1,  'Avery Johnson',   1),
(2,  'Blake Martinez',  1),
(3,  'Casey Nguyen',    1),
(4,  'Dana Patel',      2),
(5,  'Ellis Rodriguez', 2),
(6,  'Fiona Lee',       2),
(7,  'George Kim',      3),
(8,  'Hannah Osei',     3),
(9,  'Ivan Choi',       4),
(10, 'Jade Thompson',   5);

-- lesson_events
INSERT INTO lesson_events (id, student_id, lesson_id, event_type, occurred_at) VALUES
-- Biology lesson 3
(1,  1,  3, 'COMPLETED', '2026-03-15 19:30:00+00'),
(2,  2,  3, 'COMPLETED', '2026-03-15 20:15:00+00'),
(3,  3,  3, 'COMPLETED', '2026-03-15 18:45:00+00'),
(4,  4,  3, 'COMPLETED', '2026-03-15 21:00:00+00'),
(5,  5,  3, 'COMPLETED', '2026-03-15 22:10:00+00'),
(6,  6,  3, 'COMPLETED', '2026-03-15 19:55:00+00'),
(7,  7,  3, 'COMPLETED', '2026-03-15 20:40:00+00'),
(8,  9,  3, 'STARTED',   '2026-03-15 23:05:00+00'),
(9,  10, 3, 'STARTED',   '2026-03-15 21:30:00+00'),
-- EnvSci lesson 7
(10, 9,  7, 'COMPLETED', '2026-03-15 20:00:00+00'),
(11, 10, 7, 'STARTED',   '2026-03-15 21:00:00+00');

-- video_watch_events
INSERT INTO video_watch_events (id, student_id, lesson_item_id, watch_percent, marked_at) VALUES
-- lesson_item_id=9 (Biology lesson 3)
(1,  1,  9, 100, '2026-03-15 19:20:00+00'),
(2,  2,  9,  95, '2026-03-15 20:05:00+00'),
(3,  3,  9,  88, '2026-03-15 18:35:00+00'),
(4,  4,  9, 100, '2026-03-15 20:50:00+00'),
(5,  5,  9,  82, '2026-03-15 22:00:00+00'),
(6,  6,  9,  91, '2026-03-15 19:45:00+00'),
(7,  7,  9, 100, '2026-03-15 20:30:00+00'),
(8,  9,  9,  38, '2026-03-15 23:00:00+00'),
(9,  10, 9,  45, '2026-03-15 21:25:00+00'),
-- lesson_item_id=25 (EnvSci lesson 7)
(10, 9,  25, 100, '2026-03-15 19:50:00+00'),
(11, 10, 25,  15, '2026-03-15 20:55:00+00');

-- quiz_responses (Biology: questions 1,2,3; EnvSci: questions 4,5)
INSERT INTO quiz_responses (id, student_id, quiz_question_id, quiz_choice_id, submitted_at) VALUES
-- student 1: Q1=2(correct), Q2=7(correct), Q3=10(wrong)
(1,  1, 1, 2,  '2026-03-15 19:28:00+00'),
(2,  1, 2, 7,  '2026-03-15 19:28:00+00'),
(3,  1, 3, 10, '2026-03-15 19:29:00+00'),
-- student 2: Q1=2(correct), Q2=7(correct), Q3=9(correct)
(4,  2, 1, 2,  '2026-03-15 20:13:00+00'),
(5,  2, 2, 7,  '2026-03-15 20:13:00+00'),
(6,  2, 3, 9,  '2026-03-15 20:14:00+00'),
-- student 3: Q1=2(correct), Q2=7(correct), Q3=11(wrong)
(7,  3, 1, 2,  '2026-03-15 18:43:00+00'),
(8,  3, 2, 7,  '2026-03-15 18:43:00+00'),
(9,  3, 3, 11, '2026-03-15 18:44:00+00'),
-- student 4: Q1=1(wrong), Q2=7(correct), Q3=10(wrong)
(10, 4, 1, 1,  '2026-03-15 20:58:00+00'),
(11, 4, 2, 7,  '2026-03-15 20:58:00+00'),
(12, 4, 3, 10, '2026-03-15 20:59:00+00'),
-- student 5: Q1=2(correct), Q2=6(wrong), Q3=9(correct)
(13, 5, 1, 2,  '2026-03-15 22:08:00+00'),
(14, 5, 2, 6,  '2026-03-15 22:08:00+00'),
(15, 5, 3, 9,  '2026-03-15 22:09:00+00'),
-- student 6: Q1=2(correct), Q2=7(correct), Q3=12(wrong)
(16, 6, 1, 2,  '2026-03-15 19:53:00+00'),
(17, 6, 2, 7,  '2026-03-15 19:53:00+00'),
(18, 6, 3, 12, '2026-03-15 19:54:00+00'),
-- student 7: Q1=3(wrong), Q2=7(correct), Q3=9(correct)
(19, 7, 1, 3,  '2026-03-15 20:38:00+00'),
(20, 7, 2, 7,  '2026-03-15 20:38:00+00'),
(21, 7, 3, 9,  '2026-03-15 20:39:00+00'),
-- student 9: Q1=2(correct), Q2=5(wrong), Q3=10(wrong)
(22, 9, 1, 2,  '2026-03-15 23:03:00+00'),
(23, 9, 2, 5,  '2026-03-15 23:03:00+00'),
(24, 9, 3, 10, '2026-03-15 23:04:00+00'),
-- student 10: Q1=2(correct), Q2=7(correct), Q3=11(wrong)
(25, 10, 1, 2,  '2026-03-15 21:28:00+00'),
(26, 10, 2, 7,  '2026-03-15 21:28:00+00'),
(27, 10, 3, 11, '2026-03-15 21:29:00+00'),
-- EnvSci: student 9: Q4=14(correct), Q5=19(correct)
(28, 9, 4, 14, '2026-03-15 19:58:00+00'),
(29, 9, 5, 19, '2026-03-15 19:58:00+00'),
-- EnvSci: student 10: Q4=13(wrong), Q5=17(wrong)
(30, 10, 4, 13, '2026-03-15 20:58:00+00'),
(31, 10, 5, 17, '2026-03-15 20:58:00+00');

-- survey_responses
INSERT INTO survey_responses (id, student_id, survey_question_id, likert_value, free_text, submitted_at) VALUES
-- Biology survey - LIKERT (survey_question_id=1)
(1,  1,  1, 4, NULL, '2026-03-15 19:29:00+00'),
(2,  2,  1, 3, NULL, '2026-03-15 20:14:00+00'),
(3,  3,  1, 4, NULL, '2026-03-15 18:44:00+00'),
(4,  4,  1, 2, NULL, '2026-03-15 20:59:00+00'),
(5,  5,  1, 3, NULL, '2026-03-15 22:09:00+00'),
(6,  6,  1, 2, NULL, '2026-03-15 19:54:00+00'),
(7,  7,  1, 1, NULL, '2026-03-15 20:39:00+00'),
(8,  9,  1, 3, NULL, '2026-03-15 23:04:00+00'),
(9,  10, 1, 4, NULL, '2026-03-15 21:29:00+00'),
-- Biology survey - FREE_TEXT (survey_question_id=2)
(10, 2,  2, NULL, 'I''m still confused about how ATP synthase works exactly.',                   '2026-03-15 20:14:00+00'),
(11, 4,  2, NULL, 'The difference between glycolysis and the Krebs cycle is unclear to me.',    '2026-03-15 20:59:00+00'),
(12, 6,  2, NULL, 'Not sure when the cell uses aerobic vs anaerobic respiration.',              '2026-03-15 19:54:00+00'),
(13, 7,  2, NULL, 'Everything about the electron transport chain is confusing.',                '2026-03-15 20:39:00+00'),
(14, 9,  2, NULL, 'I don''t understand why glucose breaks down in steps instead of all at once.', '2026-03-15 23:04:00+00'),
-- EnvSci survey - LIKERT (survey_question_id=3)
(15, 9,  3, 5, NULL, '2026-03-15 19:59:00+00'),
(16, 10, 3, 2, NULL, '2026-03-15 20:59:00+00'),
-- EnvSci survey - FREE_TEXT (survey_question_id=4)
(17, 10, 4, NULL, 'I''m not sure how the Coriolis effect works.', '2026-03-15 20:59:00+00');

-- reset sequences
SELECT setval('courses_id_seq',          (SELECT MAX(id) FROM courses));
SELECT setval('sections_id_seq',         (SELECT MAX(id) FROM sections));
SELECT setval('lessons_id_seq',          (SELECT MAX(id) FROM lessons));
SELECT setval('lesson_items_id_seq',     (SELECT MAX(id) FROM lesson_items));
SELECT setval('quiz_questions_id_seq',   (SELECT MAX(id) FROM quiz_questions));
SELECT setval('quiz_choices_id_seq',     (SELECT MAX(id) FROM quiz_choices));
SELECT setval('survey_questions_id_seq', (SELECT MAX(id) FROM survey_questions));
SELECT setval('students_id_seq',         (SELECT MAX(id) FROM students));
SELECT setval('lesson_events_id_seq',    (SELECT MAX(id) FROM lesson_events));
SELECT setval('video_watch_events_id_seq', (SELECT MAX(id) FROM video_watch_events));
SELECT setval('quiz_responses_id_seq',   (SELECT MAX(id) FROM quiz_responses));
SELECT setval('survey_responses_id_seq', (SELECT MAX(id) FROM survey_responses));
