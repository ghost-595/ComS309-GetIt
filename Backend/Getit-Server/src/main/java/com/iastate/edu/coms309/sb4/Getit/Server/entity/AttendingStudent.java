package com.iastate.edu.coms309.sb4.Getit.Server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
public class AttendingStudent 
{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	
		@NotNull
		@Size(max = 100)
	    @Column
		public String name;
		
		@NotNull
		@Size(max = 100)
	    @Column
		public String courseName;
		
		public AttendingStudent() {};
		
		public AttendingStudent(String name, String courseName)
		{
			this.name  = name;
			this.courseName = courseName;
		}
		

}
