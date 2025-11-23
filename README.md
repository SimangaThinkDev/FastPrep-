# FastPrep-

---
## MongoDB Information

### Schema

```json
{
  "exam_metadata": {
    "exam_id": 1,
    "title": "Practice Exam 1",
    "level": "AWS Cloud Practitioner",
    "difficulty": "Foundational",
    "description": "Introduction exam"
  },
  "questions": [
    {
      "question_number": 1,
      "question_text": "Question text...",
      "is_multiple_choice": false,
      "correct_answer_keys": "A",
      "correct_answer_text": "Correct answer explanation...",
      "options": [
        { "option_key": "A", "option_text": "..." },
        { "option_key": "B", "option_text": "..." },
        { "option_key": "C", "option_text": "..." },
        { "option_key": "D", "option_text": "..." }
      ]
    }
  ]
}
```

### Difficulty Table

| AWS Exam                         | Difficulty |
| -------------------------------- | ---------- |
| Cloud Practitioner               | **1**      |
| Solutions Architect Associate    | **2**      |
| Developer Associate              | **2**      |
| SysOps Administrator Associate   | **2**      |
| Solutions Architect Professional | **3**      |
| DevOps Engineer Professional     | **3**      |
| Security Specialty               | **4**      |
| Database Specialty               | **4**      |
| Machine Learning Specialty       | **4**      |
| Networking Specialty             | **4**      |
| SAP on AWS Specialty             | **4**      |

---