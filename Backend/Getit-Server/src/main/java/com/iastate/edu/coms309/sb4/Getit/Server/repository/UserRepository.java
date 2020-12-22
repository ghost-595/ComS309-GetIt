package com.iastate.edu.coms309.sb4.Getit.Server.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
/**
 * @author Maxwell Smith
 *
 */
public interface UserRepository extends CrudRepository<User, Integer> {
	User findUserByEmailAndPassword(String email, String password);

	User findUserById(int id);
	
	Set<User> findUserByRole(int id);

}