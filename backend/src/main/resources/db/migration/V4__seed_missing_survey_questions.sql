-- Add survey questions to the 8 survey items that were left empty in V3
INSERT INTO survey_questions (lesson_item_id, question_text, type, position) VALUES
-- Lesson 1: Cell structure
(4,  'I am confident in my understanding of cell organelles and their functions.',  'LIKERT',    1),
(4,  'What aspect of cell structure is still unclear to you?',                      'FREE_TEXT', 2),
-- Lesson 2: DNA & genetics
(8,  'I am confident in my understanding of DNA structure and inheritance.',        'LIKERT',    1),
(8,  'What questions do you still have about genetics or replication?',             'FREE_TEXT', 2),
-- Lesson 4: Biomes & ecosystems
(16, 'I am confident in my understanding of biomes and food webs.',                 'LIKERT',    1),
(16, 'What aspect of ecosystem dynamics is still unclear to you?',                  'FREE_TEXT', 2),
-- Lesson 5: Evolution
(20, 'I am confident in my understanding of evolution and natural selection.',      'LIKERT',    1),
(20, 'What questions do you still have about speciation or genetic drift?',         'FREE_TEXT', 2),
-- Lesson 6: Earth systems
(24, 'I am confident in my understanding of Earth''s interacting systems.',         'LIKERT',    1),
(24, 'What aspect of Earth systems is still unclear to you?',                       'FREE_TEXT', 2),
-- Lesson 8: Water cycle
(32, 'I am confident in my understanding of the water cycle.',                      'LIKERT',    1),
(32, 'What questions do you have about freshwater resources or precipitation?',     'FREE_TEXT', 2),
-- Lesson 9: Habitats & biodiversity
(36, 'I am confident in my understanding of habitats and biodiversity.',            'LIKERT',    1),
(36, 'What aspect of biodiversity or habitat loss is still unclear to you?',        'FREE_TEXT', 2),
-- Lesson 10: Human impact
(40, 'I am confident in my understanding of human impacts on ecosystems.',          'LIKERT',    1),
(40, 'What questions do you have about sustainability or environmental change?',    'FREE_TEXT', 2);

SELECT setval('survey_questions_id_seq', (SELECT MAX(id) FROM survey_questions));
