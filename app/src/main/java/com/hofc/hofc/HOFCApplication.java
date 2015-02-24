package com.hofc.hofc;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Application Class.
 *  - Init Volley Queue
 *  - Cancel Queue on close
 * Created by maladota on 13/02/2015.
 */
public class HOFCApplication extends Application {
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public void onTerminate() {
        requestQueue.cancelAll(true);
        super.onTerminate();
    }

    private void init() {
        requestQueue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

}
