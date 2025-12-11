package com.fastprep.site.service;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamMetadata;
import com.fastprep.site.model.ExamLevel;
import com.fastprep.site.repository.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamService examService;

    private Exam testExam;

    @BeforeEach
    void setUp() {
        testExam = createTestExam(1, "Test Exam", "AWS Cloud Practitioner");
    }

    @Test
    void testGetExam() {
        when(examRepository.findByExamId(1)).thenReturn(testExam);

        Exam result = examService.getExam(1);

        assertNotNull(result);
        assertEquals(1, result.getMetadata().getExamId());
        verify(examRepository).findByExamId(1);
    }

    @Test
    void testGetExamsByCourseName() {
        List<Exam> expectedExams = List.of(testExam);
        when(examRepository.findByMetadataCourseName("AWS Cloud Practitioner")).thenReturn(expectedExams);

        List<Exam> result = examService.getExamsByCourseName("AWS Cloud Practitioner");

        assertEquals(1, result.size());
        verify(examRepository).findByMetadataCourseName("AWS Cloud Practitioner");
    }

    private Exam createTestExam(int examId, String title, String courseName) {
        Exam exam = new Exam();
        ExamMetadata metadata = new ExamMetadata();
        metadata.setExamId(examId);
        metadata.setTitle(title);
        metadata.setCourseName(courseName);
        exam.setMetadata(metadata);
        exam.setQuestions(List.of(Map.of("question_text", "Test question?")));
        return exam;
    }
}