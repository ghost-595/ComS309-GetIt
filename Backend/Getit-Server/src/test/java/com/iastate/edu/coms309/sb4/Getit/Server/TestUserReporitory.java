package com.iastate.edu.coms309.sb4.Getit.Server;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.Course;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestUserReporitory {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void findUserByEmailAndPassword() {

		User testUser = new User("max@gmail.com", "Max", "test");
		entityManager.persist(testUser);
		entityManager.flush();

		User found = userRepository.findUserByEmailAndPassword("max@gmail.com", "test");

		assertEquals(found.getEmail(), testUser.getEmail());
	}

	@Test
	public void userWrongPassword() {

		User testUser = new User("max@gmail.com", "Max", "test");
		entityManager.persist(testUser);
		entityManager.flush();

		User found = userRepository.findUserByEmailAndPassword("max@gmail.com", "smith");

		assertNull(found);
	}

	@Test
	public void userAddCourses() {
		Course testCourse = new Course("ENG", 150);
		User testUser = new User("max@gmail.com", "Max", "test");
		entityManager.persist(testUser);

		entityManager.persist(testCourse);
		entityManager.flush();

		User found = userRepository.findUserById(testUser.getId());
		found.addCourse(testCourse);

		assertEquals(found.getCourses().size(), 1);
	}
	
	@Test
	public void userFindLookApprovalProf() {

		User testProfUser = new User("teacher@gmail.com", "Admin", "test");
		
		testProfUser.setRole(-1);
		
		entityManager.persist(testProfUser);

		entityManager.flush();

		Set<User> found = userRepository.findUserByRole(-1);

		assertEquals(found.contains(testProfUser), true);
	}
	
	
	@Test
	public void testGenAttendAndVaildation() {

		Course testCourse = new Course("ENG", 150);
		User testUser = new User("teacher@gmail.com", "Teacher", "test");
		entityManager.persist(testUser);

		entityManager.persist(testCourse);
		entityManager.flush();
		
		testCourse.setProfessor(testUser);

		String attendCode = testCourse.generateAttendCode();
		
		
		
		assertEquals(true, testCourse.testAttendCode(attendCode));
	}
	
	
	@Test
	public void userFindLookApprovalProfAndSetToProf() {

		User testProfUser = new User("teacher@gmail.com", "Admin", "test");
		Course testCourse = new Course("ENG", 150);
		testProfUser.setRole(-1);
		
		entityManager.persist(testProfUser);
		entityManager.persist(testCourse);
		entityManager.flush();

		Set<User> found = userRepository.findUserByRole(-1);
		
		assertEquals(found.contains(testProfUser), true);
		
		testProfUser.setRole(1);
		
		testCourse.setProfessor(testProfUser);
		
		assertEquals(testCourse.getProfessorName(), testProfUser.getName());
		
	}
	
	
	
	@Test
	public void testRatingNon() {

		Course testCourse = new Course("ENG", 150);
	
	

		entityManager.persist(testCourse);
		entityManager.flush();
		
		
		
		assertEquals(0, testCourse.getRating());
	}
	
	@Test
	public void testRatingWithScores() {

		Course testCourse = new Course("ENG", 150);
	
		

		entityManager.persist(testCourse);
		entityManager.flush();
		
		testCourse.addRating(5);
		testCourse.addRating(1);
		
		assertEquals(3, testCourse.getRating());
	}

	@Test
	public void testCourseBasicInfo() {

		Course testCourse = new Course("ENG", 150);
	
		

		entityManager.persist(testCourse);
		entityManager.flush();
		
		
		assertEquals("ENG", testCourse.getCategory());
		assertEquals(150, testCourse.getNumber());
	}
	
	
	@Test
	public void testUserBasicInfo() {

		User testStudentUser = new User("student@gmail.com", "Student", "test");
		testStudentUser.setRole(0);
		entityManager.persist(testStudentUser);
		entityManager.flush();
		
		
		assertEquals("student@gmail.com", testStudentUser.getEmail());
		assertEquals("Student", testStudentUser.getName());
		assertEquals(0, testStudentUser.getRole());
	}
}