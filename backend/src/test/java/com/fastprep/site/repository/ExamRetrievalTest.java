package com.fastprep.site.repository;

import com.fastprep.site.model.Exam;
import com.fastprep.site.model.ExamMetadata;
import com.fastprep.site.model.ExamLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/examdb_test"
})
class ExamRetrievalTest {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        examRepository.deleteAll();
    }

    @Test
    void testFindByExamId() {
        Exam testExam = createTestExam(1, "Test Exam 1", "AWS Cloud Practitioner");
        examRepository.save(testExam);

        Exam found = examRepository.findByExamId(1);
        
        assertNotNull(found, "Should find exam by ID");
        assertEquals(1, found.getMetadata().getExamId());
        assertEquals("Test Exam 1", found.getMetadata().getTitle());
    }

    @Test
    void testFindByCourseName() {
        Exam exam1 = createTestExam(1, "Test Exam 1", "AWS Cloud Practitioner");
        Exam exam2 = createTestExam(2, "Test Exam 2", "AWS Cloud Practitioner");
        Exam exam3 = createTestExam(3, "Test Exam 3", "Solutions Architect - Associate");
        
        examRepository.saveAll(List.of(exam1, exam2, exam3));

        List<Exam> cloudPractitionerExams = examRepository.findByMetadataCourseName("AWS Cloud Practitioner");
        List<Exam> architectExams = examRepository.findByMetadataCourseName("Solutions Architect - Associate");

        assertEquals(2, cloudPractitionerExams.size());
        assertEquals(1, architectExams.size());
    }

    @Test
    void testFindByLevel() {
        Exam foundationalExam = createTestExam(1, "Foundational Exam", "AWS Cloud Practitioner");
        foundationalExam.getMetadata().setLevel(ExamLevel.FOUNDATIONAL);
        
        Exam associateExam = createTestExam(2, "Associate Exam", "Solutions Architect - Associate");
        associateExam.getMetadata().setLevel(ExamLevel.ASSOCIATE);
        
        examRepository.saveAll(List.of(foundationalExam, associateExam));

        List<Exam> foundationalExams = examRepository.findByMetadataLevel(ExamLevel.FOUNDATIONAL);
        List<Exam> associateExams = examRepository.findByMetadataLevel(ExamLevel.ASSOCIATE);

        assertEquals(1, foundationalExams.size());
        assertEquals(1, associateExams.size());
        assertEquals(ExamLevel.FOUNDATIONAL, foundationalExams.get(0).getMetadata().getLevel());
    }

    @Test
    void testFindByDifficulty() {
        Exam easyExam = createTestExam(1, "Easy Exam", "AWS Cloud Practitioner");
        easyExam.getMetadata().setDifficulty(1);
        
        Exam hardExam = createTestExam(2, "Hard Exam", "Solutions Architect - Professional");
        hardExam.getMetadata().setDifficulty(3);
        
        examRepository.saveAll(List.of(easyExam, hardExam));

        List<Exam> easyExams = examRepository.findByMetadataDifficulty(1);
        List<Exam> hardExams = examRepository.findByMetadataDifficulty(3);

        assertEquals(1, easyExams.size());
        assertEquals(1, hardExams.size());
    }

    @Test
    void testFindAllOrderedByLevelAndDifficulty() {
        Exam exam1 = createTestExam(1, "Professional Exam", "Solutions Architect - Professional");
        exam1.getMetadata().setLevel(ExamLevel.PROFESSIONAL);
        exam1.getMetadata().setDifficulty(3);
        
        Exam exam2 = createTestExam(2, "Foundational Exam", "AWS Cloud Practitioner");
        exam2.getMetadata().setLevel(ExamLevel.FOUNDATIONAL);
        exam2.getMetadata().setDifficulty(1);
        
        examRepository.saveAll(List.of(exam1, exam2));

        List<Exam> orderedExams = examRepository.findAllByOrderByMetadataLevelAscMetadataDifficultyAsc();

        assertEquals(2, orderedExams.size());
        assertEquals(ExamLevel.FOUNDATIONAL, orderedExams.get(0).getMetadata().getLevel());
        assertEquals(ExamLevel.PROFESSIONAL, orderedExams.get(1).getMetadata().getLevel());
    }

    private Exam createTestExam(int examId, String title, String courseName) {
        Exam exam = new Exam();
        
        ExamMetadata metadata = new ExamMetadata();
        metadata.setExamId(examId);
        metadata.setTitle(title);
        metadata.setCourseName(courseName);
        metadata.setDifficulty(1);
        
        exam.setMetadata(metadata);
        exam.setQuestions(List.of(
            Map.of("question_number", 1, "question_text", "Test question?", "correct_answer_keys", "A")
        ));
        
        return exam;
    }
}