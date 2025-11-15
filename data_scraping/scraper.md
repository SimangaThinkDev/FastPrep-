# Markdown Exam Parser to JSON Converter

This Python script is designed to process specially formatted Markdown (.md) files that contain multiple-choice quiz or exam data and convert them into a structured JSON format. The primary goal is to prepare quiz content for easy ingestion into a database or application back-end.

### What It Does

- The script reads a single Markdown file and performs the following core functions:

- **Metadata Extraction**: Parses optional YAML front matter (if present) and the main document title.

- **Question Segmentation**: Identifies and isolates individual questions based on numerical formatting (e.g., 1., 2.).

- **Option and Text Extraction**: Accurately extracts the main question text and all associated multiple-choice options (A., B., C., etc.).

 - **Answer Key Extraction (Robust)**: Scans the hidden `<details>` block to find the Correct Answer: key and safely extracts the key(s) (e.g., D, A,C), cleaning up any surrounding whitespace or formatting issues.

- **Structure Generation**: Assembles all components into a clean, hierarchical JSON object, ready for bulk uploading or programmatic use.

### Key Features

- **Robust Parsing**: Handles variations in spacing and newlines within the answer key section to ensure accurate extraction.

- **Multi-Select Support**: Correctly identifies and formats multiple correct keys (e.g., A,B).

- **Database Ready**: Creates a structured output ideal for storage in NoSQL databases like Firestore.

