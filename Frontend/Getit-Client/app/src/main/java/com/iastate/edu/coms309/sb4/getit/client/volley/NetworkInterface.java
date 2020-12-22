package com.iastate.edu.coms309.sb4.getit.client.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkInterface {

    public static final String SERVER_DOMAIN = "http://coms-309-sb-4.cs.iastate.edu:8080";
    //public static final String SERVER_DOMAIN = "http://10.0.2.2:8080";

    public static boolean sendRequest (String path, JSONObject request, final ResponseHandler responseHandler) {
        String url = SERVER_DOMAIN + "/" + path;
        RequestQueue queue;
        if (responseHandler instanceof Context) {
            queue = Volley.newRequestQueue((Context)responseHandler);
        } else {
            //TODO figure out how to make a request without a context
            return false;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        responseHandler.handleResponse (response);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseHandler.handleError (error);
                    }
                });

        queue.add(jsonObjectRequest);
        return true;
    }
}
