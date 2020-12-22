package com.themaxsmith.tests.springboottest1.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {
	
	Course testCourse = new Course("ENGLISH 150", 3.5, 30);
	
	
	//course output using JSON
	@GetMapping("/")
	public ResponseEntity<Course> getCourseById (@RequestParam(value = "id", defaultValue = "0") int id){
		
		if (id == 1 ) {
			return new ResponseEntity<Course>(testCourse, HttpStatus.OK);
		}
		
		return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
		   
	}

	@GetMapping("/rating/update")
	public ResponseEntity<Course> greeting(@RequestParam(value = "score", defaultValue = "5") double score) {
		System.out.println("add new score: "+score);
		testCourse.addRating(score);
		return new ResponseEntity<Course>(testCourse,HttpStatus.OK);
	}
	
	
}