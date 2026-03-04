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

	private final String HF_API_KEY = System.getenv("HF_API_KEY");
	private final String HF_API_URL = "https://router.huggingface.co/v1/chat/completions";

	@PostMapping
	public ResponseEntity<Map<String, String>> chatWithMahaBot(@RequestBody Map<String, String> request) {
		String userMessage = request.get("message");
		Map<String, String> result = new HashMap<>();

		try {
			if (HF_API_KEY == null || HF_API_KEY.isEmpty()) {
				throw new IllegalStateException("Hugging Face API key is missing!");
			}

			String formattedUserMessage = userMessage.replace("\"", "\\\"").replace("\n", " ");

			// 🧠 THE SECRET BRAIN: Tell the AI exactly who it is!
			String systemPrompt = "You are Maha Bot, the official AI teaching assistant for Maha Tech Mahi. "
					+ "You help students learn Java, Spring Boot, and Full Stack development. "
					+ "You must strictly identify yourself as Maha Bot. Never mention Qwen, Hugging Face, or Alibaba Cloud. "
					+ "Be energetic, friendly, and supportive!";

			// We now send TWO messages: The hidden system instructions, and the user's
			// question
			String requestBody = "{\n" + "  \"model\": \"Qwen/Qwen2.5-7B-Instruct\",\n" + "  \"messages\": [\n"
					+ "    {\"role\": \"system\", \"content\": \"" + systemPrompt + "\"},\n"
					+ "    {\"role\": \"user\", \"content\": \"" + formattedUserMessage + "\"}\n" + "  ]\n" + "}";

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(HF_API_KEY);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(HF_API_URL, entity, String.class);

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