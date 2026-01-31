// Exam Page JavaScript
let showAnswers, questionsArray, examID, title;
let cards, nextBtn, submitBtn, currentQSpan;
let currentIndex = 0;
let userAnswersData = {};
let examResultJson = {};

// Exam Constraints Variables
let examStartTime = Date.now();
let examDuration = 180 * 60 * 1000;
let warningCount = 0;
let examTimer;

function initializeExam(showAnswersParam, questionsArrayParam, examIDParam, titleParam) {
    showAnswers = showAnswersParam;
    questionsArray = questionsArrayParam;
    examID = examIDParam;
    title = titleParam;
    
    cards = document.querySelectorAll('.question-card');
    nextBtn = document.getElementById('nextBtn');
    submitBtn = document.getElementById('submitBtn');
    currentQSpan = document.getElementById('currentQ');
    
    examResultJson = {
        exam_metadata: {
            exam_id: examID,
            title: title,
            completed_at: null,
            show_answers_mode: showAnswers
        },
        questions: [],
        user_performance: {
            total_questions: questionsArray.length,
            answered_questions: 0,
            unanswered_questions: 0
        }
    };
    
    showQuestion(0);
    startExamTimer();
    setupEventListeners();
}

function showQuestion(index) {
    cards.forEach((card, i) => {
        card.classList.toggle('active', i === index);
        const numberSpan = card.querySelector('.question-number-dynamic');
        if (numberSpan) {
            numberSpan.textContent = index + 1;
        }
    });

    const currentCard = cards[index];
    const inputs = currentCard.querySelectorAll('input');
    inputs.forEach(input => input.disabled = false);
    
    const options = currentCard.querySelectorAll('.option-item');
    options.forEach(option => {
        option.style.backgroundColor = '';
        option.style.borderColor = '';
    });
    
    const explanation = currentCard.querySelector('.answer-explanation');
    if (explanation) explanation.remove();

    currentQSpan.textContent = index + 1;

    if (index === cards.length - 1) {
        nextBtn.style.display = 'none';
        submitBtn.style.display = 'inline-block';
    } else {
        nextBtn.style.display = 'inline-block';
        submitBtn.style.display = 'none';
    }
}

function nextQuestion() {
    const currentCard = cards[currentIndex];
    const inputs = currentCard.querySelectorAll('input[type="radio"], input[type="checkbox"]');
    const anyChecked = Array.from(inputs).some(inp => inp.checked);

    if (!anyChecked) {
        alert("Please select at least one option before proceeding.");
        return;
    }
    
    captureUserAnswer(currentIndex);

    if (showAnswers === true) {
        showAnswerForCurrentQuestion();
        return;
    }

    if (currentIndex < cards.length - 1) {
        currentIndex++;
        showQuestion(currentIndex);
    }
}

function captureUserAnswer(questionIndex) {
    const currentCard = cards[questionIndex];
    const inputs = currentCard.querySelectorAll('input[type="radio"], input[type="checkbox"]');
    const selectedAnswers = [];
    
    inputs.forEach(input => {
        if (input.checked) {
            selectedAnswers.push(input.value);
        }
    });
    
    userAnswersData[questionIndex] = selectedAnswers;
    
    const questionData = questionsArray[questionIndex];
    const questionNumber = questionData.question_number;
    const isMultipleChoice = questionData.is_multiple_choice;
    
    const existingInputs = document.querySelectorAll(`input[name^="question_${questionNumber}"]`);
    existingInputs.forEach(input => {
        if (input.type === 'hidden') {
            input.remove();
        }
    });
    
    const form = document.getElementById('examForm');
    selectedAnswers.forEach(answer => {
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = isMultipleChoice ? `question_${questionNumber}[]` : `question_${questionNumber}`;
        hiddenInput.value = answer;
        form.appendChild(hiddenInput);
    });
}

function showAnswerForCurrentQuestion() {
    const currentCard = cards[currentIndex];
    const correctAnswerKeys = currentCard.dataset.correctAnswer;
    const correctAnswerText = currentCard.dataset.correctAnswerText;
    
    const inputs = currentCard.querySelectorAll('input');
    inputs.forEach(input => input.disabled = true);
    
    const options = currentCard.querySelectorAll('.option-item');
    options.forEach(option => {
        const input = option.querySelector('input');
        const optionKey = input.value;
        const isCorrect = correctAnswerKeys.includes(optionKey);
        const isSelected = input.checked;
        
        if (isCorrect) {
            option.style.backgroundColor = '#d4edda';
            option.style.borderColor = '#28a745';
        } else if (isSelected) {
            option.style.backgroundColor = '#f8d7da';
            option.style.borderColor = '#dc3545';
        }
    });
    
    let explanationDiv = currentCard.querySelector('.answer-explanation');
    if (!explanationDiv) {
        explanationDiv = document.createElement('div');
        explanationDiv.className = 'answer-explanation';
        explanationDiv.innerHTML = `
            <div style="margin-top: 20px; padding: 15px; background-color: #e7f3ff; border-left: 4px solid #007bff; border-radius: 4px;">
                <strong>Correct Answer:</strong> ${correctAnswerKeys}<br>
                <strong>Explanation:</strong> ${correctAnswerText}
            </div>
        `;
        currentCard.appendChild(explanationDiv);
    }
    
    nextBtn.textContent = currentIndex < cards.length - 1 ? 'Next Question →' : 'Finish Exam';
    nextBtn.onclick = proceedToNext;
}

function startExamTimer() {
    examTimer = setInterval(function() {
        const elapsed = Date.now() - examStartTime;
        const remaining = examDuration - elapsed;
        
        if (remaining <= 0) {
            clearInterval(examTimer);
            alert('Time is up! Submitting exam automatically.');
            document.getElementById('examForm').submit();
            return;
        }
        
        const hours = Math.floor(remaining / (1000 * 60 * 60));
        const minutes = Math.floor((remaining % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((remaining % (1000 * 60)) / 1000);
        
        document.getElementById('timeRemaining').textContent = 
            String(hours).padStart(2, '0') + ':' + 
            String(minutes).padStart(2, '0') + ':' + 
            String(seconds).padStart(2, '0');
    }, 1000);
}

function showWarning(message) {
    document.getElementById('warningMessage').textContent = message;
    document.getElementById('warningModal').style.display = 'flex';
    
    if (warningCount >= 3) {
        document.getElementById('warningMessage').textContent += 
            ' You have received 3 warnings. The exam will be submitted automatically.';
        setTimeout(function() {
            document.getElementById('examForm').submit();
        }, 5000);
    }
}

function closeWarning() {
    document.getElementById('warningModal').style.display = 'none';
}

// Make functions globally accessible
window.closeWarning = closeWarning;
window.nextQuestion = nextQuestion;