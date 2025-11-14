-- SQL Schema for Practice Exam Questions

---
--- 1. Exams Table
--- Stores metadata about the exam.
---

CREATE TABLE Exams (
    exam_id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    layout VARCHAR(50)
);

---
--- 2. Questions Table
--- Stores the question text, the correct answer key(s), and the requested correct answer text.
---

CREATE TABLE Questions (
    question_id INT PRIMARY KEY,
    exam_id INT NOT NULL,
    question_number INT NOT NULL,
    question_text TEXT NOT NULL,
    correct_answer_keys VARCHAR(10) NOT NULL,
    correct_answer_text TEXT, -- Stores the full text of the correct option(s) (redundancy requested)
    is_multiple_choice BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (exam_id) REFERENCES Exams(exam_id)
);

---
--- 3. Options Table
--- Stores each individual answer choice (A, B, C, D, E) for a question.
--- Uses a composite primary key (question_id, option_key).
---

CREATE TABLE Options (
    question_id INT NOT NULL,
    option_key CHAR(1) NOT NULL, -- e.g., 'A', 'B', 'C'
    option_text TEXT NOT NULL,
    PRIMARY KEY (question_id, option_key),
    FOREIGN KEY (question_id) REFERENCES Questions(question_id)
);