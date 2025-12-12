# FastPrep - AWS Certification Practice Exam Platform

A comprehensive web-based practice exam platform designed to help users prepare for AWS certifications with realistic exam simulations and AI-powered feedback.

## 🎯 Project Overview

FastPrep provides an authentic AWS certification exam experience with:
- **Realistic Exam Simulation**: Timed exams with security constraints mimicking real certification environments
- **Comprehensive Question Bank**: 23+ AWS Cloud Practitioner practice exams with structured difficulty levels
- **AI-Powered Feedback**: Integration with Gemini AI for personalized performance analysis
- **Answer Review System**: Optional answer display with explanations and visual feedback
- **Exam Integrity Features**: Tab switching warnings, copy prevention, and strict timing controls

## 🛠️ Tech Stack

### Backend
- **Java 17** with **Spring Boot 3.x**
- **Spring Web MVC** for REST API and web controllers
- **MongoDB** for document-based question storage
- **Thymeleaf** for server-side templating
- **Maven** for dependency management

### Frontend
- **HTML5/CSS3** with responsive design
- **Vanilla JavaScript** for interactive exam functionality
- **Bootstrap-inspired** custom styling
- **Progressive Web App** features

### Database
- **MongoDB** for flexible JSON document storage
- **Spring Data MongoDB** for repository layer
- **Custom enum mapping** for exam difficulty levels

## 🏗️ Architecture & Design Decisions

### MVC Architecture
```
├── controller/     # REST endpoints and web controllers
├── service/       # Business logic and grading algorithms
├── repository/    # Data access layer with MongoDB integration
├── model/         # Domain entities and enums
└── util/          # Utility classes and helpers
```

**Design Rationale**: Clean separation of concerns with proper package structure over mixed responsibilities. Each layer has a single responsibility, making the codebase maintainable and testable.

### Database Schema Design
- **Document-Based Storage**: Questions stored as JSON documents in MongoDB for flexibility
- **ExamLevel Enum**: Replaced numeric difficulty with semantic levels (FOUNDATIONAL, ASSOCIATE, PROFESSIONAL, SPECIALTY)
- **Backward Compatibility**: Maintained support for legacy numeric difficulty values

### Security & Exam Integrity
- **Client-Side Constraints**: Text selection prevention, right-click disabling, tab switching detection
- **Timer Enforcement**: Strict 180-minute countdown with automatic submission
- **Answer Protection**: Initially explored Base64 encoding but prioritized functionality over security for practice context

### User Experience Design
- **Progressive Disclosure**: Questions revealed one at a time to simulate real exam flow
- **Visual Feedback**: Color-coded answer highlighting (green for correct, red for incorrect)
- **Comprehensive Instructions**: Official-style exam rules and integrity pledge
- **Responsive Design**: Works across desktop and mobile devices

## 📁 Project Structure

```
FastPrep/
├── backend/
│   ├── src/main/java/com/fastprep/site/
│   │   ├── controller/ExamController.java
│   │   ├── service/GradingService.java
│   │   ├── repository/ExamRepository.java
│   │   ├── model/ExamLevel.java
│   │   └── util/
│   ├── src/main/resources/
│   │   ├── templates/
│   │   │   ├── exam_instructions.html
│   │   │   └── exam_page.html
│   │   └── static/css/
│   └── pom.xml
├── data_scraping/
│   └── exams/AWS Certified Cloud Practitioner/
│       └── *.json (23 practice exams)
└── README.md
```

## 🚀 Key Features

### Exam Management
- **Dynamic Question Loading**: Questions fetched from MongoDB with proper error handling
- **Multiple Question Types**: Support for both single-choice and multiple-choice questions
- **Progress Tracking**: Real-time question counter and completion status

### Answer Tracking & AI Integration
- **Comprehensive Data Collection**: Captures user responses, timing, and performance metrics
- **JSON Compilation**: Structured data format ready for Gemini AI processing
- **Microservices Ready**: Single-request design to minimize API chattiness

### Grading System
- **Flexible Scoring**: Handles both single and multiple-choice question types
- **Service Layer**: Extracted from static utilities for better testability and dependency injection

## 🧪 Testing Strategy

- **Database Integration Tests**: Comprehensive MongoDB connection and retrieval testing
- **Service Layer Tests**: Business logic validation with mock data
- **Controller Tests**: HTTP endpoint testing with proper error handling
- **Error Handling**: Graceful degradation when database is unavailable

## 🔮 Future Enhancements

- **AI Feedback Integration**: Complete Gemini AI integration for personalized study recommendations
- **Multi-Certification Support**: Expand beyond Cloud Practitioner to other AWS certifications
- **Performance Analytics**: Detailed progress tracking and weak area identification
- **Social Features**: Study groups and competitive leaderboards

## 🚦 Getting Started

1. **Prerequisites**: Java 17+, MongoDB, Maven
2. **Database Setup**: Import JSON exam files to MongoDB
3. **Configuration**: Update MongoDB connection settings
4. **Build**: `mvn clean install`
5. **Run**: `mvn spring-boot:run`
6. **Access**: Navigate to `http://localhost:8080`

## 📊 Design Philosophy

- **Functionality Over Security**: Prioritized user experience for practice exams over answer protection
- **Clean Architecture**: Emphasized proper MVC separation and single responsibility principle
- **Scalability Consideration**: Designed with microservices architecture in mind
- **Realistic Simulation**: Focused on authentic exam experience with proper constraints
- **AI-First Approach**: Built data collection system specifically for AI-powered insights

---

*FastPrep is designed to provide the most realistic and effective AWS certification practice experience possible, combining traditional exam simulation with modern AI-powered learning insights.*