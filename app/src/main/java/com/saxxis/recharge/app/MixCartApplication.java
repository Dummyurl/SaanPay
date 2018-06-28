package com.saxxis.recharge.app;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.saxxis.recharge.interfaces.NetworkListener;
import com.saxxis.recharge.receivers.NetworkChangeListener;

/**
 * Created by saxxis25 on 3/25/2017.
 */

public class MixCartApplication extends Application {

    static MixCartApplication mInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        setmInstance(this);

    }

    public static synchronized MixCartApplication getInstance() {
        return mInstance;
    }

    public void setmInstance(MixCartApplication mInstance) {
        MixCartApplication.mInstance = mInstance;
    }


    public void setConnectivityListener(NetworkListener listener) {
        NetworkChangeListener.networkListener = listener;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
