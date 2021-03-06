package com.saxxis.recharge.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.saxxis.recharge.R;
import com.saxxis.recharge.activities.payment.DetailedPaymentActivity;
import com.saxxis.recharge.adapters.WalletTranxAdapter;
import com.saxxis.recharge.app.AppConstants;
import com.saxxis.recharge.app.MixCartApplication;
import com.saxxis.recharge.app.UserPref;
import com.saxxis.recharge.helpers.AppHelper;
import com.saxxis.recharge.helpers.ItemClickSupport;
import com.saxxis.recharge.models.WalletTranxs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TranxListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranxListFragment extends Fragment {


    @BindView(R.id.recv_wallettranx)
    RecyclerView recvOrders;

    @BindView(R.id.trnxprogressbar)
    ProgressBar trnxProgressBar;

    private UserPref userPref;
    private ArrayList<WalletTranxs> mData;


    public TranxListFragment() {
        // Required empty public constructor
    }

    public static TranxListFragment newInstance() {
       return new TranxListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tranx_list, container, false);
        ButterKnife.bind(this,view);

        userPref = new UserPref(getActivity());
        mData = new ArrayList<WalletTranxs>();
        fetchOrders();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            fetchOrders();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void fetchOrders() {

        recvOrders.setHasFixedSize(true);
        recvOrders.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        String url = AppConstants.WALLET_TRNX+userPref.getUserId()+"&walletcatagory=0";
        Log.e("response",url);
        trnxProgressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppHelper.logout(getActivity(),response);
                        Log.e("response",response);
                        mData.clear();
                        trnxProgressBar.setVisibility(View.GONE);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("ok")){

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                int len = jsonArray.length();
                                for (int i=0;i<len;i++){
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    mData.add(new WalletTranxs(obj.getString("amount"),obj.getString("order_number"),
                                            obj.getString("wallet_description"),obj.getString("date"),
                                            "","",obj.getString("wallet_type"),obj.getString("status")));
                                }

                                recvOrders.setAdapter(new WalletTranxAdapter(getActivity(),mData));
                                ItemClickSupport.addTo(recvOrders).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                    @Override
                                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                        startActivity(new Intent(getActivity(),DetailedPaymentActivity.class)
                                                .putExtra("ordernumber",mData.get(position)));
                                    }
                                });
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
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Cookie",mPref.getSessionId());
//                return headers;
//            }
//        };

        MixCartApplication.getInstance().addToRequestQueue(request);
    }

}
