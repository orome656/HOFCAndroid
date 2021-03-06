package com.hofc.hofc;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

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
    private static HOFCApplication app;

    public static HOFCApplication get() {
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        init();
        app = this;
    }

    @Override
    public void onTerminate() {
        requestQueue.cancelAll(true);
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void init() {
        requestQueue = Volley.newRequestQueue(this);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

}
