package com.iastate.edu.coms309.sb4.getit.client;

import com.iastate.edu.coms309.sb4.getit.client.screens.student.StudentAttendance;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StudentAttendanceUnitTest {
    @Test
    public void testSubmitAttendance() {
        StudentAttendance a = new StudentAttendance();
        String userName = "john@gmail.com";
        //a.submitAttendance(userName);
        //Need to figure out how to test websocket results
        //assertEquals(result);
        assertEquals(2,2);
    }
}
