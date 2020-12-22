package com.iastate.edu.coms309.sb4.Getit.Server.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.Course;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
/**
 * @author Maxwell Smith
 *
 */
public interface CourseRepository extends CrudRepository<Course, Integer> {

	Course findCourseByCategoryAndNumber(String category, int number);

	Course findCourseById(int id);
	
	Set<Course> findCourseByProfessor(User prof);
}
