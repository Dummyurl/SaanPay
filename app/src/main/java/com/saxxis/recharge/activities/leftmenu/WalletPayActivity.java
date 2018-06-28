package com.saxxis.recharge.activities.leftmenu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saxxis.recharge.R;
import com.saxxis.recharge.activities.main.MainActivity;
import com.saxxis.recharge.activities.payment.KYCUploadActivity;
import com.saxxis.recharge.app.AppConstants;
import com.saxxis.recharge.app.MixCartApplication;
import com.saxxis.recharge.app.UserPref;
import com.saxxis.recharge.helpers.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletPayActivity extends AppCompatActivity {


    @BindView(R.id.txt_walletbalance)TextView txtAvailBalance;

    @BindView(R.id.txtpaid_mobileno)TextInputEditText txtPayMobile;
    @BindView(R.id.txtpaid_amount)TextInputEditText txtPayAmount;
    @BindView(R.id.txtpaid_descripetion)TextInputEditText txtPayDescript;
    @BindView(R.id.payamount)TextView txtViewAmount;

    @BindView(R.id.toolbar)Toolbar toolbar;

    private UserPref userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_pay);
        ButterKnife.bind(this);
        userPref=new UserPref(WalletPayActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Pay");

        txtAvailBalance.setText(getResources().getString(R.string.walletbalanceout)+userPref.getWalletAmount());
        refreshWallet();

    }


    @OnClick(R.id.payamount)
    void onPayAmount(){

        String mobile=txtPayMobile.getText().toString();
        String amount=txtPayAmount.getText().toString();
        String description=txtPayDescript.getText().toString();

        if (!validate(mobile,amount,description)) {
            checkLimit(amount);
            /*String url=AppConstants.WALLETTOWALLET_TRSF +userPref.getUserId()+"&mobile="+
                    mobile.replaceAll(" ","%20")+"&amount="+amount.replaceAll(" ","%20")+
                    "&description="+description.replaceAll(" ","%20");
                //&userid=1040&mobile=9052001111&amount=10&description=Syed
            AppHelper.showDialog(WalletPayActivity.this, "Loading Please Wait...");
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                AppHelper.hideDialog();
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("ok")) {
                                    String messageObject = jsonObject.getString("message");
                                    new AlertDialog.Builder(WalletPayActivity.this)
                                            .setMessage(messageObject)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    Intent i = new Intent(WalletPayActivity.this, MainActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i);
                                                }
                                            })
                                            .create().show();
                                }
                                if (jsonObject.getString("status").equals("ko")) {
                                    String messageObject = jsonObject.getString("message");
                                    new AlertDialog.Builder(WalletPayActivity.this)
                                            .setMessage(messageObject)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                AppHelper.hideDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MixCartApplication.getInstance().addToRequestQueue(stringRequest);*/
        }
    }


    private boolean validate(String mobile, String amount,String description) {
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        if (mobile.isEmpty()){
            AppHelper.Snackbar(getApplicationContext(), txtAvailBalance, "Enter Mobile Number", AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
            return true;
        }

        if (amount.isEmpty()){
            AppHelper.Snackbar(getApplicationContext(), txtAvailBalance, "Enter Amount", AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
            return true;
        }

        if (!amount.isEmpty()){
            if (Integer.parseInt(amount)>userPref.getWalletAmount()){
                AppHelper.Snackbar(getApplicationContext(), txtAvailBalance, "Please Enter Sufficient Amount to Transfer", AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);

                return true;
            }
            return false;
        }

        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshWallet() {
//        mProgress.setVisibility(View.VISIBLE);
        Log.e("response",AppConstants.WALLET_URL+"&userid="+userPref.getUserId());
        StringRequest request=new StringRequest(Request.Method.GET, AppConstants.WALLET_URL+
                "&userid="+userPref.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
//                    mProgress.setVisibility(View.GONE);
                            if (status.equals("ok")){
                                JSONObject dataobject=jsonObject.getJSONObject("data");
                                userPref.setWalletAmount(Float.parseFloat(dataobject.getString("walletamount")));
                                txtAvailBalance.setText(getResources().getString(R.string.walletbalanceout)+dataobject.getString("walletamount"));
                            }
                        }catch (Exception ignored){

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MixCartApplication.getInstance().addToRequestQueue(request);
    }
    public void checkLimit(final String finalmoney){
        String url = AppConstants.LIMIT_CHECK+userPref.getUserId();

        AppHelper.showDialog(WalletPayActivity.this,"Checking transaction limit. Please Wait...");
        StringRequest str = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                AppHelper.logout(WalletPayActivity.this,s);
                try{
                    AppHelper.hideDialog();
                    JSONObject resp = new JSONObject(s);
                    String status = resp.getString("status");
                    if(status.equals("ok")){
                        final String usertype = resp.getString("usertype");
                        String atomwallet = resp.getString("atomwallet");
                        String walletamt = resp.getString("walletamt");
                        String atomwalletremainingamount = resp.getString("atomwalletremainingamount");
                        String remainingwalletamount = resp.getString("remainingwalletamount");
                        int remainingwalletamountInt = 0;
                        if(remainingwalletamount!=null){
                            remainingwalletamountInt = Integer.parseInt(remainingwalletamount);
                        }
                        if(Integer.parseInt(finalmoney)<=remainingwalletamountInt){
                            submitPayment();
                        }else{
                            new AlertDialog.Builder(WalletPayActivity.this)
                                    .setMessage("Available limit is "+remainingwalletamountInt+" Please re enter amount")
                                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if(usertype.equals("NonkycUser")){
                                                showKYCAlert();
                                            }

                                        }
                                    }).show();
                        }

                    }
                }catch (Exception e){
                    AppHelper.hideDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppHelper.hideDialog();
            }
        });

        str.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MixCartApplication.getInstance().addToRequestQueue(str);

    }
    public void submitPayment(){
        String mobile=txtPayMobile.getText().toString();
        String amount=txtPayAmount.getText().toString();
        String description=txtPayDescript.getText().toString();

        String url=AppConstants.WALLETTOWALLET_TRSF +userPref.getUserId()+"&mobile="+
                mobile.replaceAll(" ","%20")+"&amount="+amount.replaceAll(" ","%20")+
                "&description="+description.replaceAll(" ","%20");
        //&userid=1040&mobile=9052001111&amount=10&description=Syed
        AppHelper.showDialog(WalletPayActivity.this, "Loading Please Wait...");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppHelper.logout(WalletPayActivity.this,response);
                        try {
                            AppHelper.hideDialog();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("ok")) {
                                String messageObject = jsonObject.getString("message");
                                new AlertDialog.Builder(WalletPayActivity.this)
                                        .setMessage(messageObject)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent i = new Intent(WalletPayActivity.this, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                            }
                                        })
                                        .create().show();
                            }
                            /*if (jsonObject.getString("status").equals("ko")) {
                                String messageObject = jsonObject.getString("message");
                                new AlertDialog.Builder(WalletPayActivity.this)
                                        .setMessage(messageObject)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .create().show();
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AppHelper.hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MixCartApplication.getInstance().addToRequestQueue(stringRequest);
    }
    public void showKYCAlert(){
        new AlertDialog.Builder(WalletPayActivity.this)
                .setMessage("Verify KYC to increase limit")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(WalletPayActivity.this, KYCUploadActivity.class));
                    }
                }).show();
    }
}
