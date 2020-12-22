package com.iastate.edu.coms309.sb4.getit.client;

import com.iastate.edu.coms309.sb4.getit.client.screens.entry.Onboard;
import com.iastate.edu.coms309.sb4.getit.client.state.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class LoginTest {

    private static JSONObject validLoginResponse;
    private static JSONObject invalidLoginResponse;

    @BeforeClass
    private static void makeDummyResponses () throws JSONException {
        validLoginResponse = new JSONObject ("success:true,response:\"\"");
        validLoginResponse.put ("success", true);
        JSONObject validResponseObj = new JSONObject ();
        validResponseObj.put ("email","validemail@validemail.com");
        validResponseObj.put ("name", "suctioncupman");
        validResponseObj.put ("id", 420);
        validResponseObj.put ("role", 69);
        validLoginResponse.put ("response", validResponseObj);
        invalidLoginResponse = new JSONObject ();
        invalidLoginResponse.put ("success", false);
        JSONObject invalidResponseObj = new JSONObject ();
        invalidResponseObj.put ("THIS_IS_AN", "INVALID_RESPONSE");
        invalidLoginResponse.put ("response", invalidResponseObj);
    }

    @Test
    public void testValidLoginResponse () {
        //Get onboard screen to test
        Onboard onboard = Mockito.spy (new Onboard());
        //Give it the constructed response
        boolean result = onboard.handleServerResult (validLoginResponse);
        //Aaaaand run some unit tests!
        assertEquals (result, true);
        assertEquals (User.isValid (), true);
        assertEquals (User.getId (), 420);
    }

    @Test
    public void testInvalidLoginResponse () {
        //Get onboard screen to test
        Onboard onboard = Mockito.spy (new Onboard());
        //Give it the constructed response
        boolean result = onboard.handleServerResult (invalidLoginResponse);
        //Aaaaand run some unit tests!
        assertEquals (result, false);
        assertEquals (User.isValid (), false);
    }
}