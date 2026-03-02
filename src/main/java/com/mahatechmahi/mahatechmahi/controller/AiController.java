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

	// 🛑 PASTE YOUR API KEY HERE:
	private final String GEMINI_API_KEY = "AIzaSyDRdEqzaJl-A0keQMp7fBSX3CvA_etMBQw";

	private final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
			+ GEMINI_API_KEY;

	@PostMapping
	public ResponseEntity<Map<String, String>> chatWithMahaBot(@RequestBody Map<String, String> request) {
		String userMessage = request.get("message");
		Map<String, String> result = new HashMap<>();

		try {
			// 🧠 The "Secret Brain" of Maha Bot!
			String systemPrompt = "You are Maha Bot, the official AI teaching assistant for Maha Tech Mahi. "
					+ "You help students learn Java, Spring Boot, and Full Stack development. "
					+ "Be energetic, supportive, and brilliant. "
					+ "IMPORTANT: If a student asks for the exact code answer, DO NOT give them the final code. "
					+ "Instead, explain the logic and give them a hint so they learn how to figure it out themselves!";

			String fullPrompt = systemPrompt + "\n\nStudent asks: " + userMessage;

			// 🛡️ BULLETPROOF JSON BUILDING
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> textNode = new HashMap<>();
			textNode.put("text", fullPrompt);

			Map<String, Object> partsNode = new HashMap<>();
			partsNode.put("parts", new Object[] { textNode });

			Map<String, Object> bodyMap = new HashMap<>();
			bodyMap.put("contents", new Object[] { partsNode });

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bodyMap, headers);

			// Send to Gemini API
			ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, entity, String.class);

			// Extract the text safely
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			String aiResponse = root.path("candidates").get(0).path("content").path("parts").get(0).path("text")
					.asText();

			result.put("reply", aiResponse);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			// 🚨 THIS PRINTS THE EXACT ERROR TO RENDER LOGS!
			System.err.println("❌ MAHA BOT ERROR: " + e.getMessage());
			e.printStackTrace();

			result.put("reply", "Oops! Maha Bot is taking a quick nap. Please try again later.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
}