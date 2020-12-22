package com.iastate.edu.coms309.sb4.getit.client.volley;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ResponseHandler {
    /**
     * A method for handling the response for a volley request
     * @param response the JSONObject given by the server as a response
     * @return true if the response was valid and processed successfully; false otherwise
     */
    public boolean handleResponse (JSONObject response);

    /**
     * A method for handling the error given by a volley request
     * @param error the VolleyError to be passed
     */
    public void handleError (VolleyError error);
}
