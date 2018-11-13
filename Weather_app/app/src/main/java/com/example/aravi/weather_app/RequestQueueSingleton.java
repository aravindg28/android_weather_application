package com.example.aravi.weather_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*************************************************************************************************************************************************************
 Program: RequestQueueSingleton class for handling requests over the network
 Author: Aravind
 Date of creation: 25-Oct-2018
 Reference: Lab 6 material
 *************************************************************************************************************************************************************/

public class RequestQueueSingleton {

    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;


    private RequestQueueSingleton(Context context) {

        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton(context.getApplicationContext());

        }

        return mInstance;

    }


    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue (Request<T> req) {
        getRequestQueue().add(req);
    }
}

