package com.saxxis.recharge.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saxxis.recharge.R;
import com.saxxis.recharge.activities.leftmenu.HelpContactActivity;
import com.saxxis.recharge.helpers.AppHelper;
import com.saxxis.recharge.models.BanksList;
import com.saxxis.recharge.models.Order;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saxxis25 on 4/5/2017.
 */

public class BanksListAdapter extends RecyclerView.Adapter<BanksListAdapter.BankssHolder> {

    private Context mContext;
    private ArrayList<BanksList> mData;

    public BanksListAdapter(Context cont, ArrayList<BanksList> data){
        this.mContext = cont;
        this.mData = data;
    }

    @Override
    public BankssHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bank_item,parent,false);
        return new BankssHolder(view);
    }

    @Override
    public void onBindViewHolder(BankssHolder holder, int position) {
        BanksList banksList = mData.get(position);

        holder.bankNameTV.setText(banksList.getBank_name());

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class BankssHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.bankNameTV)
        TextView bankNameTV;

        public BankssHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
