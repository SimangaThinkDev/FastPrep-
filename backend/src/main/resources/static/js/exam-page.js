// Exam Page JavaScript - Minimal Working Version
let currentIndex = 0;
let cards, nextBtn, submitBtn, currentQSpan;
let examTimer;
let examStartTime = Date.now();
let examDuration = 180 * 60 * 1000;
let warningCount = 0;

// Initialize when DOM loads
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, initializing exam...');
    
    cards = document.querySelectorAll('.question-card');
    nextBtn = document.getElementById('nextBtn');
    submitBtn = document.getElementById('submitBtn');
    currentQSpan = document.getElementById('currentQ');
    
    console.log('Found elements:', {
        cards: cards.length,
        nextBtn: !!nextBtn,
        submitBtn: !!submitBtn,
        currentQSpan: !!currentQSpan
    });
    
    if (cards.length > 0) {
        showQuestion(0);
        startTimer();
        setupEventListeners();
    }
});

function showQuestion(index) {
    console.log('Showing question:', index);
    
    // Hide all cards, show current
    cards.forEach((card, i) => {
        card.style.display = i === index ? 'block' : 'none';
    });
    
    // Update question number
    if (currentQSpan) {
        currentQSpan.textContent = index + 1;
    }
    
    // Update navigation buttons
    if (index === cards.length - 1) {
        nextBtn.style.display = 'none';
        submitBtn.style.display = 'inline-block';
    } else {
        nextBtn.style.display = 'inline-block';
        submitBtn.style.display = 'none';
    }
    
    currentIndex = index;
}

function nextQuestion() {
    console.log('Next question clicked, current index:', currentIndex);
    
    const currentCard = cards[currentIndex];
    const inputs = currentCard.querySelectorAll('input[type="radio"], input[type="checkbox"]');
    const anyChecked = Array.from(inputs).some(inp => inp.checked);
    
    if (!anyChecked) {
        alert("Please select at least one option before proceeding.");
        return;
    }
    
    if (currentIndex < cards.length - 1) {
        showQuestion(currentIndex + 1);
    }
}

function startTimer() {
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
        
        const timeElement = document.getElementById('timeRemaining');
        if (timeElement) {
            timeElement.textContent = 
                String(hours).padStart(2, '0') + ':' + 
                String(minutes).padStart(2, '0') + ':' + 
                String(seconds).padStart(2, '0');
        }
    }, 1000);
}

function setupEventListeners() {
    // Tab switching detection
    document.addEventListener('visibilitychange', function() {
        if (document.hidden) {
            warningCount++;
            showWarning(`Warning ${warningCount}: You switched tabs or minimized the window.`);
        }
    });
}

function showWarning(message) {
    const warningModal = document.getElementById('warningModal');
    const warningMessage = document.getElementById('warningMessage');
    
    if (warningMessage) {
        warningMessage.textContent = message;
    }
    if (warningModal) {
        warningModal.style.display = 'flex';
    }
}

function closeWarning() {
    const warningModal = document.getElementById('warningModal');
    if (warningModal) {
        warningModal.style.display = 'none';
    }
}

// Make functions globally accessible
window.nextQuestion = nextQuestion;
window.closeWarning = closeWarning;