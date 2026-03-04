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

	// The NEW Universal Hugging Face Chat API Endpoint
	private final String HF_API_URL = "https://router.huggingface.co/v1/chat/completions";

	@PostMapping
	public ResponseEntity<Map<String, String>> chatWithMahaBot(@RequestBody Map<String, String> request) {
		String userMessage = request.get("message");
		Map<String, String> result = new HashMap<>();

		try {
			if (HF_API_KEY == null || HF_API_KEY.isEmpty()) {
				throw new IllegalStateException("Hugging Face API key is missing!");
			}

			// Clean user input
			String formattedUserMessage = userMessage.replace("\"", "\\\"").replace("\n", " ");

			// NEW JSON format required by the Router API
			String requestBody = "{\n" + "  \"model\": \"HuggingFaceH4/zephyr-7b-beta\",\n"
					+ "  \"messages\": [{\"role\": \"user\", \"content\": \"" + formattedUserMessage + "\"}]\n" + "}";

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(HF_API_KEY);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			// Send to API
			ResponseEntity<String> response = restTemplate.postForEntity(HF_API_URL, entity, String.class);
			System.out.println("🤖 RAW HF RESPONSE: " + response.getBody());

			// Extract from NEW JSON Response structure
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

			// NO MORE "TAKING A NAP". Print the literal exact error to the website screen!
			result.put("reply", "Server Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}