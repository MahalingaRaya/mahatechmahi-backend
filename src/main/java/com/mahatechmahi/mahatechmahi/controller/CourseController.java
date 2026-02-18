package com.mahatechmahi.mahatechmahi.controller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}