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

	private String title;
	private String description;
	private String imageUrl;
	private String youtubeId;

	@Column(columnDefinition = "TEXT")
	private String blogContent;

	// --- Standard Getters and Setters ---

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
	// ... inside your Course class ...

	// NEW: This holds your entire Blog Post!
	@jakarta.persistence.Column(length = 10000)
	private String content;

	// ... (Keep existing fields) ...

	// NEW: Getters and Setters for content
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
// ... inside Course.java ...

	private String category; // <--- NEW: Add this line!

	// ...

	// ... scroll down to Getters and Setters ...

	// NEW: Add these two methods
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}