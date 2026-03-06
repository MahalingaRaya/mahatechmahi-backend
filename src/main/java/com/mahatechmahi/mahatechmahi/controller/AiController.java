package com.mahatechmahi.mahatechmahi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class AiController {

	private final String GROQ_API_KEY = System.getenv("GROQ_API_KEY");
	private final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

	@PostMapping
	public ResponseEntity<Map<String, String>> chatWithMahaBot(@RequestBody Map<String, Object> request) {
		Map<String, String> result = new HashMap<>();

		try {
			if (GROQ_API_KEY == null || GROQ_API_KEY.isEmpty()) {
				throw new IllegalStateException("Groq API key is missing from Render!");
			}

			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, String>> messages = new ArrayList<>();

			// 🧠 1. Set the System Prompt (The Personality)
			Map<String, String> systemMessage = new HashMap<>();
			systemMessage.put("role", "system");
			systemMessage.put("content",
					"You are Maha Tech Mahi, the official AI teaching assistant for the Maha Tech Mahi platform. "
							+ "Your goal is to help students learn Java, Spring Boot, and Full Stack development through guided discovery. "
							+ "CRITICAL RULE: You must act as a Socratic tutor. You are strictly forbidden from writing complete code solutions. "
							+ "When a student asks for code, you MUST respond by: 1) Explaining the underlying logic. 2) Providing a small hint or pseudocode. 3) Asking the student to try writing the code themselves. "
							+ "Be energetic, friendly, and supportive!");
			messages.add(systemMessage);

			// 📚 2. Load the Chat Memory from Frontend
			if (request.containsKey("history")) {
				List<Map<String, String>> history = (List<Map<String, String>>) request.get("history");
				messages.addAll(history);
			}

			// 🚀 3. Build the Groq Request safely
			Map<String, Object> groqRequest = new HashMap<>();
			groqRequest.put("model", "llama3-8b-instant");
			groqRequest.put("messages", messages);

			String requestBody = mapper.writeValueAsString(groqRequest);

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(GROQ_API_KEY);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(GROQ_API_URL, entity, String.class);

			JsonNode root = mapper.readTree(response.getBody());
			String aiResponse = "Maha Tech Mahi is thinking...";

			if (root.has("choices") && root.path("choices").isArray()) {
				aiResponse = root.path("choices").get(0).path("message").path("content").asText();
			}

			result.put("reply", aiResponse);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			System.err.println("❌ MAHA TECH MAHI ERROR: " + e.getMessage());
			result.put("reply", "Server Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}