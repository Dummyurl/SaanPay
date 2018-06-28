package com.saxxis.recharge.activities.utlity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saxxis.recharge.R;
import com.saxxis.recharge.activities.CompletePaymentActivity;
import com.saxxis.recharge.activities.main.LoginActivity;
import com.saxxis.recharge.app.AppConstants;
import com.saxxis.recharge.app.MixCartApplication;
import com.saxxis.recharge.app.UserPref;
import com.saxxis.recharge.fragments.OperatorFragment;
import com.saxxis.recharge.helpers.AppHelper;
import com.saxxis.recharge.helpers.parsers.JSONParser;
import com.saxxis.recharge.models.Operator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ElectricityActivity extends AppCompatActivity {


    @BindView(R.id.cl_electricity)CoordinatorLayout clLayout;

    @BindView(R.id.toolbar)
    Toolbar electricitybilltoolbar;

    @BindView(R.id.txt_elec_eletyboard)
    TextView txtElectricityBoard;

    @BindView(R.id.elect_srvcno)
    TextInputEditText txtElectServiceNo;

    @BindView(R.id.elect_amount)
    TextInputEditText txtElectricityAmount;

    @BindView(R.id.elect_paybill)
    Button btnGetBill;

    String operator="empty";
    String serviceNo,amount="";


    private UserPref mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        ButterKnife.bind(this);

        setSupportActionBar(electricitybilltoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUser = new UserPref(this);

        electricitybilltoolbar.setTitle("Electricity");

    }

    @OnClick(R.id.txt_elec_eletyboard)
    void getBoards(){
        AppHelper.showDialog(ElectricityActivity.this,"Loading Please Wait...");
        String url = AppConstants.ALL_OPERATORS+"6";
        Log.e("reponse",url);
        StringRequest stringRequest=new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppHelper.logout(ElectricityActivity.this,response);
                        Log.e("reponse",response);
                        try {
                            AppHelper.hideDialog();
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("status").equals("ok")){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                final ArrayList<Operator> mList = JSONParser.getOperators(jsonArray);
                                AppHelper.hideDialog();
                                showOperatorDialog(mList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppHelper.hideDialog();
            }
        });

        MixCartApplication.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void showOperatorDialog(ArrayList<Operator> mList){
        OperatorFragment op = OperatorFragment.newInstance(mList,"electricity");
        op.show(getSupportFragmentManager(),"op");
    }

    public void updateOperator(Operator op) {
        txtElectricityBoard.setText(op.getOptype_name());
        operator = op.getOp_id();
    }


    @OnClick(R.id.elect_paybill)
    void getPayBill(){
        serviceNo = txtElectServiceNo.getText().toString();

//        if (amount==null)
        amount = txtElectricityAmount.getText().toString();

        if (operator.equals("empty")){
            AppHelper.Snackbar(this,clLayout,"Select Service Operator",AppConstants.MESSAGE_COLOR_ERROR,AppConstants.TEXT_COLOR);
        }else if (serviceNo.isEmpty()){
            AppHelper.Snackbar(this,clLayout,"Enter Service Number",AppConstants.MESSAGE_COLOR_ERROR,AppConstants.TEXT_COLOR);
        }else if (amount.isEmpty()||amount==""){
            AppHelper.Snackbar(this,clLayout,"Enter Amount On Bill",AppConstants.MESSAGE_COLOR_ERROR,AppConstants.TEXT_COLOR);
        }else {
            if (mUser.isLoggedIn()) {
                Intent i = new Intent(ElectricityActivity.this, CompletePaymentActivity.class);
                i.putExtra("operator", operator);
                i.putExtra("number", serviceNo);
                i.putExtra("amount", amount);
                i.putExtra("operatortype", "6");
                i.putExtra("type", "electricity");
                startActivity(i);
            }

            if (!mUser.isLoggedIn()){
                startActivity(new Intent(ElectricityActivity.this, LoginActivity.class).putExtra("finish","finish"));
            }
        }

    }

}
