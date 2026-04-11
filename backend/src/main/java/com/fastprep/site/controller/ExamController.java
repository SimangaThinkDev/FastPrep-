package com.fastprep.site.controller;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.User;
import com.fastprep.site.model.AwsExamCatalog;
import com.fastprep.site.model.ExamLevel;
import com.fastprep.site.model.ExamReview;
import com.fastprep.site.service.ExamService;
import com.fastprep.site.service.GradingService;
import com.fastprep.site.service.UserService;
import com.fastprep.site.service.GeminiService;
import com.fastprep.site.repository.ExamReviewRepository;
import com.fastprep.site.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class ExamController {

    private final ExamService examService;
    private final GradingService gradingService;
    private final UserService userService;
    private final GeminiService geminiService;
    private final ExamReviewRepository examReviewRepository;

    public ExamController(ExamService examService, GradingService gradingService, UserService userService, 
                         GeminiService geminiService, ExamReviewRepository examReviewRepository) {
        this.examService = examService;
        this.gradingService = gradingService;
        this.userService = userService;
        this.geminiService = geminiService;
        this.examReviewRepository = examReviewRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        String a = "";
        a = a + "2";
        System.out.println( Integer.parseInt(a) );

        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");
        String userEmail = (String) session.getAttribute("userEmail");
        String userId = (String) session.getAttribute("userId");
        
        // Get actual user data from database
        User user = userService.getOrCreateUser(userEmail, userName);
        
        // Get recent exam review if available
        ExamReview recentReview = null;
        if (userId != null) {
            recentReview = examReviewRepository.findFirstByUserIdOrderByCreatedAtDesc(userId).orElse(null);
        }
        
        model.addAttribute("userName", userName != null ? userName : "User");
        model.addAttribute("userStats", user.getStats());
        model.addAttribute("examHistory", user.getExamHistory());
        model.addAttribute("recentReview", recentReview);
        
        return "dashboard";
    }

    /**
     * Show all exam courses with counts.
     * Groups by courseName (was previously level string) and counts the number of exams in each.
     */
    @GetMapping("/exams")
    public String examsPage(HttpSession session, Model model) {
        List<Exam> availableExams = examService.getAllExams();
        
        Map<String, Long> availableExamCounts = availableExams.stream()
                .filter(e -> e.getMetadata() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getMetadata().getCourseName(),
                        Collectors.counting()
                ));
        
        Map<ExamLevel, List<AwsExamCatalog.ExamInfo>> examsByLevel = AwsExamCatalog.getExamsByLevel();
        
        // Create ordered map by difficulty level
        Map<ExamLevel, List<AwsExamCatalog.ExamInfo>> orderedExamsByLevel = new java.util.LinkedHashMap<>();
        orderedExamsByLevel.put(ExamLevel.FOUNDATIONAL, examsByLevel.get(ExamLevel.FOUNDATIONAL));
        orderedExamsByLevel.put(ExamLevel.ASSOCIATE, examsByLevel.get(ExamLevel.ASSOCIATE));
        orderedExamsByLevel.put(ExamLevel.PROFESSIONAL, examsByLevel.get(ExamLevel.PROFESSIONAL));
        orderedExamsByLevel.put(ExamLevel.SPECIALTY, examsByLevel.get(ExamLevel.SPECIALTY));
        
        // Update availability based on actual exam data
        examsByLevel.values().stream()
                .flatMap(List::stream)
                .forEach(examInfo -> {
                    if (availableExamCounts.containsKey(examInfo.getName())) {
                        // Mark as available if we have exams for this course
                    }
                });
        
        model.addAttribute("examsByLevel", orderedExamsByLevel);
        model.addAttribute("availableExamCounts", availableExamCounts);
        
        return "exams";
    }

    /**
     * Show all exams for a specific course.
     */
    @GetMapping("/exam/details")
    public String examDetailsPage(@RequestParam("name") String courseName, 
                                 HttpSession session, 
                                 Model model) {
        List<Exam> exams = examService.getExamsByCourseName(courseName);

        if (exams.isEmpty()) {
            // Check if this is a valid exam from our catalog
            boolean isValidExam = AwsExamCatalog.getAllExams().stream()
                .anyMatch(examInfo -> examInfo.getName().equals(courseName));
            
            if (!isValidExam) {
                throw new RuntimeException("Exam not found: " + courseName);
            }
            
            // Valid exam but no practice tests available
            ExamLevel level = ExamLevel.fromCourseName(courseName);
            model.addAttribute("courseTitle", courseName);
            model.addAttribute("examLevel", level.getDisplayName());
            model.addAttribute("numExams", 0);
            model.addAttribute("exams", exams);
            model.addAttribute("comingSoon", true);
            
            return "exam_details";
        }

        // Sort by examId and add level information
        exams.sort(Comparator.comparingInt(e -> e.getMetadata().getExamId()));
        
        String examLevel = exams.get(0).getMetadata().getLevel().getDisplayName();

        model.addAttribute("courseTitle", courseName);
        model.addAttribute("examLevel", examLevel);
        model.addAttribute("numExams", exams.size());
        model.addAttribute("exams", exams);
        model.addAttribute("comingSoon", false);

        return "exam_details";
    }

    /**
     * Show exam instructions before starting.
     */
    @GetMapping("/exam/instructions")
    public String examInstructions(@RequestParam("examNum") int examNumber, 
                                  HttpSession session, 
                                  Model model) {
        Exam exam = examService.getExam(examNumber);
        if (exam == null) {
            throw new RuntimeException("Exam not found: " + examNumber);
        }

        model.addAttribute("examTitle", exam.getMetadata().getTitle());
        model.addAttribute("courseName", exam.getMetadata().getCourseName());
        model.addAttribute("examId", exam.getMetadata().getExamId());
        model.addAttribute("questionCount", exam.getQuestions().size());

        return "exam_instructions";
    }

    /**
     * Start a specific exam by examId.
     */
    @GetMapping("/exam/start")
    public String startExam(@RequestParam("examNum") int examNumber, 
                           @RequestParam(value = "showAnswers", defaultValue = "false") boolean showAnswers,
                           HttpSession session,
                           Model model) {
        Exam exam = examService.getExam(examNumber);
        if (exam == null) {
            throw new RuntimeException("Exam not found: " + examNumber);
        }

        model.addAttribute("title", exam.getMetadata().getTitle());
        model.addAttribute("examID", exam.getMetadata().getExamId());
        model.addAttribute("questionsArray", exam.getQuestions());
        model.addAttribute("showAnswers", showAnswers);

        return "exam_page";
    }

    /**
     * Grade a submitted exam and generate AI review.
     */
    @PostMapping("/exam/submit")
    public String submitExam(@RequestParam("examID") int examID,
                             HttpServletRequest request,
                             HttpSession session,
                             Model model) {
        Exam exam = examService.getExam(examID);
        if (exam == null) {
            throw new RuntimeException("Exam not found: " + examID);
        }

        // Extract and grade
        Map<Integer, List<String>> userAnswers = RequestUtils.extractUserAnswers(request);
        int score = gradingService.gradeExam(userAnswers, exam.getQuestions());
        
        // Record exam attempt for logged-in user
        String userId = (String) session.getAttribute("userId");
        System.out.println("DEBUG: userId from session: " + userId);
        
        if (userId != null) {
            userService.addExamAttempt(userId, examID, exam.getMetadata().getTitle(), 
                                     score, exam.getQuestions().size(), 180);
            
            // Generate AI review
            System.out.println("DEBUG: Generating AI review for user: " + userId);
            String aiReview = geminiService.generateExamReview(
                exam.getMetadata().getTitle(),
                exam.getQuestions(),
                RequestUtils.extractUserAnswersAsMap(request),
                score,
                exam.getQuestions().size()
            );
            
            // Save review to database
            ExamReview review = new ExamReview(userId, String.valueOf(examID), score, aiReview);
            ExamReview savedReview = examReviewRepository.save(review);
            System.out.println("DEBUG: Saved review with ID: " + savedReview.getId());
        } else {
            System.out.println("DEBUG: No userId in session - review not saved");
        }

        model.addAttribute("score", score);
        model.addAttribute("totalQuestions", exam.getQuestions().size());
        return "exam_result_simple";
    }
    
    @GetMapping("/exam/review/{reviewId}")
    public String viewExamReview(@PathVariable String reviewId, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        
        ExamReview review = examReviewRepository.findById(reviewId).orElse(null);
        if (review == null || !review.getUserId().equals(userId)) {
            throw new RuntimeException("Review not found");
        }
        
        model.addAttribute("review", review);
        return "exam_review";
    }
    
    @GetMapping("/analytics")
    public String viewAnalytics(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }
        
        // Get all exam reviews for the user
        List<ExamReview> allReviews = examReviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        // Get user exam history for score trends
        User user = userService.getOrCreateUser(
            (String) session.getAttribute("userEmail"),
            (String) session.getAttribute("userName")
        );
        
        model.addAttribute("allReviews", allReviews);
        model.addAttribute("examHistory", user.getExamHistory());
        model.addAttribute("userName", session.getAttribute("userName"));
        
        return "analytics";
    }
    
    @GetMapping("/resources")
    public String studyResources(Model model) {
        return "resources";
    }
    
    @GetMapping("/privacy")
    public String privacyPolicy(Model model) {
        return "privacy";
    }
    
    @GetMapping("/terms")
    public String termsOfService(Model model) {
        return "terms";
    }
}
