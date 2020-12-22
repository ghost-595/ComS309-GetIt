package com.iastate.edu.coms309.sb4.getit.client;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.iastate.edu.coms309.sb4.getit.client.websockets.ProfessorWebsocket;
import com.iastate.edu.coms309.sb4.getit.client.websockets.StudentWebsocket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Server must be running for these tests otherwise it will get stuck in the beforeClass trying to connect the websockets and will not pass or fail
 */
public class AttendanceSocketTest {

    private static ProfessorWebsocket professorWebsocket;
    private static StudentWebsocket studentWebsocket;

    @BeforeClass
    public static void beforeClass() {
        Context context = ApplicationProvider.getApplicationContext();
        professorWebsocket = new ProfessorWebsocket(context, 28);
        studentWebsocket = new StudentWebsocket(context, 1);
    }

    @AfterClass
    public static void afterClass() {
        professorWebsocket.closeWebsocket();
        studentWebsocket.closeWebsocket();
    }

    @After
    public void after() {
        professorWebsocket.sendMessage("clear " + "CPRE 281");
    }

    /**
     * sends to the server the correct attendance code for Phil 101 expects the server to send "john" to the professor
     *
     * @throws InterruptedException
     */
    @Test
    public void professorReceivesStudentName() throws InterruptedException {
        studentWebsocket.sendMessage("submit attendance " + 1489 + " " + "CPRE 281");
        Thread.sleep(1000);
        assertEquals("john", professorWebsocket.onMessageReturnString);
    }

    /**
     * sends the incorrect attendance code to the server and expects the server to return "Attendance code is incorrect" to the student
     *
     * @throws InterruptedException
     */
    @Test
    public void studentMessageForWrongCode() throws InterruptedException {
        studentWebsocket.sendMessage("submit attendance " + 1234 + " " + "CPRE 281");
        Thread.sleep(1000);
        assertEquals("Attendance code is incorrect", studentWebsocket.onMessageReturnString);
    }

    /**
     * sends the correct attendance code to the server and expects the server to return "You have been marked as present" to the student
     *
     * @throws InterruptedException
     */
    @Test
    public void studentMessageForCorrectCode() throws InterruptedException {
        studentWebsocket.sendMessage("submit attendance " + 1489 + " " + "CPRE 281");
        Thread.sleep(1000);
        assertEquals("You have been marked as present", studentWebsocket.onMessageReturnString);
    }

}
