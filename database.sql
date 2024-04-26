CREATE DATABASE codewalker;
USE codewalker;

CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_code VARCHAR(20) NOT NULL DEFAULT '',
    student_name VARCHAR(100) NOT NULL,
    student_class VARCHAR(20) DEFAULT ''
);
CREATE TABLE subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL
);
CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    subject_id INT,
    score_text VARCHAR(100) DEFAULT '',
    score_first FLOAT NOT NULL CHECK(score_first>=0),
    score_second FLOAT NOT NULL CHECK(score_second>=0),
    score_final FLOAT NOT NULL CHECK(score_final>=0),
    score_over_rall FLOAT NOT NULL CHECK(score_over_rall>=0),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);
