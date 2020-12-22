package com.iastate.edu.coms309.sb4.getit.client.screens.entry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.iastate.edu.coms309.sb4.getit.client.R;
import com.iastate.edu.coms309.sb4.getit.client.screens.course.CourseList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.iastate.edu.coms309.sb4.getit.client.R.id.edittext_confirm_password;

/* Create Account Class
 * This class hosts a page for the user to create an account and send to the server
 *
 * @author Emin Okic
 *
 */
public class CreateAccount extends AppCompatActivity {

    //Username inputted by user in the name field box
    private TextInputEditText textInputName;
    //Username inputted by user in the username field box
    private TextInputEditText textInputUsername;
    //Username inputted by user in the password field box
    private TextInputEditText textInputPassword;
    //Username inputted by user in the confirm password field box
    private TextInputEditText textInputConirmPassword;

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

    /* On Create Method
     * A method created by android studio that allows for access of the page
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        textInputName = findViewById(R.id.edittext_name);
        textInputUsername = findViewById(R.id.edittext_username);
        textInputPassword = findViewById(R.id.edittext_password);
        textInputConirmPassword = findViewById(edittext_confirm_password);

        //Configure back button
        Button backButton = (Button) findViewById(R.id.nav_back_create_account);
        BackButtonListener backListener = new BackButtonListener();
        backButton.setOnClickListener(backListener);

        Button registerButton = (Button) findViewById(R.id.button_register);
        CreateAccountButtonListener createAccountListener = new CreateAccountButtonListener();
        registerButton.setOnClickListener(createAccountListener);
    }

    /* Go Back Method
     *
     * Allows user to return to the login page if needed.
     *
     */
    private void goBack() {
        this.finish();
    }

    /* Back Button Listener Method
     *
     * When the back button is clicked, it will listen and return user to login page
     *
     */
    private class BackButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            goBack();
        }

    }

    /* Send Register Method
     *
     * This takes in the new user's input, sends to the server as JSON object
     * and then returns the result back to the client
     *
     * @param name - user's name
     * @param email - user's email
     * @param password - user's password
     *
     */
    private void sendRegister(String name, String email, String password) {

        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        String url = "http://coms-309-sb-4.cs.iastate.edu:8080/user/createAccount";
        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            handleServerResult(response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showMessage(error.getMessage());
                            Log.e("Create Account", error.toString());
                            showMessage(error.getMessage());
                        }
                    });

            queue.add(jsonObjectRequest);

        } catch (JSONException error) {
            Log.e("Create Account JsonBody", error.toString());
            error.printStackTrace();
        }
    }

    /* Handle Server Result Method
     *
     * This takes in the JSON object, and sends to the server as JSON object
     * and then shows the result of the request back to the client
     *
     * @param response
     *
     */
    public void handleServerResult(JSONObject response) {
        try {
            if (response.getBoolean("success")) {
                Intent intent = new Intent(this, CourseList.class);
                startActivity(intent);
            } else {
                showMessage(response.getString("result"));
            }
        } catch (JSONException error) {
            showMessage("parse error: " + error.getMessage());
        }

    }

    /* Show Message Method
     *
     * Displays toast for given duration of given text
     *
     * @param msg - the message to show
     *
     */
    public void showMessage(String msg) {
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }


    /* Create Account Button Listener Method
     *
     * When user selects register button, it will send a add account request to server
     * and then direct them to the course list page if username/password are valid
     *
     */
    private class CreateAccountButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (validateUsername() == true && validatePassword() == true) {
                doCreateAccount();
            }
        }

    }

    /* Do Create Account Method
     *
     * Sends user to the create account page when button is selected
     *
     */
    public void doCreateAccount() {
        sendRegister(textInputName.getEditableText().toString().trim(), textInputUsername.getEditableText().toString().trim(), textInputPassword.getEditableText().toString().trim());
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
        } else if (usernameInput.length() > 15) {
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
        String passConfirmInput = textInputConirmPassword.getEditableText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        } else if (passConfirmInput == passwordInput) {
            textInputPassword.setError("Passwords do not match!");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

}
