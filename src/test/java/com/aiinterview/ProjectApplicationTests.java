package com.aiinterview;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.ai.openai.api-key=dummy",
		"spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
class ProjectApplicationTests {

	@Test
	void contextLoads() {
	}
}
