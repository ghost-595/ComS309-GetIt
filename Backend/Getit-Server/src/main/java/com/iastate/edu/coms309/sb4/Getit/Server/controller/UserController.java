package com.iastate.edu.coms309.sb4.Getit.Server.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iastate.edu.coms309.sb4.Getit.Server.RequestAttend;
import com.iastate.edu.coms309.sb4.Getit.Server.RequestStatus;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.Course;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.CourseRepository;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.UserRepository;

/**
 * @author Maxwell Smith
 *
 */
@Controller
@RequestMapping(path = "/user")
public class UserController {
	/**
	 * API calls for user auth & information
	 *
	 */

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseRepository courseRepository;

	@PostMapping(path = "/login")
	public @ResponseBody RequestStatus loginUser(@RequestBody Map<String, Object> payload) {

		User user = userRepository.findUserByEmailAndPassword((String) payload.get("email"),
				(String) payload.get("password"));

		if (user != null) {
			return new RequestStatus(true, user);
		}

		return new RequestStatus(false, "could not find user!");
	}

	@PostMapping(path = "/createAccount")
	public @ResponseBody RequestStatus createAccount(@RequestBody Map<String, Object> payload) {

		String name = (String) payload.get("name");
		String email = (String) payload.get("email");
		String password = (String) payload.get("password");

		Integer role = Integer.parseInt(payload.get("role").toString());

		if (name.isEmpty() || email.isEmpty() || password.length() < 4) {
			return new RequestStatus(true, "Input Failed");
		}

		User n = new User();
		n.setName(name);
		n.setEmail(email);
		n.setPassword(password);
		n.setRole(role);

		userRepository.save(n);
		return new RequestStatus(true, "New Account Created");

	}

	@PostMapping(path = "/courses")
	public @ResponseBody RequestStatus userCourses(@RequestBody Map<String, Object> payload) {

		User user = userRepository.findUserById(Integer.parseInt(payload.get("userID").toString()));

		if (user != null) {
			return new RequestStatus(true, user.getCourses());
		}

		return new RequestStatus(false, "could not find user!");
	}

	@PostMapping(path = "/join")
	public @ResponseBody RequestStatus courseJoin(@RequestBody Map<String, Object> payload) {

		String category = (String) payload.get("category");
		int number = (int) payload.get("number");

		int id = (int) payload.get("userID");

		User user = userRepository.findUserById(id);

		Course course = courseRepository.findCourseByCategoryAndNumber(category, number);

		// auto create course
		if (course == null) {
			course = new Course(category, number);
			courseRepository.save(course);
			course = courseRepository.findCourseByCategoryAndNumber(category, number);
		}

		if (user != null) {

			user.getCourses().add(course);
			course.getStudents().add(user);
			courseRepository.save(course);

			return new RequestStatus(true, user);

		} else {

			return new RequestStatus(false, "Please check to make sure the user exist!");

		}

	}

	@PostMapping(path = "/attend")
	public @ResponseBody RequestStatus attendCourse(@RequestBody Map<String, Object> payload) {

		int courseID = (int) payload.get("courseID");

		int id = (int) payload.get("userID");

		String attendCode = (String) payload.get("attendCode");

		User user = userRepository.findUserById(id);

		Course course = courseRepository.findCourseById(courseID);

		if (user == null) {
			return new RequestStatus(false, "could not find user");
		}

		if (course == null) {
			return new RequestStatus(false, "could not find course");
		}

		if (!course.getStudents().contains(user)) {

			return new RequestStatus(false, "user not in course");
		}

		if (course.testAttendCode(attendCode)) {

			course.getStudentsInClass().add(user);

			courseRepository.save(course);
			try {
				sendToProf(new RequestAttend(user, course.getStudentsInClass().size(), course.getStudents().size(),
						courseID));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new RequestStatus(true, "attend code vaild");
		} else {
			return new RequestStatus(false, "attend code not vaild");
		}

	}

	@MessageMapping("/services")
	@SendTo("/attend/courseAttend")
	public RequestAttend sendToProf(RequestAttend answer) throws Exception {
		return answer;
	}

}
