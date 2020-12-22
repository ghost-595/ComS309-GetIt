package com.iastate.edu.coms309.sb4.getit.client.screens.student;

import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.model.Course;
import com.iastate.edu.coms309.sb4.getit.client.state.User;
import com.iastate.edu.coms309.sb4.getit.client.ui_elements.CanvasUi;
import com.iastate.edu.coms309.sb4.getit.client.ui_elements.NoteArea;
import com.iastate.edu.coms309.sb4.getit.client.volley.NetworkInterface;
import com.iastate.edu.coms309.sb4.getit.client.volley.ResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Notes extends AppCompatActivity implements ResponseHandler {

    private NoteArea noteArea;

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //Configure back button
        Button backButton = (Button) findViewById(R.id.nav_back_notes);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        //Set text on button
        TextView courseText = (TextView) findViewById (R.id.notes_course_display);
        courseText.setText (Course.getCurrentCourse ().getName ());

        //Get screen dimensions
        DisplayMetrics m = new DisplayMetrics ();
        getWindowManager ().getDefaultDisplay ().getMetrics (m);
        screenWidth = m.widthPixels;
        screenHeight = m.heightPixels;

        //Create view
        CanvasUi ui = new CanvasUi(this, m.widthPixels, m.widthPixels);
        noteArea = new NoteArea(ui);
        ui.setContent(noteArea);
        LinearLayout ly = findViewById(R.id.notes_container);
        ly.addView(ui);
        ui.invalidate();

        //Import notes for the current course, if any
        int userId = User.generateUserCourseId (Course.getCurrentCourse ());
        JSONObject request = new JSONObject ();
        try {
            request.put ("id", userId);
        } catch (JSONException e) {
            //How on earth did you get here?
            e.printStackTrace();
        }
        NetworkInterface.sendRequest("notes/find", request, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void goBack() {
        JSONObject notes = noteArea.exportNotes();
        JSONObject request = new JSONObject();
        try {
            request.put("id", User.generateUserCourseId(Course.getCurrentCourse()));
            request.put("note", notes);
        } catch (JSONException e) {
            //Do nothing
        }
        NetworkInterface.sendRequest("notes/save", request, this);
        this.finish();
    }

    /**
     * A method for handling the response for a volley request
     *
     * @param response the JSONObject given by the server as a response
     * @return true if the response was valid and processed successfully; false otherwise
     */
    @Override
    public boolean handleResponse(JSONObject response) {
        JSONObject obj = new JSONObject ();
        try {
            if (response.getBoolean ("success")) {
                String data = new String(Base64.decode(response.getJSONObject("result").getString("data"), Base64.DEFAULT));
                JSONObject result = response.getJSONObject ("result");
                obj.put ("course", result.getString ("course"));
                obj.put ("text", result.getString ("text"));
                obj.put ("date", result.getString ("date"));
                obj.put ("pages", result.getInt ("pages"));
                obj.put ("data", new JSONArray(data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        noteArea.openNotes(obj);
        return false;
    }

    /**
     * A method for handling the error given by a volley request
     *
     * @param error the VolleyError to be passed
     */
    @Override
    public void handleError(VolleyError error) {
        Log.wtf ("SERVER_RESPONSE", error.toString ());
    }

    private class BackButtonListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            goBack();
        }

    }
}
