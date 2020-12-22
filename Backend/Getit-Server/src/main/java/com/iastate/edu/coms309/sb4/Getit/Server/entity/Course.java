package com.iastate.edu.coms309.sb4.Getit.Server.entity;

import java.util.Random;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Maxwell Smith
 *
 */

@Entity
public class Course {

	/**
	 * Course is a student course. Courses are identified by it's category and number
	 * 
	 * It keeps track of enlisted students & a public rating of the course.
	 *
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToMany
	@JoinTable(name = "enlistedStudents", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	Set<User> enlistedStudents;
	
	@ManyToMany
	@JoinTable(name = "studentsMarked", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> studentsMarked;
	
	 @ManyToOne
	 	@JoinColumn(name="professor", nullable=true)
	 	private User professor;

	private String category;
	private int number;
	
	private double rating;
	private int amtOfRatings;
	
	private String attendCode = "";


	public Course(String category, int number) {

		this.category = category;
		this.number = number;

	}

	public Course() {

	}

	public String getCategory() {
		return category;
	}

	public Integer getId() {
		return id;
	}
	
	
	
	public String getProfessorName() {
		if (professor != null) {
		return professor.getName();
		}else {
			return null;
		}
	}
	

	public String getProfessorEmail() {
		if (professor != null) {
		return professor.getEmail();
		}else {
			return null;
		}
	}
	
	public void setProfessor(User user) {
		professor = user;
	}

	@JsonIgnore
	public Set<User> getStudents() {
		return enlistedStudents;
	}
	
	@JsonIgnore
	public Set<User> getStudentsInClass() {
		return studentsMarked;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	
	//Rating System
	
	public double getRating() {
		return Math.floor(rating*100) / 100;
	}
	
	public void addRating(double score) {
		double newValue = rating * amtOfRatings;
		newValue += score;
		amtOfRatings += 1;
		rating = newValue / amtOfRatings;
	}
	
	//attend system
	
	public String generateAttendCode() {
		Random rand = new Random();
		
		if (studentsMarked != null) {
		studentsMarked.clear();
		}
		
		attendCode =  String.format("%04d", rand.nextInt(10000)); 
		return attendCode;
	}
	
	public boolean testAttendCode(String code) {
		return code.equals(attendCode);
	}


}
