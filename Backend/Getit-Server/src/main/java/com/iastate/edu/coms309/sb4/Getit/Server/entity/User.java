package com.iastate.edu.coms309.sb4.Getit.Server.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Maxwell Smith
 *
 */

@Entity
public class User {

	/**
	 * User can ether be a student or professor or professor waiting to be approved
	 *
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Integer id;

	/**
	 * userType: 0=student,-1=professor pending approval, 1=professor approved,
	 * -2=rejected professor, 2=admin
	 */

	private int role = 0;

	@ManyToMany(mappedBy = "enlistedStudents")
	private Set<Course> followedCourse;

	@NotEmpty(message = "Name may not be empty")
	private String name;

	@NotEmpty(message = "Email may not be empty")
	private String email;

	@NotEmpty(message = "Password may not be empty")
	private String password;

	public User() {
		super();
	}

	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}

	protected String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int type) {
		this.role = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(unique = true)
	public String getEmail() {
		return email;
	}

	public Set<Course> getCourses() {
		return followedCourse;
	}

	public void addCourse(Course c) {
		if (followedCourse == null) {
			// incase DB is not a mysql relationship init it (mostly for testing)
			followedCourse = new HashSet<Course>();
		}

		followedCourse.add(c);
	}

	public void setEmail(String email) {
		this.email = email;
	}
}