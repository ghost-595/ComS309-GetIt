package com.iastate.edu.coms309.sb4.Getit.Server.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iastate.edu.coms309.sb4.Getit.Server.RequestStatus;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.Course;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.CourseRepository;

/**
 * @author Maxwell Smith
 *
 */
@Controller
@RequestMapping(path = "/course")
public class CourseController {
	
	/**
	 * API calls for course information
	 *
	 */
	
	@Autowired
	private CourseRepository courseRepository;

	@GetMapping(path = "/list")
	public @ResponseBody RequestStatus courseList() {// @RequestBody Map<String, Object> payload) {

		return new RequestStatus(true, courseRepository.findAll());
	}

	@PostMapping(path = "/add")
	public @ResponseBody RequestStatus courseList(@RequestBody Map<String, Object> payload) {
		String category = (String) payload.get("category");
		int number = (int) payload.get("number");

		Course courseAlreadyExists = courseRepository.findCourseByCategoryAndNumber(category, number);
		
		if(courseAlreadyExists != null)
		{
			return new RequestStatus(false, "Course already exists");
		}
		else
		{
			Course course = new Course(category, number);
			courseRepository.save(course);
			return new RequestStatus(true, course);
		}
	}

	@PostMapping(path = "/students")
	public @ResponseBody RequestStatus courseStudents(@RequestBody Map<String, Object> payload) {
		int id = (int) payload.get("id");

		Course course = courseRepository.findCourseById(id);
		Set<User> students = course.getStudents();

		if (students != null) {
			return new RequestStatus(true, students);
		} else {
			return new RequestStatus(false, "could not find course");
		}
	}
	
	@PostMapping(path = "/")
	public @ResponseBody RequestStatus courseInfo(@RequestBody Map<String, Object> payload) {
		int id = (int) payload.get("courseID");

		Course course = courseRepository.findCourseById(id);
		if (course == null) {
			return new RequestStatus(false, "could not find course");
		}
		
		return new RequestStatus(true, course);
		
	}
	

	@PostMapping(path = "/rate")
	public @ResponseBody RequestStatus courseRate(@RequestBody Map<String, Object> payload) {
		int id = (int) payload.get("courseID");
		int score = (int) payload.get("score");
		
		Course course = courseRepository.findCourseById(id);
		
		if (course == null) {
			return new RequestStatus(false, "could not find course");
		}
		
		course.addRating(score);
		courseRepository.save(course);
		return new RequestStatus(true,"sent rating");
	}

}
