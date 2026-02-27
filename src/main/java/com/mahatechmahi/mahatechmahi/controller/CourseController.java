package com.mahatechmahi.mahatechmahi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahatechmahi.mahatechmahi.model.Course;
import com.mahatechmahi.mahatechmahi.repository.CourseRepository;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

	private final CourseRepository repository;

	public CourseController(CourseRepository repository) {
		this.repository = repository;
	}

	// 1. Get ALL courses (For Homepage & Admin List)
	@GetMapping
	public List<Course> getAllCourses() {
		return repository.findAll();
	}

	// 2. NEW: Get ONE course by ID (Fixes the Learning Hub / Blog Page!)
	@GetMapping("/{id}")
	public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
		return repository.findById(id).map(course -> ResponseEntity.ok().body(course))
				.orElse(ResponseEntity.notFound().build());
	}

	// 3. Add a NEW course
	@PostMapping
	public Course addCourse(@RequestBody Course course) {
		return repository.save(course);
	}

	// 4. UPDATED: Edit an EXISTING course
	@PutMapping("/{id}")
	public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
		return repository.findById(id).map(course -> {
			// Core Info
			course.setTitle(courseDetails.getTitle());
			course.setDescription(courseDetails.getDescription());
			course.setCategory(courseDetails.getCategory());

			// Media & Content
			course.setImageUrl(courseDetails.getImageUrl());
			course.setYoutubeId(courseDetails.getYoutubeId());
			course.setBlogContent(courseDetails.getBlogContent());

			// Mini-Project Tags
			course.setTechStack(courseDetails.getTechStack());
			course.setDifficulty(courseDetails.getDifficulty());
			course.setSourceCodeUrl(courseDetails.getSourceCodeUrl());

			Course updatedCourse = repository.save(course);
			return ResponseEntity.ok().body(updatedCourse);
		}).orElse(ResponseEntity.notFound().build());
	}

	// 5. Delete a course
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
		return repository.findById(id).map(course -> {
			repository.delete(course);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}