-- Add STARTED events for students who completed lesson 3 (Biology: Photosynthesis).
-- The original seed only included COMPLETED events; STARTED events are needed for the
-- engagement funnel to show a realistic Enrolled → Started → Completed → Surveyed progression.

INSERT INTO lesson_events (student_id, lesson_id, event_type, occurred_at) VALUES
(1, 3, 'STARTED', '2026-03-15 19:15:00+00'),
(2, 3, 'STARTED', '2026-03-15 20:00:00+00'),
(3, 3, 'STARTED', '2026-03-15 18:30:00+00'),
(4, 3, 'STARTED', '2026-03-15 20:45:00+00'),
(5, 3, 'STARTED', '2026-03-15 22:00:00+00'),
(6, 3, 'STARTED', '2026-03-15 19:40:00+00'),
(7, 3, 'STARTED', '2026-03-15 20:25:00+00'),
(8, 3, 'STARTED', '2026-03-15 23:00:00+00');
