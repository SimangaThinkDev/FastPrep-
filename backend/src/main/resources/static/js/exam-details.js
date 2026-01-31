// Exam Details Pagination
document.addEventListener('DOMContentLoaded', () => {
    const exams = Array.from(document.querySelectorAll('#exam-list .exam-item-card'));
    const pageSize = 5;
    let currentPage = 0;
    const totalPages = Math.ceil(exams.length / pageSize);

    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const pageInfo = document.getElementById('page-info');

    function showPage(page) {
        const start = page * pageSize;
        const end = start + pageSize;

        exams.forEach((exam, index) => {
            exam.style.display = (index >= start && index < end) ? 'block' : 'none';
        });

        pageInfo.textContent = `Page ${page + 1} of ${totalPages}`;
        prevBtn.disabled = page === 0;
        nextBtn.disabled = page === totalPages - 1;
    }

    prevBtn.addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            showPage(currentPage);
        }
    });

    nextBtn.addEventListener('click', () => {
        if (currentPage < totalPages - 1) {
            currentPage++;
            showPage(currentPage);
        }
    });

    // Initialize
    showPage(currentPage);
});