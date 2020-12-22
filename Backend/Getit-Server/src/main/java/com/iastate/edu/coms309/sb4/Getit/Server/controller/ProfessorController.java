package com.iastate.edu.coms309.sb4.Getit.Server.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iastate.edu.coms309.sb4.Getit.Server.RequestStatus;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.Course;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.CourseRepository;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.UserRepository;

@Controller
@RequestMapping(path = "/professor")
public class ProfessorController {
	

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;
	
	
	@PostMapping(path = "/courses")
	public @ResponseBody RequestStatus profCourses(@RequestBody Map<String, Object> payload) {

		int id = (int) payload.get("profID");
		
		User user = userRepository.findUserById(id);

		if (user == null) {
			return new RequestStatus(false, "could not find prof user");
		}
		
		if (user.getRole() != 1) {
			return new RequestStatus(false, "not prof user");
		}
		
	
		
		return  new RequestStatus(true,courseRepository.findCourseByProfessor(user));	
	
	}
	
	@PostMapping(path = "/assign")
	public @ResponseBody RequestStatus profAssign(@RequestBody Map<String, Object> payload) {

		int id = (int) payload.get("profID");
		
		int courseID = (int) payload.get("courseID");
		
		User user = userRepository.findUserById(id);

		if (user == null) {
			return new RequestStatus(false, "could not find prof user");
		}
		
		if (user.getRole() != 1) {
			return new RequestStatus(false, "not prof user");
		}
		
		
		Course course = courseRepository.findCourseById(courseID);
	
		if (course == null) {
			return new RequestStatus(false, "could not find course");
		}
		
		course.setProfessor(user);
		courseRepository.save(course);
		
		return  new RequestStatus(true,"now prof of course");	
	
	}
	
	@PostMapping(path = "/genAttendCode")
	public @ResponseBody RequestStatus attendGenerate(@RequestBody Map<String, Object> payload) {
		int courseID = (int) payload.get("courseID");

		int id = (int) payload.get("profID");
		
		User user = userRepository.findUserById(id);

		if (user == null) {
			return new RequestStatus(false, "could not find prof user");
		}
		
		if (user.getRole() != 1) {
			return new RequestStatus(false, "not prof user");
		}
		
		Course c = courseRepository.findCourseById(courseID);
		
		if (c == null) {
			return new RequestStatus(false, "could not find course");
		}
		
		if (c.getProfessorEmail().equals(user.getEmail())) {
			String code = c.generateAttendCode();
			courseRepository.save(c);
			return  new RequestStatus(true, code);	
		}else {
			return new RequestStatus(false, "not prof for class");
		}
		
		
		
	
	}
}
