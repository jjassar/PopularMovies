package com.jagdeep.myapps.popularmovies;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by jagdeep on 12/07/15.
 * Load Movie details
 */
public class MovieDetals extends AppCompatActivity {


    String original_title,overview,vote_average,release_date,poster_path;

    TextView title, t1,t2,t3, t4;

    ImageView im1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_movie_detail);


        int id = getIntent().getExtras().getInt("id");
        original_title = getIntent().getExtras().getString("original_title");
        overview = getIntent().getExtras().getString("overview");
        vote_average = getIntent().getExtras().getString("vote_average");
        release_date = getIntent().getExtras().getString("release_date");
        poster_path = getIntent().getExtras().getString("poster_path");


        setupBar();
        initUi();


        title.setText(original_title);
        t1.setText(overview);
        t2.setText(vote_average);
        t3.setText(release_date);
        t4.setText("Rating - "+ vote_average + "/10");


        Picasso.with(this).load("http://image.tmdb.org/t/p/w185//" + poster_path).into(im1);


        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_LONG).show();



    }

    private void setupBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final ActionBar ab = getSupportActionBar();
        // ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Details");
    }


    private void initUi()
    {
        title = (TextView)findViewById(R.id.mtitle);

        t1 = (TextView)findViewById(R.id.overview);
        t2 = (TextView)findViewById(R.id.rating);
        t3 = (TextView)findViewById(R.id.date);
        t4 = (TextView)findViewById(R.id.textView);

        im1 = (ImageView)findViewById(R.id.imageView);



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
