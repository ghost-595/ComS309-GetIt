package com.iastate.edu.coms309.sb4.getit.client;

import android.content.Context;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;

import com.iastate.edu.coms309.sb4.getit.client.screens.recyclerView.RecylcerViewAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class RecyclerViewAdapterTest {

    @Test
    public void testGetItemCount() {
        List<String> courseList = new ArrayList<>();
        courseList.add("Add Course");
        courseList.add("Com S 101");

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        RecylcerViewAdapter recyclerViewAdapter = new RecylcerViewAdapter(context, courseList, R.layout.course_layout, R.id.textView);
        assertEquals(2, recyclerViewAdapter.getItemCount());
    }

}
