package com.iastate.edu.coms309.sb4.Getit.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.AttendingStudent;

public interface AttendingStudentRepository extends JpaRepository<AttendingStudent, Long> {

}
