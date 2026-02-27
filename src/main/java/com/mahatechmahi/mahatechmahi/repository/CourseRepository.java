package com.mahatechmahi.mahatechmahi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahatechmahi.mahatechmahi.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}