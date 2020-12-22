package com.iastate.edu.coms309.sb4.Getit.Server;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;

public class RequestAttend {
	private User user;
	private int sizeOfClass;
	private int inClass;
	private int courseID;
	
	public RequestAttend(User u, int currentSize, int allSize, int course) {
		user = u;
		sizeOfClass = allSize;
		inClass = currentSize;
		setCourseID(course);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSizeOfClass() {
		return sizeOfClass;
	}

	public void setSizeOfClass(int sizeOfClass) {
		this.sizeOfClass = sizeOfClass;
	}

	public int getInClass() {
		return inClass;
	}

	public void setInClass(int inClass) {
		this.inClass = inClass;
	}

	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	
}
