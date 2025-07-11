use sazamtro;
-- USERS
INSERT INTO Users (username, password_hash, profilePicture_url, is_admin) VALUES
            ('gvantsa', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', 'https://i.pinimg.com/736x/e7/03/f0/e703f0d151e171a848c0fe060808cbaf.jpg', TRUE),
            ('irakli', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', 'https://i.pinimg.com/736x/6c/b8/9a/6cb89a09c0969ca5d10a08516357c8ee.jpg?fbclid=IwY2xjawLfDB5leHRuA2FlbQIxMABicmlkETFaUm56cGtSZmJkM0xUV0VWAR6DHr-Sg0i3Xfk9QixVIGlKtgGg7fx6HUZVB81nyXF1nTe_-Ty5-x70AassgA_aem_UmD6XE82m67nCExnwBizSw', TRUE),
            ('tsotne', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', 'https://i.pinimg.com/736x/36/14/12/3614127a1cf010af2a5d20ad0549a29f.jpg?fbclid=IwY2xjawLfDVNleHRuA2FlbQIxMABicmlkETFubW1WODlKbGhtUXczbWx0AR4wPQo3eG2CO-tyP098ifFzuqLlz4WB7awIyFF7c1qhAAOTMeMNwHJC7QP9Ag_aem_VVFGsX2_jskA47fm4PeI6g', TRUE),
            ('taso', '5eec30a512219bbd18e44a4672e02123d9f0a6fb', 'https://cdn-icons-png.flaticon.com/512/8390/8390026.png', TRUE),
            ('ketevan','5eec30a512219bbd18e44a4672e02123d9f0a6fb', NULL, FALSE),
            ('avtandil','5eec30a512219bbd18e44a4672e02123d9f0a6fb', NULL, FALSE);


-- FRIEND REQUESTS
INSERT INTO FriendRequests (from_user_id, to_user_id, status) VALUES
            (1, 2, 'ACCEPTED'),
            (1, 5, 'ACCEPTED'),
            (1, 6,'ACCEPTED'),
            (2, 3, 'PENDING'),
            (3, 1, 'ACCEPTED'),
            (3, 6, 'REJECTED'),
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
INSERT INTO QuizAttempts (user_id, quiz_id, score, time_taken_min, is_practice) VALUES
            (1, 1, 2, 1.5, FALSE),
            (2, 1, 1, 2.0, TRUE),
            (3, 2, 1, 3.5, FALSE);

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
            (6, 1),
            (4, 3);

-- ANNOUNCEMENTS
INSERT INTO Announcements (administrator_id, announcement_text) VALUES
            (2, 'Don’t forget to log your practice attempts'),
            (4, 'New quizzes coming this weekend!'),
            (4, 'Site maintenance scheduled for Friday midnight.');
