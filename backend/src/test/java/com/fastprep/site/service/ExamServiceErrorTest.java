package com.fastprep.site.service;

import com.fastprep.site.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceErrorTest {

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamService examService;

    @Test
    void testDatabaseConnectionError() {
        when(examRepository.findByExamId(1))
            .thenThrow(new DataAccessException("Connection failed") {});

        assertThrows(DataAccessException.class, () -> {
            examService.getExam(1);
        }, "Should propagate database connection errors");
    }

    @Test
    void testRepositoryTimeout() {
        when(examRepository.count())
            .thenThrow(new DataAccessException("Timeout") {});

        assertThrows(DataAccessException.class, () -> {
            examRepository.count();
        }, "Should handle database timeouts");
    }
}