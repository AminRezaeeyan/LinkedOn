-- databaseSetUp
-- Version: 1.0.0

-- Create the database
CREATE DATABASE IF NOT EXISTS LinkedOn_DB;
USE LinkedOn_DB;

-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    bio VARCHAR(225),
    additional_name VARCHAR(40),
    headline VARCHAR(220),
    location VARCHAR(120),
    user_state ENUM('OPEN_TO_WORK', 'OPEN_TO_HIRING', 'OPEN_TO_PROVIDING_SERVICES', 'NONE') DEFAULT 'NONE',
    industry VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (email) -- Ensure email uniqueness
)

CREATE TABLE IF NOT EXISTS contact_infos
(
    user_id BINARY(16) NOT NULL,
    phone_number VARCHAR(15),
    phone_type ENUM('HOME','WORK','MOBILE','NONE') DEFAULT 'NONE',
    address VARCHAR(220),
    website VARCHAR(50),
    birthday DATE,
    instant_message VARCHAR(40),
    instant_message_type VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS educations(
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    school VARCHAR(100),
    degree VARCHAR(25),
    field  VARCHAR(40),
    grade VARCHAR(10),
    start_date DATE,
    end_date DATE,
    description VARCHAR(225),
    activities VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS skills(
     skill_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
     skill_name VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS follows
(
    follower_id BINARY(16) NOT NULL,
    followed_id BINARY(16) NOT NULL,
    FOREIGN KEY (follower_id) REFERENCES users (id),
    FOREIGN KEY (followed_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS connects(
    user_id_1 BINARY(16) NOT NULL,
    user_id_2 BINARY(16) NOT NULL,
    status ENUM('CONNECTED','PENDING') DEFAULT 'PENDING' NOT NULL,
    FOREIGN KEY (user_id_1) REFERENCES users (id)
    ON DELETE CASCADE,
    FOREIGN KEY (user_id_2) REFERENCES users (id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts(
    id BINARY(16),
    user_id BINARY(16) NOT NULL,
    text VARCHAR(255),
    likes INT DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes(
    user_id BINARY(16),
    post_id BINARY(16),
    FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts (id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages(
    sender_id BINARY(16),
    receiver_id BINARY(16),
    text VARCHAR(255),
    FOREIGN KEY (sender_id) REFERENCES users (id)
    ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id)
    ON DELETE CASCADE
);
-- Seed Data
INSERT INTO skills (skill_name)
VALUES ('Java'),
       ('Python'),
       ('JavaScript'),
       ('SQL'),
       ('Microsoft Excel'),
       ('Project management'),
       ('Leadership'),
       ('Data analysis'),
       ('Microsoft Office'),
       ('Communication'),
       ('HTML'),
       ('CSS'),
       ('C++'),
       ('Customer service'),
       ('Time management'),
       ('Problem-solving'),
       ('Sales'),
       ('Marketing'),
       ('Social media'),
       ('Teamwork'),
       ('Research'),
       ('Financial analysis'),
       ('Business development'),
       ('Strategic planning'),
       ('Cloud computing'),
       ('Software development'),
       ('Cybersecurity'),
       ('Database management'),
       ('Conflict resolution'),
       ('Decision-making'),
       ('Public speaking'),
       ('Negotiation'),
       ('Machine learning'),
       ('Artificial intelligence'),
       ('Product management'),
       ('Supply chain management'),
       ('Human resources'),
       ('Graphic design'),
       ('Web development'),
       ('Content writing'),
       ('SEO (Search Engine Optimization)'),
       ('Digital marketing'),
       ('Event planning'),
       ('Leadership development'),
       ('Coaching'),
       ('Entrepreneurship'),
       ('Agile methodology'),
       ('Quality assurance'),
       ('Risk management'),
       ('Change management'),
       ('Networking'),
       ('Problem management'),
       ('Data visualization'),
       ('Statistical analysis'),
       ('Financial modeling'),
       ('Business intelligence'),
       ('Market research'),
       ('Brand management'),
       ('User experience (UX) design'),
       ('Front-end development'),
       ('Back-end development'),
       ('Mobile app development'),
       ('Database administration'),
       ('Systems administration'),
       ('DevOps'),
       ('Scrum'),
       ('Six Sigma'),
       ('Lean management'),
       ('Process improvement'),
       ('Data engineering'),
       ('Data science'),
       ('Blockchain'),
       ('Cryptocurrency'),
       ('Game development'),
       ('Video editing'),
       ('Photography'),
       ('Illustration'),
       ('Animation'),
       ('Copywriting'),
       ('Technical writing'),
       ('Legal research'),
       ('Intellectual property'),
       ('Patent law'),
       ('Environmental sustainability'),
       ('Renewable energy'),
       ('Supply chain logistics'),
       ('Retail management'),
       ('Real estate'),
       ('Interior design'),
       ('Event management'),
       ('Hospitality'),
       ('Culinary arts'),
       ('Fitness training'),
       ('Yoga instruction'),
       ('Personal branding'),
       ('Influencer marketing'),
       ('Nonprofit management'),
       ('Fundraising'),
       ('Teaching'),
       ('Tutoring');

-- Create Indexes For Better Performance (primary and foreign keys are indexed automatically
CREATE INDEX idx_email ON users (email);

-- Procedures For Cleaner Code In Java
CREATE PROCEDURE GetConnections(IN given_user_id BINARY(16))
BEGIN
SELECT
    u2.*
FROM connects c
         JOIN users u ON c.user_id_1 = u.id OR c.user_id_2 = u.id
         JOIN users u2 ON (c.user_id_1 = u2.id OR c.user_id_2 = u2.id) AND u2.id <> given_user_id
WHERE c.status = 'CONNECTED' AND u.id = given_user_id;
END

CREATE PROCEDURE GetConnectionRequests(IN given_user_id BINARY(16))
BEGIN
SELECT
    u2.*
FROM connects c
         JOIN users u ON c.user_id_2 = u.id
         JOIN users u2 ON c.user_id_1 = u2.id
WHERE c.status = 'PENDING' AND u.id = given_user_id;
END

CREATE PROCEDURE GetUserFeed(IN userId BINARY(16)) -- Get posts of user's connection's post and his own posts as feed
BEGIN
SELECT p.id, p.user_id, p.text, p.likes,
       EXISTS(SELECT 1 FROM likes l WHERE l.user_id = userId AND l.post_id = p.id) AS hasLiked
FROM posts p
         JOIN connects c ON (c.user_id_1 = userId AND c.user_id_2 = p.user_id AND c.status = 'CONNECTED')
    OR (c.user_id_2 = userId AND c.user_id_1 = p.user_id AND c.status = 'CONNECTED')
    LIMIT 10;
END;

-- Triggers
CREATE TRIGGER after_user_insert -- Create new contact info when a new user signs up
    AFTER INSERT ON users
    FOR EACH ROW
BEGIN
    INSERT INTO contact_infos (user_id)
    VALUES (NEW.id);
END;

CREATE TRIGGER increment_likes -- Increment the number of likes of a post when a user likes it
    AFTER INSERT ON likes
    FOR EACH ROW
BEGIN
    UPDATE posts
    SET likes = likes + 1
    WHERE id = NEW.post_id;
END;
