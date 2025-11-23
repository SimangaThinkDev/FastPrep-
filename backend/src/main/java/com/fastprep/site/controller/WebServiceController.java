package com.fastprep.site.controller;

import com.fastprep.site.model.Exam;
import com.fastprep.site.service.ExamService;
import com.fastprep.site.service.GradingTools;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fastprep.site.service.Tools.extractUserData;
import static com.fastprep.site.persistence.ExamFileOperations.toggleActiveController;

@Controller
@RequestMapping("")
public class WebServiceController {

    private final ExamService examService;

    public WebServiceController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    /**
     * Show all exam courses with counts.
     * Groups by courseName (was previously level string) and counts the number of exams in each.
     */
    @GetMapping("/exams")
    public String examsPage(Model model) {
        toggleActiveController("Exams Page");

        // Fetch all exams
        List<Exam> allExams = examService.getAllExams();

        // Filter out exams with null metadata to avoid NPE
        List<Exam> validExams = allExams.stream()
                .filter(e -> e.getMetadata() != null)
                .toList();

        // Group exams by courseName
        Map<String, Long> levels = validExams.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getMetadata().getCourseName(),
                        Collectors.counting()
                ));

        model.addAttribute("allCertificationAvailable", levels);
        model.addAttribute("numAvailableCourses", levels.size());

        // Optional: log exams missing metadata
        allExams.stream()
                .filter(e -> e.getMetadata() == null)
                .forEach(e -> System.out.println("Exam with null metadata: " + e.getId()));

        return "exams";
    }

    /**
     * Show all exams for a specific course.
     */
    @GetMapping("/exam/details")
    public String examDetailsPage(@RequestParam("name") String courseName, Model model) {
        List<Exam> exams = examService.getExamsByCourseName(courseName);

        // Sort by numeric examId
        exams.sort(Comparator.comparingInt(e -> e.getMetadata().getExamId()));

        model.addAttribute("courseTitle", courseName);
        model.addAttribute("numExams", exams.size());
        model.addAttribute("exams", exams);

        return "exam_details";
    }

    /**
     * Start a specific exam by examId.
     */
    @GetMapping("/exam/start")
    public String startExam(@RequestParam("examNum") int examNumber, Model model) {
        Exam exam = examService.getExam(examNumber);
        if (exam == null) {
            throw new RuntimeException("Exam not found: " + examNumber);
        }

        model.addAttribute("title", exam.getMetadata().getTitle());
        model.addAttribute("examID", exam.getMetadata().getExamId());
        model.addAttribute("questionsArray", exam.getQuestions());

        return "exam_page";
    }

    /**
     * Grade a submitted exam.
     */
    @PostMapping("/exam/submit")
    public String submitExam(@RequestParam("examID") int examID,
                             HttpServletRequest request,
                             Model model) {
        Exam exam = examService.getExam(examID);
        if (exam == null) {
            throw new RuntimeException("Exam not found during grading: " + examID);
        }

        Map<Integer, List<String>> userAnswers = extractUserData(request);
        JSONArray questionsArray = new JSONArray(exam.getQuestions());

        int score = GradingTools.gradeAll(userAnswers, questionsArray);

        model.addAttribute("score", score);
        return "exam_result";
    }
}
