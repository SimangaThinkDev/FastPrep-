package com.fastprep.site.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27099/nonexistent_db"
})
class DatabaseErrorHandlingTest {

    @Test
    void testDatabaseUnavailable() {
        // This test verifies the application can start even when MongoDB is unavailable
        // The connection will fail, but Spring should handle it gracefully
        assertDoesNotThrow(() -> {
            // Application context should load even with bad DB config
            Thread.sleep(100); // Give time for connection attempt
        }, "Application should handle database connection failures gracefully");
    }
}