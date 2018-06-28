package com.saxxis.recharge.activities.reservation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.saxxis.recharge.R;
import com.saxxis.recharge.adapters.MoviesListAdapter;
import com.saxxis.recharge.helpers.ui.RecvDecors;
import com.saxxis.recharge.models.MovieList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity {


    @BindView(R.id.tablayout_languages)
    TabLayout tabLayout;

    @BindView(R.id.recv_movies)
    RecyclerView recvMovies;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    MovieList movieList;
    String[] moviename={"Fidaa","Nenu Raju Nene Mantri","Darshakudu","Bahubali-2","ninnu Kori","Vaishakam","Nakshtram","Gowtham Nanda"};

    ArrayList<MovieList> movieArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setTitle("Movies");

        recvMovies.setHasFixedSize(true);
        recvMovies.setLayoutManager(new GridLayoutManager(this,2));
        recvMovies.addItemDecoration(new RecvDecors(this,R.dimen.job_listing_main_offset));

        movieArrayList=new ArrayList<MovieList>();
        for (int i = 0; i < 7; i++) {
            movieArrayList.add(new MovieList(moviename[i],"Telugu",i));
        }

        recvMovies.setAdapter(new MoviesListAdapter(MoviesActivity.this,movieArrayList));
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
