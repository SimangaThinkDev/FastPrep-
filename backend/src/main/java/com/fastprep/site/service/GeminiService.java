package com.fastprep.site.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class GeminiService {

    @Value("${google.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();
    
    public String generateExamReview(String examTitle, List<Map<String, Object>> questions, Map<String, Object> userAnswers, int score, int totalQuestions) {
        String prompt = buildExamReviewPrompt(examTitle, questions, userAnswers, score, totalQuestions);
        
        try {
            String markdownResponse = callGeminiAPI(prompt);
            return convertMarkdownToHtml(markdownResponse);
        } catch (Exception e) {
            return generateEnhancedMockReview(examTitle, questions, userAnswers, score, totalQuestions);
        }
    }
    
    public String generateContent(String prompt) {
        try {
            String markdownResponse = callGeminiAPI(prompt);
            return convertMarkdownToHtml(markdownResponse);
        } catch (Exception e) {
            return "Mock AI Response: " + prompt + " - AI works by processing data through neural networks.";
        }
    }
    
    private String convertMarkdownToHtml(String markdown) {
        // Clean up any HTML document structure that Gemini might return
        String cleanedMarkdown = cleanHtmlDocument(markdown);
        
        Node document = parser.parse(cleanedMarkdown);
        return renderer.render(document);
    }
    
    private String cleanHtmlDocument(String content) {
        // If Gemini returned a full HTML document, extract just the body content
        if (content.contains("<body>")) {
            int bodyStart = content.indexOf("<body>") + 6;
            int bodyEnd = content.indexOf("</body>");
            if (bodyEnd > bodyStart) {
                content = content.substring(bodyStart, bodyEnd).trim();
            }
        }
        
        // Remove any remaining HTML document tags
        content = content.replaceAll("(?s)<!DOCTYPE[^>]*>", "")
                        .replaceAll("(?s)<html[^>]*>", "")
                        .replaceAll("(?s)</html>", "")
                        .replaceAll("(?s)<head[^>]*>.*?</head>", "")
                        .replaceAll("(?s)<meta[^>]*>", "")
                        .replaceAll("(?s)<title[^>]*>.*?</title>", "")
                        .trim();
        
        return content;
    }
    
    private String callGeminiAPI(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + apiKey;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);
        content.put("parts", List.of(part));
        requestBody.put("contents", List.of(content));
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        
        if (response.getBody() != null && response.getBody().containsKey("candidates")) {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (!candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> content_response = (Map<String, Object>) candidate.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content_response.get("parts");
                if (!parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }
            }
        }
        
        throw new RuntimeException("Failed to get response from Gemini API");
    }
    
    private String buildExamReviewPrompt(String examTitle, List<Map<String, Object>> questions, Map<String, Object> userAnswers, int score, int totalQuestions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AWS certification expert. Analyze this exam performance and provide a detailed review using Markdown formatting.\n\n");
        prompt.append("Exam: ").append(examTitle).append("\n");
        prompt.append("Score: ").append(score).append("/").append(totalQuestions).append("\n\n");
        
        prompt.append("Questions and User Answers:\n");
        for (int i = 0; i < Math.min(questions.size(), 5); i++) {
            Map<String, Object> question = questions.get(i);
            prompt.append("Q").append(i + 1).append(": ").append(question.get("question_text")).append("\n");
            prompt.append("Correct Answer: ").append(question.get("correct_answer_text")).append("\n");
            prompt.append("User Answer: ").append(userAnswers.getOrDefault("question_" + question.get("question_number"), "Not answered")).append("\n\n");
        }
        
        prompt.append("Please provide a detailed review using Markdown formatting:\n");
        prompt.append("1. Overall performance analysis\n");
        prompt.append("2. Key AWS knowledge gaps identified\n");
        prompt.append("3. Specific topics to study\n");
        prompt.append("4. Recommended AWS services to focus on\n");
        prompt.append("5. Study recommendations for improvement\n");
        prompt.append("Use Markdown formatting like ## headers, **bold**, *italic*, - lists, etc.\n");
        
        return prompt.toString();
    }
    
    private String generateEnhancedMockReview(String examTitle, List<Map<String, Object>> questions, Map<String, Object> userAnswers, int score, int totalQuestions) {
        double percentage = (double) score / totalQuestions * 100;
        
        StringBuilder review = new StringBuilder();
        review.append("<h2>AI-Powered Exam Performance Analysis</h2>");
        review.append("<p><strong>Exam:</strong> ").append(examTitle).append("</p>");
        review.append("<p><strong>Overall Score:</strong> ").append(score).append("/").append(totalQuestions).append(" (").append(String.format("%.1f", percentage)).append("%)</p>");
        
        if (percentage >= 80) {
            review.append("<p><strong>🎉 Excellent Performance!</strong> You demonstrate strong AWS knowledge.</p>");
            review.append("<h3>Areas of Strength:</h3>");
            review.append("<ul><li>Strong understanding of core AWS services</li><li>Good grasp of security best practices</li></ul>");
        } else if (percentage >= 60) {
            review.append("<p><strong>👍 Good Performance</strong> with room for improvement.</p>");
            review.append("<h3>Key Areas to Focus On:</h3>");
            review.append("<ul><li>Review AWS security services (IAM, CloudTrail)</li><li>Study compute services (EC2, Lambda)</li></ul>");
        } else {
            review.append("<p><strong>📖 Needs Improvement</strong> - Focus on AWS fundamentals.</p>");
            review.append("<h3>Priority Study Areas:</h3>");
            review.append("<ul><li>AWS Global Infrastructure</li><li>Core services: EC2, S3, RDS</li></ul>");
        }
        
        review.append("<h3>Next Steps:</h3>");
        review.append("<ol><li>Review AWS documentation</li><li>Take more practice exams</li><li>Consider AWS training courses</li></ol>");
        
        return review.toString();
    }
}
