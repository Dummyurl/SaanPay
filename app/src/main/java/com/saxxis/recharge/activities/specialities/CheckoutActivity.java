package com.saxxis.recharge.activities.specialities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saxxis.recharge.R;
import com.saxxis.recharge.activities.CompletePaymentActivity;
import com.saxxis.recharge.activities.main.MainActivity;
import com.saxxis.recharge.adapters.CheckRecyclerViewAdapter;
import com.saxxis.recharge.app.ShoppingPref;
import com.saxxis.recharge.models.Product;
import com.saxxis.recharge.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = CheckoutActivity.class.getSimpleName();

    @BindView(R.id.checkout_list)
    RecyclerView checkRecyclerView;

    @BindView(R.id.sub_total)
    TextView subTotal;

    @BindView(R.id.cart_layout)
    LinearLayout cartlayout;

    @BindView(R.id.no_items)
    LinearLayout noItemsLayout;

    private double mSubTotal = 0;

    private ShoppingPref mShared;
    private GsonBuilder builder;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Over Cart");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CheckoutActivity.this);
        checkRecyclerView.setLayoutManager(linearLayoutManager);
        checkRecyclerView.setHasFixedSize(true);
        checkRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(CheckoutActivity.this));

        // get content of cart
        mShared = new ShoppingPref(CheckoutActivity.this);

        builder = new GsonBuilder();
        gson = builder.create();

        Product[] addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(), Product[].class);
        List<Product> productList = convertObjectArrayToListObject(addCartProducts);

        if (productList.size() == 0) {
            cartlayout.setVisibility(View.INVISIBLE);
            noItemsLayout.setVisibility(View.VISIBLE);
        } else {
            cartlayout.setVisibility(View.VISIBLE);
            noItemsLayout.setVisibility(View.INVISIBLE);
            CheckRecyclerViewAdapter mAdapter = new CheckRecyclerViewAdapter(CheckoutActivity.this, productList);
            checkRecyclerView.setAdapter(mAdapter);
        }


        mSubTotal = getTotalPrice(productList);
        setTotalAmount();


    }

    @OnClick(R.id.shopping)
    void ContinueShopping() {
        Intent shoppingIntent = new Intent(CheckoutActivity.this, MainActivity.class);
        shoppingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shoppingIntent);
    }

    @OnClick(R.id.checkout)
    void ContinueCheckout() {

        Product[] addCartProducts = gson.fromJson(mShared.retrieveProductFromCart(), Product[].class);
        List<Product> productList = convertObjectArrayToListObject(addCartProducts);
        System.out.println(getProductIds(productList));
        int calue = (int) mSubTotal;
        Intent paymentIntent = new Intent(CheckoutActivity.this, CompletePaymentActivity.class);
        paymentIntent.putExtra("operator", "");
        paymentIntent.putExtra("number", productList.get(0).getId());
        paymentIntent.putExtra("amount", String.valueOf(calue));
        paymentIntent.putExtra("type", "shopping");
        startActivity(paymentIntent);
        finish();
    }


    @OnClick(R.id.cont_shopp)
    void ContinueShopping1() {
        Intent shoppingIntent = new Intent(CheckoutActivity.this, MainActivity.class);
        shoppingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shoppingIntent);
    }


    private List<Product> convertObjectArrayToListObject(Product[] allProducts) {
        List<Product> mProduct = new ArrayList<Product>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    private int returnQuantityByProductName(String productName, List<Product> mProducts) {
        int quantityCount = 0;
        for (int i = 0; i < mProducts.size(); i++) {
            Product pObject = mProducts.get(i);
            if (pObject.getName().trim().equals(productName.trim())) {
                quantityCount++;
            }
        }
        return quantityCount;
    }

    private double getTotalPrice(List<Product> mProducts) {
        double totalCost = 0;
        for (int i = 0; i < mProducts.size(); i++) {
            Product pObject = mProducts.get(i);
            totalCost = totalCost + Double.valueOf(pObject.getPrice());
        }
        return totalCost;
    }


    private String getProductIds(List<Product> mProducts) {
        String ids = "";
        for (int i = 0; i < mProducts.size(); i++) {
            Product pObject = mProducts.get(i);
            if (ids.equals("")) {
                ids = pObject.getId();
            } else {
                ids = ids + "," + pObject.getId();
            }
        }
        return ids;
    }

    private void setTotalAmount() {
        subTotal.setText("Subtotal excluding tax and shipping: " + getResources().getString(R.string.Rs, String.valueOf(mSubTotal)));

    }

    public void RemoveAmount(List<Product> mProducts, int cartProductNumber) {
        if (cartProductNumber != 0) {
            mSubTotal = getTotalPrice(mProducts);
            setTotalAmount();
        } else {
            cartlayout.setVisibility(View.INVISIBLE);
            noItemsLayout.setVisibility(View.VISIBLE);
        }
    }

    public void AddAmount(String mProducts, int count) {

        mSubTotal = count * Double.valueOf(mProducts);
        setTotalAmount();
    }


    public void RemoveIndividualAmount(String price, int totalnumber) {
        mSubTotal = totalnumber * Double.valueOf(price);
        setTotalAmount();
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
