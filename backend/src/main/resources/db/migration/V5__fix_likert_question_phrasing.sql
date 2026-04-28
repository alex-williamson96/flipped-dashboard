-- Rephrase Likert questions as statements so they work on an agree/disagree scale
UPDATE survey_questions SET question_text = 'I am confident in my understanding of cellular energy processes.'
  WHERE question_text = 'How confident are you in your understanding of cellular energy processes?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of climate systems.'
  WHERE question_text = 'How confident are you in your understanding of climate systems?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of cell organelles and their functions.'
  WHERE question_text = 'How confident are you in your understanding of cell organelles?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of DNA structure and inheritance.'
  WHERE question_text = 'How confident are you in your understanding of DNA and inheritance?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of biomes and food webs.'
  WHERE question_text = 'How confident are you in your understanding of biomes and food webs?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of evolution and natural selection.'
  WHERE question_text = 'How confident are you in your understanding of evolution and natural selection?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of Earth''s interacting systems.'
  WHERE question_text = 'How confident are you in your understanding of Earth''s interacting systems?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of the water cycle.'
  WHERE question_text = 'How confident are you in your understanding of the water cycle?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of habitats and biodiversity.'
  WHERE question_text = 'How confident are you in your understanding of habitats and biodiversity?';

UPDATE survey_questions SET question_text = 'I am confident in my understanding of human impacts on ecosystems.'
  WHERE question_text = 'How confident are you in your understanding of human impacts on ecosystems?';
