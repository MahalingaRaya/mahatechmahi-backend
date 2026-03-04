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

	// 🛡️ Securely fetch the API key from Render's Environment Variables
	private final String HF_API_KEY = System.getenv("HF_API_KEY");

	// Using Hugging Face's API with Microsoft's conversational model
	private final String HF_API_URL = "https://api-inference.huggingface.co/models/microsoft/DialoGPT-large";

	@PostMapping
	public ResponseEntity<Map<String, String>> chatWithMahaBot(@RequestBody Map<String, String> request) {
		String userMessage = request.get("message");
		Map<String, String> result = new HashMap<>();

		try {
			// Check if the environment variable was found
			if (HF_API_KEY == null || HF_API_KEY.isEmpty()) {
				throw new IllegalStateException("Hugging Face API key environment variable is missing!");
			}

			// Cleanly format the JSON String for Hugging Face
			String formattedUserMessage = userMessage.replace("\"", "\\\"");
			String requestBody = "{\"inputs\": \"" + formattedUserMessage + "\"}";

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(HF_API_KEY); // Securely pass the API key!

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			// Send to Hugging Face API
			ResponseEntity<String> response = restTemplate.postForEntity(HF_API_URL, entity, String.class);

			System.out.println("🤖 RAW HUGGING FACE RESPONSE: " + response.getBody());

			// Extract the text safely
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			String aiResponse = "Maha Bot is thinking..."; // Default

			if (root.isArray() && root.size() > 0) {
				aiResponse = root.get(0).path("generated_text").asText();
			}

			result.put("reply", aiResponse);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			System.err.println("❌ MAHA BOT ERROR: " + e.getMessage());
			e.printStackTrace();

			result.put("reply", "Oops! Maha Bot is taking a quick nap. Please try again later.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}
