package com.saxxis.recharge.activities.recharge;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saxxis.recharge.R;
import com.saxxis.recharge.activities.CompletePaymentActivity;
import com.saxxis.recharge.activities.main.LoginActivity;
import com.saxxis.recharge.adapters.TabAdapter;
import com.saxxis.recharge.app.AppConstants;
import com.saxxis.recharge.app.MixCartApplication;
import com.saxxis.recharge.app.UserPref;
import com.saxxis.recharge.fragments.OperatorFragment;
import com.saxxis.recharge.helpers.AppHelper;
import com.saxxis.recharge.helpers.parsers.JSONParser;
import com.saxxis.recharge.models.Category;
import com.saxxis.recharge.models.Operator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saxxis.recharge.app.AppConstants.PLAN_REQUEST;

public class DataCardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.cl_dc)
    CoordinatorLayout clLayout;



    @BindView(R.id.rgv_prepostpaid)
    RadioGroup radiopGrp;

    @BindView(R.id.rb_prepaid)
    RadioButton radioButtonPrepaid;

    @BindView(R.id.rb_postpaid)
    RadioButton radiobuttonPostPaid;


    @BindView(R.id.dc_number)
    TextInputEditText etDcNum;
    @BindView(R.id.dc_operator)
    TextView dcOperator;
    @BindView(R.id.amount_dc)
    TextInputEditText etAmount;
    @BindView(R.id.proceed_to_pay_dc)
    TextView proceedPay;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.dc_browse_plans)
    TextView dcPlans;

    private String operator="empty",dcNumber,serviceid,amount=null,servicename;

    private UserPref mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_card);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUser = new UserPref(this);

        mToolbar.setTitle("Data Card");

        radioButtonPrepaid.setChecked(true);

        initializeTabs();
    }

    private void initializeTabs() {
        TabAdapter pagerAdapter = new TabAdapter(getSupportFragmentManager(),this);
//        mTabLayoutTop.setVisibility(View.VISIBLE);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
//        mTabLayout.getTabAt(0).getCustomView().setSelected(true);

    }


    @OnClick(R.id.dc_browse_plans)
    void browseplans(){
        if(dcPlans.isEnabled()) {
            ArrayList<Category> mPlans = AppConstants.getDTHPlans();
            Intent i = new Intent(DataCardActivity.this, BrowsePlansActivity.class);
            i.putExtra("serviceid", serviceid);
            i.putExtra("servicename", servicename);
            i.putParcelableArrayListExtra("plans", mPlans);
            startActivityForResult(i, PLAN_REQUEST);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DataCardActivity.this);
            builder.setTitle("Select Operator to view Plans");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @OnClick(R.id.dc_operator)
    void selectOperator(){
        AppHelper.showDialog(DataCardActivity.this,"Fetching Operators.......");
        StringRequest request=new StringRequest(Request.Method.GET, AppConstants.DC_OPERATOR_URL,
           new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                AppHelper.logout(DataCardActivity.this,response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("ok")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        final ArrayList<Operator> mList = JSONParser.getDTHOperators(jsonArray);
                        AppHelper.hideDialog();
                        showOperatorDialog(mList);
//                        List<CharSequence> charSequences = new ArrayList<>();
//                        for (int i = 0; i<mList.size();i++){
//                            charSequences.add(mList.get(i).getOptype_name());
//                        }
//                        final CharSequence[] charSequenceArray = charSequences.toArray(new
//                                CharSequence[charSequences.size()]);
//                        AppHelper.hideDialog();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(DataCardActivity.this);
//                        builder.setTitle("Make your selection");
//                        builder.setItems(charSequenceArray, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int item) {
//                                // Do something with the selection
//                                dcOperator.setText(charSequenceArray[item]);
//                                operator = mList.get(item).getOp_id();
//                                serviceid = mList.get(item).getId();
//                                dcPlans.setEnabled(true);
//                            }
//                        });
//                        AlertDialog alert = builder.create();
//                        alert.show();
                    }
                }catch (Exception ignored){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Cookie",mUser.getSessionId());
//                return headers;
//            }
//        };
        MixCartApplication.getInstance().addToRequestQueue(request);
    }

    @OnClick(R.id.proceed_to_pay_dc)
    void ProceedToPay(){

        dcNumber = etDcNum.getText().toString().trim();
        if (amount==null)
            amount = etAmount.getText().toString().trim();

        if(dcNumber.isEmpty()){
            AppHelper.Snackbar(this,clLayout,"Enter DataCard Number",AppConstants.MESSAGE_COLOR_ERROR,AppConstants.TEXT_COLOR);
        }else if(amount.isEmpty()){
            AppHelper.Snackbar(this,clLayout,"Enter the recharge amount",AppConstants.MESSAGE_COLOR_ERROR,AppConstants.TEXT_COLOR);
        }else if(operator.equals("empty")){
            AppHelper.Snackbar(this,clLayout,"Select Operator",AppConstants.MESSAGE_COLOR_ERROR,AppConstants.TEXT_COLOR);
        }else{

            if (!mUser.isLoggedIn()){
                Intent in=new Intent(DataCardActivity.this, LoginActivity.class);
                in.putExtra("finish","finish");
                startActivity(in);
            }
            else{
            Intent i = new Intent(DataCardActivity.this, CompletePaymentActivity.class);
            i.putExtra("operator", operator);
            i.putExtra("number", dcNumber);
            i.putExtra("amount", amount);
            i.putExtra("operatortype","3");
            i.putExtra("type","dc");
            startActivity(i);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == PLAN_REQUEST){
           if(resultCode == RESULT_OK) {
               amount = data.getStringExtra("amount");
               etAmount.setText(amount);
           }
        }
    }

    private void showOperatorDialog(ArrayList<Operator> mList){
        OperatorFragment op = OperatorFragment.newInstance(mList,"dc");
        op.show(getSupportFragmentManager(),"op");
    }


    public void updateOperator(Operator op) {
        servicename = op.getOptype_name();
        dcOperator.setText(op.getOptype_name());
        operator = op.getOp_id();
        serviceid = op.getId();
        dcPlans.setEnabled(true);
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

}
