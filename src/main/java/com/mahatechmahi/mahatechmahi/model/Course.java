package com.mahatechmahi.mahatechmahi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// --- Core Information ---
	private String title;
	private String description; // We will use this as the quick "Hook"
	private String category; // e.g., "Frontend", "Backend"

	// --- Media & Content ---
	private String imageUrl; // Thumbnail image link
	private String youtubeId; // e.g., "dQw4w9WgXcQ"

	@Column(columnDefinition = "TEXT")
	private String blogContent; // The step-by-step tutorial

	// --- NEW: Mini-Project Fields ---
	private String techStack; // e.g., "HTML, CSS, JS"
	private String difficulty; // e.g., "Beginner", "Intermediate"
	private String sourceCodeUrl; // GitHub repository link

	// Empty Constructor
	public Course() {
	}

	// ==========================================
	// GETTERS AND SETTERS
	// ==========================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}

	public String getBlogContent() {
		return blogContent;
	}

	public void setBlogContent(String blogContent) {
		this.blogContent = blogContent;
	}

	public String getTechStack() {
		return techStack;
	}

	public void setTechStack(String techStack) {
		this.techStack = techStack;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getSourceCodeUrl() {
		return sourceCodeUrl;
	}

	public void setSourceCodeUrl(String sourceCodeUrl) {
		this.sourceCodeUrl = sourceCodeUrl;
	}
}