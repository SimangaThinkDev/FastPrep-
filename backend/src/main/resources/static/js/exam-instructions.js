// Exam Instructions JavaScript
let examId;

function initializeInstructions(examIdParam) {
    examId = examIdParam;
    
    const confirmRules = document.getElementById('confirmRules');
    const startExamBtn = document.getElementById('startExamBtn');
    
    confirmRules.addEventListener('change', function() {
        startExamBtn.disabled = !this.checked;
    });
    
    startExamBtn.addEventListener('click', function() {
        if (confirmRules.checked) {
            const showAnswers = document.getElementById('showAnswers').checked;
            const examUrl = `/exam/start?examNum=${examId}&showAnswers=${showAnswers}`;
            window.location.href = examUrl;
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {
    // examId will be set by the HTML template
    if (typeof window.examId !== 'undefined') {
        initializeInstructions(window.examId);
    }
});