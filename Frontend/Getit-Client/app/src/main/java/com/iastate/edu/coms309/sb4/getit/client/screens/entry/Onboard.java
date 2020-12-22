package com.iastate.edu.coms309.sb4.getit.client.screens.entry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.screens.course.CourseList;
import com.iastate.edu.coms309.sb4.getit.client.state.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.iastate.edu.coms309.sb4.getit.client.R.id;
import static com.iastate.edu.coms309.sb4.getit.client.R.layout;

public class Onboard extends AppCompatActivity {

    /* Password Pattern
     * A variable defined as the requirements for creating a password.
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    //Username inputted by user in the onboard username field box.
    private TextInputEditText textInputUsername;
    //Password inputted by user in the onboard password field box.
    private TextInputEditText textInputPassword;

    private JSONObject userData = null;

    /* On Create Method
     * A method created by android studio that allows for access of the page
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        LoginButtonListener loginListener = new LoginButtonListener();
        CreateAccountButtonListener createAccountListener = new CreateAccountButtonListener();

        Button registerButton = findViewById(id.button_register);
        Button loginButton = findViewById(id.button_login);

        textInputUsername = findViewById(id.edittext_username);
        textInputPassword = findViewById(id.edittext_password);


        loginButton.setOnClickListener(loginListener);
        registerButton.setOnClickListener(createAccountListener);

    }

    /* On Create Options Menu
     * Android Studio provided method to highlight items selected
     *
     * @param menu
     * @return true;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* On Options Item Selected
     * Android Studio provided method to handle action bar item clicks
     *
     * @param item
     *
     * @return the boolean result of the selected item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Handle Server Result Method
     *
     * This takes in the JSON object, and sends to the server as JSON object
     * and then shows the result of the request back to the client
     *
     * @param response the response received from the server
     *
     *
     */
    public boolean handleServerResult(JSONObject response) {

        if (userData != null) {
            User.importData(userData);
        }
        try {
            if (response.getBoolean("success")) {
                Intent intent = new Intent(this, CourseList.class);
                startActivity(intent);
                return true;
            } else {
                showMessage(response.getString("result"));
                return false;
            }
        } catch (JSONException error) {
            Log.e("Login Json Response", error.toString());
            error.printStackTrace();
            return false;
        }

    }

    /* Show Message Method
     *
     * Displays toast for given duration of given text
     *
     * @param msg - the message to show
     *
     */
    private void showMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    //Methods for navigation

    /* Do Login Method

     * sends the login request from client to server
     *
     */
    private void doLogin() {
        String email = textInputUsername.getEditableText().toString().trim();
        String password = textInputPassword.getEditableText().toString().trim();

        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/user/login";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                userData = response.getJSONObject("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            handleServerResult(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showMessage(error.toString());
                        }
                    });

            queue.add(jsonObjectRequest);

        } catch (JSONException error) {
            showMessage("parse error");
        }
    }

    /* Do Create Account Method
     *
     * Sends user to the create account page when button is selected
     *
     */
    private void doCreateAccount() {
        Intent intent = new Intent(this, GettingStarted.class);
        startActivity(intent);
    }

    /* Login Button Listener Method
     *
     * When login button is clicked on the view,
     * it checks to see if username and password are valid and does the login
     *
     */
    private class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (validateUsername() && validatePassword()) {
                doLogin();
            }
        }

    }

    /* Validate Username Method
     *
     * Checks to see if user's input for the username field is correct
     *
     * @return true if username meets credentials, otherwise false
     *
     */
    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditableText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            textInputUsername.setError("Username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    /* Validate Password Method
     *
     * Checks to see if user's input for the password field is correct
     *
     * @return true if password meets credentials, otherwise false
     *
     */
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditableText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    /* Create Account Button Listener Method
     *
     * When user selects register button, it will send them to the create account page
     *
     */
    private class CreateAccountButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            doCreateAccount();
        }

    }

}
