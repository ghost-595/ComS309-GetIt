package com.iastate.edu.coms309.sb4.getit.client;

import android.content.Context;

import com.iastate.edu.coms309.sb4.getit.client.screens.entry.SliderAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SliderAdapterUnitTest {
    @Test
    public void getTotalSliderPages() {
        Context context = null;
        SliderAdapter sliderAdapter = new SliderAdapter(context);
        int result = sliderAdapter.getCount();
        assertEquals(3, result);
    }
}
