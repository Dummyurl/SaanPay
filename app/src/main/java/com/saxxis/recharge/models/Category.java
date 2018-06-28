package com.saxxis.recharge.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.saxxis.recharge.R;

import java.util.ArrayList;

/**
 * Created by saxxis25 on 3/30/2017.
 */

public class Category implements Parcelable{

    private int id;
    private int mIcon;
    private String mTitle;
    private int mColor;


    public Category() {

    }

    public Category(int i, int icon, String title) {
        this.id = i;
        this.mIcon = icon;
        this.mTitle = title;
    }

    public Category(int i, int icon, String title,int mColor) {
        this.id = i;
        this.mIcon = icon;
        this.mTitle = title;
        this.mColor= mColor;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        mIcon = in.readInt();
        mTitle = in.readString();
        mColor = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmIcon() {
        return mIcon;
    }

    public void setmIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }



    public static ArrayList<Category> getMoneyData(){
        ArrayList<Category> data=new ArrayList<>();
//        data.add(new Category(0, R.drawable.rupees_hand,"Add\nMoney"));
//       // data.add(new Category(1, R.drawable.rupee_fn,"Pay\nMoney"));
//        data.add(new Category(2, R.drawable.wallet,"Receive\nMoney"));
//       // data.add(new Category(3, R.drawable.money_trns,"Send\nMoney"));
//        data.add(new Category(4, R.drawable.rec_sen,"IMPS(TM)"));
//        data.add(new Category(5, R.drawable.mble_opt,"Scan & Pay"));
//        data.add(new Category(6, R.drawable.money_wlt,"Pass\nBook"));
//        data.add(new Category(7, R.drawable.home,"Pay\nStore"));
//        data.add(new Category(8, R.drawable.googlepay,"Google\nPlay"));

        data.add(new Category(0, R.drawable.ic_credit_pay,"Pay"));
        data.add(new Category(1, R.drawable.ic_add_rupee,"Add Money"));
        data.add(new Category(2, R.drawable.passbook_two,"Pass Book"));
//        data.add(new Category(3, R.drawable.wallet_two,"Wallet"));
        data.add(new Category(3, R.drawable.ic_deposit,"Deposit"));
        return data;

    }
    public static ArrayList<Category> getPayBills(){
        ArrayList<Category> data1=new ArrayList<>();
//        data1.add(new Category(0, R.drawable.prepaid,"Prepaid"));
//        data1.add(new Category(1, R.drawable.postpaid,"Postpaid"));
//        data1.add(new Category(2, R.drawable.dth,"DTH"));
//        data1.add(new Category(4, R.drawable.dc,"Data Card"));



//        data1.add(new Category(3, R.drawable.landline,"Landline"));
//        data1.add(new Category(5, R.drawable.pwr,"BroadBrand"));
//        data1.add(new Category(6, R.drawable.elctry,"Elecricity"));
//        data1.add(new Category(7, R.drawable.tap,"Water"));
//        data1.add(new Category(8, R.drawable.gas,"Gas"));


        data1.add(new Category(0, R.drawable.ic_mobile,"Mobile Prepaid"));
        data1.add(new Category(1, R.drawable.ic_mobile,"Mobile Postpaid"));
        data1.add(new Category(2, R.drawable.ic_dth,"DTH"));
        data1.add(new Category(3, R.drawable.ic_data_card,"Data Card"));


        return data1;

    }
    public static ArrayList<Category> getBookOnline(){
        ArrayList<Category> data = new ArrayList<>();
//        data.add(new Category(0, R.drawable.flight,"Flight\nTickets"));
//        data.add(new Category(1, R.drawable.train,"Train\nTickets"));
//        data.add(new Category(2, R.drawable.bus,"Bus\nTickets"));
//        data.add(new Category(3, R.drawable.vdeo,"Movie\nTickets"));
//        data.add(new Category(4, R.drawable.car,"Book a\nCab"));
//        data.add(new Category(5, R.drawable.petrol,"Petrol"));
//        data.add(new Category(6, R.drawable.fmly,"Insurance"));
//        data.add(new Category(7, R.drawable.stars,"Hotels"));
//        data.add(new Category(8, R.drawable.donate,"Donate"));

        data.add(new Category(0, R.drawable.ic_electric, "Electricity"));
        data.add(new Category(1, R.drawable.ic_gas, "Gas"));
        data.add(new Category(2, R.drawable.ic_insurance, "Insurance"));
        data.add(new Category(3, R.drawable.ic_tap, "Water"));

        return data;
    }

    public static ArrayList<Category> getReservationData(){
        ArrayList<Category> data = new ArrayList<>();
        data.add(new Category(0, R.drawable.ic_bus,"Bus"));
        data.add(new Category(1, R.drawable.ic_flight,"Flight"));
        data.add(new Category(2, R.drawable.ic_holiday,"Holiday"));
        data.add(new Category(3, R.drawable.ic_hotel,"Hotels"));
        data.add(new Category(4, R.drawable.vdeo,"Movie"));
//        data.add(new Category(1, R.drawable.train,"Train\nTickets"));
//        data.add(new Category(4, R.drawable.car,"Book a\nCab"));
//        data.add(new Category(5, R.drawable.petrol,"Petrol"));
//        data.add(new Category(6, R.drawable.fmly,"Insurance"));
//        data.add(new Category(8, R.drawable.donate,"Donate"));

        return data;
    }

    public static ArrayList<Category> getSpecialityStore(){
        ArrayList<Category> data=new ArrayList<>();
//        data.add(new Category(17, R.drawable.fish,"Sea Foods"));
//        data.add(new Category(21, R.drawable.chicken,"Chicken"));
//        data.add(new Category(22, R.drawable.meat,"Meat"));
//        data.add(new Category(23, R.drawable.herbl_prdcts,"Herbal Products"));
//        data.add(new Category(24, R.drawable.sweets,"Sweets"));
//        data.add(new Category(25, R.drawable.bouquet,"Boutique"));
//        data.add(new Category(26, R.drawable.cake,"Cakes"));

        return data;

    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mIcon);
        dest.writeString(mTitle);
        dest.writeInt(mColor);
    }
}
