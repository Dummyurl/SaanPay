package com.saxxis.recharge.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saxxis.recharge.R;

/**
 * A simple {@link Fragment} subclass.*/
public class ReqForPaymentFragment extends Fragment {

    public ReqForPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_req_for_payment, container, false);
    }


    public static ReqForPaymentFragment newInstance() {
        return new ReqForPaymentFragment();
    }

}
