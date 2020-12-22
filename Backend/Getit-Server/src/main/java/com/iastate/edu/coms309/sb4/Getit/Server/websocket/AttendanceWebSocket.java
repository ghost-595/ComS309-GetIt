package com.iastate.edu.coms309.sb4.Getit.Server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.iastate.edu.coms309.sb4.Getit.Server.entity.AttendingStudent;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.Course;
import com.iastate.edu.coms309.sb4.Getit.Server.entity.User;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.AttendingStudentRepository;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.CourseRepository;
import com.iastate.edu.coms309.sb4.Getit.Server.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@ServerEndpoint(value = "/attendance/{id}")
public class AttendanceWebSocket 
{
	private final Logger logger = LoggerFactory.getLogger(AttendanceWebSocket.class);
	
	//maps to find session or userName based on each other
	private static Map<Session, String> sessionNameMap = new Hashtable<>();
	private static Map<String, Session> nameSessionMap = new Hashtable<>();

	//connected user information
	int userId;
	String userName;
	
	//the attendance code being sent in and the corresponding course for the code
	public String code;
	public String courseName;
	public String courseCat;
	public int courseNumber = 0;
	
	//repositories to get information about the user and course and send to the attendingStudent repository to be recorded
	private static UserRepository usersRepo;
	private static CourseRepository courseRepo;
	private static AttendingStudentRepository attendingStudentsRepo;
	
	//initialize the repositories
	@Autowired
	public void UserRepository(UserRepository repo)
	{
		usersRepo = repo;
	}
	
	@Autowired
	public void AttendingStudentRepository(AttendingStudentRepository repo)
	{
		attendingStudentsRepo = repo;
	}
	
	@Autowired
	public void CourseRepository(CourseRepository repo)
	{
		courseRepo = repo;
	}
	
	/**
	 * opens websocket, saves user id and username and puts username and session in corresponding hashmaps
	 * @param session
	 * @param id
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("id") int id) {
		
		//find the user in the repo based on id
		User user = usersRepo.findById(id).get();
		
		userId = id;
		userName = user.getName();
		
		sessionNameMap.put(session, userName);
		nameSessionMap.put(userName, session);

		
		logger.info(userName + " is connected");
		
	}

	/**
	 * depending on the message received different actions are taken
	 * 
	 * clear: deletes the students in the attending students repo if the course name the student is attending is the same as the course name that is sent in
	 * 
	 * close: closes the websocket
	 * 
	 * submit attendance: checks if the sent in code is the correct attendance code for the sent in course and sends the correct response 
	 * and records the student in the attending student repo if the code is correct
	 * 
	 * @param session
	 * @param message
	 */
	
	@OnMessage
	public void onMessage(Session session, String message) {
		logger.info("Message Received: " + message);

		if(message.substring(0,5).equals("clear"))
		{
			courseName = message.substring(6);
			List<AttendingStudent> attendingStudents = attendingStudentsRepo.findAll();
			for(AttendingStudent student : attendingStudents)
			{
				if(student.courseName.equals(courseName))
				{
					attendingStudentsRepo.delete(student);
				}
			}
		}
		else if(message.equals("close"))
		{
			onClose(nameSessionMap.get(userName));
		}
		else if(message.substring(0,17).equals("submit attendance"))
		{
			
			logger.info("submiting attendance");
			
			splitAttendanceString(message);
			//find the correct course based on sent in course name
			Course course = courseRepo.findCourseByCategoryAndNumber(courseCat, courseNumber);
			 
			//test if the attendance code is correct check the professor is connected or send a message to client notifying it of an incorrect code
			if(course.testAttendCode(code))
			{
				
				//get professor name by course name
				String profName = courseRepo.findCourseByCategoryAndNumber(courseCat, courseNumber).getProfessorName();
				
				//check if a professor for the course exits, if it does not then attendance is not being taken
				boolean profExists = nameSessionMap.containsKey(profName);
				
				//if professor is connected
				if(profExists == true)
				{
					attendingStudentsRepo.save(new AttendingStudent(userName, course.getCategory() + " " + course.getNumber()));
					logger.info("student is present");
					
					//message the student they have been marked as present and send the student name to the professor
					sendMessage(nameSessionMap.get(userName), "You have been marked as present");
					sendMessage(nameSessionMap.get(profName), userName);
				}
				//professor is not connected
				else
				{
					logger.info("correct code but the prof is not online");
					sendMessage(nameSessionMap.get(userName), "Attendance is not being taken right now");
				}
			}
			else
			{
				logger.info("inccorect code");
				sendMessage(nameSessionMap.get(userName), "Attendance code is incorrect");
			}
		}
	}
	
	
	/**
	 * splits the sent in message to get the course category and course number
	 * @param attendanceString
	 */
	public void splitAttendanceString(String attendanceString)
	{
		String[] splitString = attendanceString.split(" ");
		
		code = splitString[2];
		if(splitString[4].equals("S"))
		{
			courseCat = "COM S";
			courseNumber = Integer.parseInt(splitString[5]);
		}
		else
		{
			courseCat = splitString[3];
			courseNumber = Integer.parseInt(splitString[4]);
		}
		
		courseName = courseCat + " " + courseNumber;
	}

	/**
	 * closes the websocket defined by the session and removes the username and session from corresponding hashmaps
	 * @param session
	 */
	@OnClose
	public void onClose(Session session) 
	{
		Session sessionToClose = nameSessionMap.get(userName);
		logger.info(userName + " disconnected");
		nameSessionMap.remove(userName);
		sessionNameMap.remove(sessionToClose);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		if (!throwable.getClass().getSimpleName().equals("EOFException")) {
			logger.debug("An error has occurred");
			throwable.printStackTrace();
		}
		
	}

	/**
	 * send message to a websocket defined by the session
	 * @param session
	 * @param message
	 */
	private void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
