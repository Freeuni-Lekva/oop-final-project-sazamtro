USE sazamtro;


DROP TABLE IF EXISTS Announcements;
DROP TABLE IF EXISTS UserAnswers;
DROP TABLE IF EXISTS AnswerOptions;
DROP TABLE IF EXISTS CorrectAnswers;
DROP TABLE IF EXISTS Questions;
DROP TABLE IF EXISTS QuizAttempts;
DROP TABLE IF EXISTS Messages;
DROP TABLE IF EXISTS FriendRequests;
DROP TABLE IF EXISTS UserAchievements;
DROP TABLE IF EXISTS Achievements;
DROP TABLE IF EXISTS Quizzes;
DROP TABLE IF EXISTS Users;



CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    profilePicture_url VARCHAR(255),
    is_admin BOOLEAN DEFAULT FALSE
);


CREATE TABLE FriendRequests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    from_user_id INT NOT NULL,
    to_user_id INT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',

    FOREIGN KEY (from_user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (to_user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);


CREATE TABLE Quizzes (
    quiz_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    creator_id INT NOT NULL,
    is_random BOOLEAN DEFAULT FALSE,
    is_multipage BOOLEAN DEFAULT FALSE,
    immediate_correction BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (creator_id) REFERENCES Users(user_id) ON DELETE CASCADE
);


CREATE TABLE Questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    type ENUM('QUESTION_RESPONSE', 'FILL_IN_THE_BLANK', 'MULTIPLE_CHOICE', 'MULTI_SELECT', 'PICTURE_RESPONSE') NOT NULL,
    prompt TEXT NOT NULL,
    image_url TEXT,
    position INT,
    is_active BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (quiz_id) REFERENCES Quizzes (quiz_id) ON DELETE CASCADE
);


CREATE TABLE AnswerOptions (
    option_id INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT NOT NULL,
    text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);


CREATE TABLE CorrectAnswers (
    answer_id INT PRIMARY KEY AUTO_INCREMENT,
    question_id INT NOT NULL,
    text TEXT NOT NULL,

    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);


CREATE TABLE QuizAttempts (
    attempt_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    quiz_id INT NOT NULL,
    score INT,
    time_taken_min DOUBLE,
    is_practice BOOLEAN DEFAULT FALSE,
    taken_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);


CREATE TABLE UserAnswers (
    answer_id INT PRIMARY KEY AUTO_INCREMENT,
    attempt_id INT NOT NULL,
    question_id INT NOT NULL,
    response_text TEXT,
    is_correct BOOLEAN,

    FOREIGN KEY (attempt_id) REFERENCES QuizAttempts(attempt_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);


CREATE TABLE Messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    from_user_id INT NOT NULL,
    to_user_id INT NOT NULL,
    type ENUM('NOTE', 'FRIEND_REQUEST', 'CHALLENGE') NOT NULL,
    content TEXT,
    quiz_id INT,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (from_user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (to_user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);


CREATE TABLE Achievements (
    achievement_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    icon_url TEXT
);

INSERT INTO Achievements (name, description, icon_url) VALUES
-- Amateur Author
('Amateur Author',
 'You’ve crafted your very first quiz. Every legend starts with one.',
 'https://cdn-icons-png.flaticon.com/512/7601/7601260.png'),

-- Quiz Machine
('Quiz Machine',
 'You’ve taken 10 quizzes! At this point, you basically *live* here.',
 'https://cdn-icons-png.flaticon.com/512/3261/3261154.png'),

-- Practice Makes Perfect
('Practice Makes Perfect',
 'Tried a quiz in practice mode. A disciplined learner always sharpens the blade.',
 'https://cdn-icons-png.flaticon.com/512/6747/6747027.png'),

-- Perfectionist
('Perfectionist',
 'You scored perfectly on a quiz. Precision. Focus. Victory.',
 'https://cdn-icons-png.flaticon.com/512/5377/5377848.png'),

-- Social Butterfly
('Social Butterfly',
 'You made 5 friends! Your quiz circle just got a whole lot warmer.',
 'https://cdn-icons-png.flaticon.com/512/4713/4713808.png'),

-- Night Owl
('Night Owl',
 'Took a quiz between 2–5 AM. Who needs sleep when there’s learning to do?',
 'https://cdn-icons-png.flaticon.com/512/3643/3643401.png');



CREATE TABLE UserAchievements (
    user_id INT NOT NULL,
    achievement_id INT NOT NULL,
    awarded_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, achievement_id),

    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (achievement_id) REFERENCES Achievements(achievement_id) ON DELETE CASCADE
);


CREATE TABLE Announcements (
    announcement_id INT PRIMARY KEY AUTO_INCREMENT,
    administrator_id INT NOT NULL,
    announcement_text TEXT NOT NULL,
    done_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (administrator_id) REFERENCES Users(user_id) ON DELETE CASCADE
);







