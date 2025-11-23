# Reason for Choosing MongoDB for the Exam Practice Web App

### 1. **Natural Fit for JSON-Based Exam Data**

The application stores exams as structured JSON documents containing metadata, questions, and nested options. MongoDB’s document model matches this format directly, allowing each exam to be stored as a single document without restructuring or complex table design.

### 2. **Efficient Full-Document Retrieval**

The system loads entire exams at once rather than querying individual fields. MongoDB is optimized for document-level reads, making exam retrieval extremely fast and straightforward.

### 3. **Minimal Need for Relational Updates**

Past exam papers are static and do not require ongoing updates. MongoDB performs best with read-heavy, write-light workloads, making it ideal for immutable exam content.

### 4. **Simple Handling of User Attempts and Scores**

Although the system tracks user attempts, scores, and selected answers, these can be stored as separate documents referencing exam IDs. MongoDB handles these non-relational references efficiently without the need for complex SQL joins.

### 5. **Scalable for Future Growth**

With 100+ exams planned and potentially large user traffic, MongoDB provides horizontal scalability and flexible indexing, ensuring the system can grow without schema limitations.

### 6. **Smooth Deployment on AWS**

MongoDB Atlas runs natively on AWS and provides a fully managed, production-ready environment with monitoring, backups, scaling, and secure access—reducing operational overhead.