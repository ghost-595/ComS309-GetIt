package com.iastate.edu.coms309.sb4.getit.client;

import android.content.Intent;
import android.os.Build;

import com.iastate.edu.coms309.sb4.getit.client.screens.professor.ProfessorAttendance;
import com.iastate.edu.coms309.sb4.getit.client.screens.professor.ProfessorCourse;
import com.iastate.edu.coms309.sb4.getit.client.screens.student.StudentAttendance;
import com.iastate.edu.coms309.sb4.getit.client.screens.student.StudentCourse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class NavigationTests {
    @Test
    public void studentCourseToStudentAttendance() {
        StudentCourse studentCourse = Robolectric.setupActivity(StudentCourse.class);
        studentCourse.findViewById(R.id.nav_student_attendance).performClick();

        Intent expectedIntent = new Intent(studentCourse, StudentAttendance.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void professorCourseToProfessorAttendance() {
        ProfessorCourse professorCourse = Robolectric.setupActivity(ProfessorCourse.class);
        professorCourse.findViewById(R.id.nav_professor_attendance).performClick();

        Intent expectedIntent = new Intent(professorCourse, ProfessorAttendance.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
