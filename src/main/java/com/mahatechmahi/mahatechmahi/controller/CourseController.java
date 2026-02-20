package com.mahatechmahi.mahatechmahi.controller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahatechmahi.mahatechmahi.model.Course;

interface CourseRepository extends JpaRepository<Course, Long> {
}

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
	private final CourseRepository repository;

	public CourseController(CourseRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Course> getAllCourses() {
		return repository.findAll();
	}

	@PostMapping
	public Course addCourse(@RequestBody Course course) {
		return repository.save(course);
	}

	// NEW: Update an existing course
	@org.springframework.web.bind.annotation.PutMapping("/{id}")
	public Course updateCourse(@org.springframework.web.bind.annotation.PathVariable Long id,
			@org.springframework.web.bind.annotation.RequestBody Course courseDetails) {
		Course course = repository.findById(id).orElseThrow();

		// Update all fields with new data
		course.setTitle(courseDetails.getTitle());
		course.setCategory(courseDetails.getCategory());
		course.setDescription(courseDetails.getDescription());
		course.setImageUrl(courseDetails.getImageUrl());
		course.setYoutubeId(courseDetails.getYoutubeId());
		course.setContent(courseDetails.getContent()); // Don't forget the blog content!

		return repository.save(course);
	}

	@DeleteMapping("/{id}")
	public void deleteCourse(@PathVariable Long id) {
		repository.deleteById(id);
	}
}