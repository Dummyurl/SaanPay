package com.saxxis.recharge.activities.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.saxxis.recharge.R;
import com.saxxis.recharge.app.MixCartApplication;
import com.saxxis.recharge.app.UserPref;
import com.saxxis.recharge.helpers.AppHelper;
import com.saxxis.recharge.interfaces.NetworkListener;

import butterknife.ButterKnife;

/**
 * Created by saxxis25 on 3/25/2017.
 */

public class SplashActivity extends AppCompatActivity implements NetworkListener{

//    @BindView(R.id.splash_layout)
//    View mView;

//    @BindView(R.id.splash_wv)
//    WebView wv;

    private UserPref mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        mUser = new UserPref(this);

//        assert wv != null;
//        wv.clearCache(false);
//        wv.getSettings().setLoadWithOverviewMode(true);
//        wv.getSettings().setUseWideViewPort(true);
//        wv.loadUrl("file:///android_asset/splash.gif");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                if(mUser.isLoggedIn()){
                    AppHelper.LaunchActivity(SplashActivity.this,MainActivity.class);
                    finish();
//                }else {
//                    AppHelper.LaunchActivity(SplashActivity.this, LoginMainActivity.class);
//                    finish();
//                }
            }
        },800);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MixCartApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnecting, boolean isConnected) {

//        if (!isConnecting && !isConnected) {
//            AppHelper.Snackbar(this, mView, getString(R.string.connection_is_not_available), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
//        } else if (isConnecting && isConnected) {
//            AppHelper.Snackbar(this, mView, getString(R.string.connection_is_available), AppConstants.MESSAGE_COLOR_SUCCESS, AppConstants.TEXT_COLOR);
//        } else {
//            AppHelper.Snackbar(this, mView, getString(R.string.waiting_for_network), AppConstants.MESSAGE_COLOR_WARNING, AppConstants.TEXT_COLOR);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
