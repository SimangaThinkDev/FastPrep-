package com.fastprep.site.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/examdb_test"
})
class DatabaseConnectionTest {

    // Note: These tests require MongoDB to be running on localhost:27017
    // They will fail if MongoDB is not available

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ExamRepository examRepository;

    @Test
    void testMongoDBConnection() {
        assertNotNull(mongoTemplate, "MongoTemplate should be available");
        
        String databaseName = mongoTemplate.getDb().getName();
        assertNotNull(databaseName, "Database name should not be null");
        assertEquals("examdb_test", databaseName, "Should connect to test database");
    }

    @Test
    void testRepositoryConnection() {
        assertNotNull(examRepository, "ExamRepository should be available");
        
        assertDoesNotThrow(() -> {
            examRepository.count();
        }, "Repository should be able to perform count operation");
    }

    @Test
    void testDatabaseOperations() {
        assertDoesNotThrow(() -> {
            mongoTemplate.getCollectionNames();
        }, "Should be able to get collection names");
    }
}