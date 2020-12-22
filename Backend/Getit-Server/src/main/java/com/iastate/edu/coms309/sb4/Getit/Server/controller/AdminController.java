package com.iastate.edu.coms309.sb4.Getit.Server.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iastate.edu.coms309.sb4.Getit.Server.RequestStatus;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.UserRepository;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	
	@PostMapping(path = "/approve")
	public @ResponseBody RequestStatus adminApprove(@RequestBody Map<String, Object> payload) {
		
		User userAdmin = userRepository.findUserById(Integer.parseInt(payload.get("adminID").toString()));

		if (userAdmin == null) {
			return new RequestStatus(false, "could not find admin user");
		}

		if (userAdmin.getRole() != 2) {
			return new RequestStatus(false, "not admin user");
		}
		
		User user = userRepository.findUserById(Integer.parseInt(payload.get("userID").toString()));

		if (user == null) {
			return new RequestStatus(false, "could not find prof user");
		}
		
		user.setRole(1);
		
		userRepository.save(user);
		
		return new RequestStatus(true, "changed user to prof");
	
	}
	
	@PostMapping(path = "/list")
	public @ResponseBody RequestStatus adminList(@RequestBody Map<String, Object> payload) {
		
		User userAdmin = userRepository.findUserById(Integer.parseInt(payload.get("adminID").toString()));

		if (userAdmin == null) {
			return new RequestStatus(false, "could not find admin user");
		}
		
		if (userAdmin.getRole() != 2) {
			return new RequestStatus(false, "not admin user");
		}
		
		
		return new RequestStatus(true, userRepository.findUserByRole(-1));
	}
}
