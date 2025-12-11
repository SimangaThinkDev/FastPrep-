package com.fastprep.site.controller;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamMetadata;
import com.fastprep.site.service.ExamService;
import com.fastprep.site.service.GradingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExamService examService;

    @MockBean
    private GradingService gradingService;

    @Test
    void testExamsPage() throws Exception {
        mockMvc.perform(get("/exams"))
                .andExpect(status().isOk())
                .andExpect(view().name("exams"))
                .andExpect(model().attributeExists("examsByLevel"));
    }

    @Test
    void testExamInstructions() throws Exception {
        Exam testExam = createTestExam();
        when(examService.getExam(1)).thenReturn(testExam);

        mockMvc.perform(get("/exam/instructions").param("examNum", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("exam_instructions"))
                .andExpect(model().attribute("examTitle", "Test Exam"))
                .andExpect(model().attribute("examId", 1));
    }

    @Test
    void testStartExam() throws Exception {
        Exam testExam = createTestExam();
        when(examService.getExam(1)).thenReturn(testExam);

        mockMvc.perform(get("/exam/start").param("examNum", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("exam_page"))
                .andExpect(model().attribute("showAnswers", false));
    }

    private Exam createTestExam() {
        Exam exam = new Exam();
        ExamMetadata metadata = new ExamMetadata();
        metadata.setExamId(1);
        metadata.setTitle("Test Exam");
        metadata.setCourseName("AWS Cloud Practitioner");
        exam.setMetadata(metadata);
        exam.setQuestions(List.of(Map.of("question_text", "Test?")));
        return exam;
    }
}