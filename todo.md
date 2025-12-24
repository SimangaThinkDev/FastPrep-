# Fastprep Backlog

## Timeline ( Fri Nov 21 y25 - present )

### Exam Details Page Adjustments

1. Exam level separation
    - [ ] The exams should simulate AWS's official exam pathway
    - [ ] separated to show the user where to start and where to end of

2. Exam description ( Backlogged )
    - [ ] Every exam should have its own definite description
    - [ ] The average timing should also be researched for this


### Exam Page Adjustments

1. Before the user takes the exam, Show them the rules as an official assignment would
    - [ ] The time limit
    - [ ] assurance of test environment, i.e, write on a laptop, make sure internet is stable
    - [ ] pledge from user to not use any AI throughout the exam, but this should be more focused on the user's honesty to themselves. If they are cheating in practice, then what is their plan for the actual exam?
    - [ ] ... and many more you can find

2. Constraints on the exam page
   - [ ] Make sure the user cannot copy text on the page they are currently working on
   - [ ] The user should be given warnings for visiting other tabs
   - [ ] Strict exam timing should be implemented. i.e, 180 minutes is 180 minutes


### Migration to a database

1. Initial Part
    - [ ] Choose a database engine that fits the application's use case -- Document the research behind this
    - [ ] Design A schema specific to the engine
    - [ ] Choose between an ORM system to use, hopefully not lemnik again :(

2. Tests
    - [ ] Write connection tests
    - [ ] Write retrieval tests

3. Application
    - [ ] Define data classes and start populating the database
    - [ ] test if getters are working as expected
    - [ ] fix bugs if any and continue testing

4. Integration
    - [ ] Write integration tests
    - [ ] start integrating the apps functionality with the database logic
    - [ ] Test, Refactor, Improve (Not Done)

### Enhance user feedback

1. Collect User performance on exams
    - [ ] Collect how a user performed for a specific practice exam
    - [ ] Store data to use in analytics

2. Provide User Feedback
    - [ ] Leave the after exam general feedback as is but add a message to hint the user at checking their emails.
    - [ ] Use gemini to generate a constructive feedback for the user
    - [ ] Design the email response
    - [ ] Use Amazon SNS to send the feedback notification
    - [ ] Attach a Google form to collect reverse feedback...

## AI-Powered Features & User Management

#### US-001: Gemini Integration for Performance Feedback
**As a** student preparing for AWS certification  
**I want** to receive AI-powered feedback on my exam performance  
**So that** I can understand my strengths and weaknesses and get personalized study recommendations

**Acceptance Criteria:**
- [ ] System sends exam results to Gemini AI after completion
- [ ] AI analyzes performance patterns and knowledge gaps
- [ ] User receives detailed feedback with study recommendations
- [ ] Feedback includes specific topic areas to focus on
- [ ] Integration handles API errors gracefully

#### US-002: User Authentication with Amazon Cognito
**As a** platform user  
**I want** to securely sign up and log in to my account  
**So that** I can track my progress and access personalized features

**Acceptance Criteria:**
- [ ] Users can register with email/password via Cognito
- [ ] Users can log in with existing credentials
- [ ] Session management with secure token handling
- [ ] Password reset functionality
- [ ] Proper error handling for authentication failures

#### US-003: User Dashboard with Analytics
**As a** registered user  
**I want** to view my exam history and performance analytics  
**So that** I can track my progress and identify areas for improvement

**Acceptance Criteria:**
- [ ] Dashboard shows exam completion history
- [ ] Mock analytics display score trends over time
- [ ] Visual charts for performance by topic/domain
- [ ] Progress indicators toward certification readiness
- [ ] Quick access to retake exams or start new ones
- [ ] Responsive design for mobile and desktop

#### US-004: Gemini Feedback History & Logging
**As a** user who has taken multiple exams  
**I want** to access my previous AI feedback reports  
**So that** I can review detailed performance insights and track my improvement over time

**Acceptance Criteria:**
- [ ] System stores all Gemini feedback responses
- [ ] Users can view chronological list of feedback reports
- [ ] Each report shows exam date, score, and AI insights
- [ ] Search/filter functionality for feedback history
- [ ] Export functionality for personal study notes
- [ ] Feedback comparison between different exam attempts

## Add a new timeline here