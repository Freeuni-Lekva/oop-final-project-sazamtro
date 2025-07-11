-- USERS
INSERT INTO Users (username, password_hash, profilePicture_url, is_admin) VALUES
            ('gvantsa', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', 'https://example.com/pic2.png', TRUE),
            ('itakli', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', NULL, FALSE),
            ('tsotne', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', NULL, FALSE),
            ('taso', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', 'https://cdn-icons-png.flaticon.com/512/8390/8390026.png', TRUE),
            ('nerse','5eec30a512219bbd18e44a4672e02123d9f0a6fb', NULL, FALSE),
            ('rio','5eec30a512219bbd18e44a4672e02123d9f0a6fb', NULL, FALSE);


-- FRIEND REQUESTS
INSERT INTO FriendRequests (from_user_id, to_user_id, status) VALUES
            (1, 2, 'ACCEPTED'),
            (1, 5, 'ACCEPTED'),
            (1, 6,'ACCEPTED'),
            (2, 3, 'PENDING'),
            (3, 1, 'ACCEPTED'),
            (3, 6, 'REJECTED'),
            (4, 5, 'ACCEPTED'),
            (4, 1, 'ACCEPTED'),
            (5, 4, 'PENDING'),
            (6, 4, 'ACCEPTED');

-- QUIZZES
INSERT INTO Quizzes (title, description, creator_id, is_random, is_multipage, immediate_correction) VALUES
            ('Math Basics', 'A quiz about basic math.', 1, FALSE, FALSE, TRUE),
            ('Geography Fun', 'Test your world knowledge!', 2, TRUE, TRUE, FALSE),
            ('Science Quiz', 'Random science questions.', 1, TRUE, FALSE, FALSE);

-- QUESTIONS
INSERT INTO Questions (quiz_id, type, prompt, image_url, position) VALUES
            (1, 'QUESTION_RESPONSE', 'What is 2 + 2?', NULL, 1),
            (1, 'MULTIPLE_CHOICE', 'Select the prime number:', NULL, 2),
            (2, 'QUESTION_RESPONSE', 'Capital of France?', NULL, 1),
            (3, 'PICTURE_RESPONSE', 'Name this constellation.', 'https://www.thoughtco.com/thmb/N0DqcVSob63BmrpXu_mDhKWsM3M=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/orion-constellation-944405126-b2b06de820fd4458af81e0f864dcd413.jpg', 1);

-- ANSWER OPTIONS (for question 2)
INSERT INTO AnswerOptions (question_id, text, is_correct) VALUES
            (2, '4', FALSE),
            (2, '5', TRUE),
            (2, '6', FALSE);

-- CORRECT ANSWERS
INSERT INTO CorrectAnswers (question_id, text) VALUES
            (1, '4'),
            (3, 'Paris');

-- QUIZ ATTEMPTS
INSERT INTO QuizAttempts (user_id, quiz_id, score, time_taken_min, is_practice, taken_at) VALUES
            (1, 1, 2, 1.5, FALSE,'2025-06-25 10:15:00'),
            (1, 1, 5, 4.0, FALSE, '2025-06-26 14:30:00'),
            (1, 2, 3, 2.3, TRUE, '2025-06-27 19:45:00'),
            (2, 1, 1, 2.0, TRUE, '2025-07-01 19:45:00'),
            (3, 2, 1, 3.5, FALSE, '2025-07-02 08:55:00');


-- USER ANSWERS
INSERT INTO UserAnswers (attempt_id, question_id, response_text, is_correct) VALUES
            (1, 1, '4', TRUE),
            (1, 2, '5', TRUE),
            (2, 1, '5', FALSE),
            (3, 3, 'Paris', TRUE);

-- MESSAGES
INSERT INTO Messages (from_user_id, to_user_id, type, content, quiz_id) VALUES
            (1, 2, 'NOTE', 'Good quiz!', 1),
            (2, 3, 'FRIEND_REQUEST', 'Let\'s be friends!', NULL),
            (4, 1, 'CHALLENGE', 'Try to beat my score!', 2);

-- USER ACHIEVEMENTS
INSERT INTO UserAchievements (user_id, achievement_id) VALUES
            (1, 1),
            (1, 2),
            (2, 3),
            (2, 6),
            (3, 4),
            (1, 5),
            (4, 1);

-- ANNOUNCEMENTS
INSERT INTO Announcements (administrator_id, announcement_text, done_at)
VALUES
    (1, 'Welcome to the new quiz season!', '2025-06-20 09:00:00'),
    (2, 'Server maintenance scheduled.', '2025-06-22 16:15:00'),
    (1, 'New quizzes added.', '2025-06-24 11:30:00');
