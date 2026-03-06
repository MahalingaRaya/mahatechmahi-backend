package com.mahatechmahi.mahatechmahi.controller;

import java.util.HashMap;
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

	// 🛡️ Fetching the ultra-reliable Groq API Key
	private final String GROQ_API_KEY = System.getenv("GROQ_API_KEY");
	private final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

	@PostMapping
	public ResponseEntity<Map<String, String>> chatWithMahaBot(@RequestBody Map<String, String> request) {
		String userMessage = request.get("message");
		Map<String, String> result = new HashMap<>();

		try {
			if (GROQ_API_KEY == null || GROQ_API_KEY.isEmpty()) {
				throw new IllegalStateException("Groq API key is missing from Render!");
			}

			String formattedUserMessage = userMessage.replace("\"", "\\\"").replace("\n", " ");

			// 🧠 The Socratic Tutor Brain
			String systemPrompt = "You are Maha Tech Mahi, the official AI teaching assistant for Maha Tech Mahi. "
					+ "Your goal is to help students learn Java, Spring Boot, and Full Stack development through guided discovery. "
					+ "CRITICAL RULE: You must act as a Socratic tutor. You are strictly forbidden from writing complete code solutions. "
					+ "When a student asks for code, you MUST respond by: 1) Explaining the underlying logic. 2) Providing a small hint or pseudocode. 3) Asking the student to try writing the code themselves. "
					+ "Be energetic, friendly, and supportive!";

			// Using Groq's lightning-fast Llama 3 (8 Billion parameter) model
			String requestBody = "{\n" + "  \"model\": \"llama3-8b-8192\",\n" + "  \"messages\": [\n"
					+ "    {\"role\": \"system\", \"content\": \"" + systemPrompt + "\"},\n"
					+ "    {\"role\": \"user\", \"content\": \"" + formattedUserMessage + "\"}\n" + "  ]\n" + "}";

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(GROQ_API_KEY);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(GROQ_API_URL, entity, String.class);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			String aiResponse = "Maha Bot is thinking...";

			if (root.has("choices") && root.path("choices").isArray()) {
				aiResponse = root.path("choices").get(0).path("message").path("content").asText();
			}

			result.put("reply", aiResponse);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			System.err.println("❌ MAHA BOT ERROR: " + e.getMessage());
			result.put("reply", "Server Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}