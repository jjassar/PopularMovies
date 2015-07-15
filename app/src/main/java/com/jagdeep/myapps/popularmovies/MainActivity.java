/*

Class opens GridView and load images with the help of ImageAdapeter class using Picassio

Also tried to use themoviedbapi by holgerbrandl for testing only
Finally project is using Discover method only




*/



package com.jagdeep.myapps.popularmovies;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;


public class MainActivity extends AppCompatActivity {

    GridView gridview;
    TmdbMovies movies;
    List<MovieDb> allmovies;
    List<String> movie_backdrops;

    JSONArray jsonArray;
    JSONObject jsonObject = null;
    JSONObject[] jsonObjects;



    MyParcelable myParcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_grid_movies);

        gridview = (GridView) findViewById(R.id.gridview);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if(savedInstanceState == null || !savedInstanceState.containsKey("json"))
        {



           if(checknet())
             {
                 Toast.makeText(this,"Avail",Toast.LENGTH_LONG).show();

                 new ConnectTask(this).execute(1);

             }
           else
             {
                 Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
             }


        }
        else
        {
            myParcelable = savedInstanceState.getParcelable("json");
           // Toast.makeText(this, myParcelable.js,Toast.LENGTH_LONG).show();
            try {
                jsonObject = new JSONObject(myParcelable.js);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            loadJsonData();

        }



        /*  Access using themoviedbapi Library
            MovieTask m1 = new MovieTask();
            m1.execute(1);
        */



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable("json",myParcelable);

    }



// ------------ Result received from ConnectTask  --
    public void onRes(JSONObject j)
    {
        jsonObject = j;
        if(jsonObject == null)
        {
            Toast.makeText(this,"Connection TimeOut",Toast.LENGTH_LONG).show();
        }

        else {
            loadJsonData();
            myParcelable = new MyParcelable(jsonObject.toString());

        }
    }




    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    /* ----    Init Userinterface with Grid View -------*/
    private void loadUi() {
        gridview.setAdapter(new ImageAdapter(this, movie_backdrops));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                launchDetailsAct(position);

            }
        });



    }


/* ---- Launch New Activity to Show Details of movie -------*/
    private void launchDetailsAct(int position)
    {
        Intent in = new Intent(MainActivity.this, MovieDetals.class);
        Bundle b = new Bundle();

        try {
            b.putInt("id", jsonObjects[position].getInt("id"));
            b.putString("original_title", jsonObjects[position].getString("original_title"));
            b.putString("overview", jsonObjects[position].getString("overview"));
            b.putString("vote_average", jsonObjects[position].getString("vote_average"));
            b.putString("release_date", jsonObjects[position].getString("release_date"));
            b.putString("poster_path", jsonObjects[position].getString("poster_path"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        in.putExtras(b);
        startActivity(in);
    }


/* --------------- Fetch Data from JSON -----------------------*/
    private void loadJsonData()
    {
        try {
            jsonArray = jsonObject.getJSONArray("results");

            movie_backdrops = new ArrayList<>();
            if (jsonArray != null) {
                jsonObjects = new JSONObject[jsonArray.length()];
                String[] backdrop = new String[jsonArray.length()];
                String[] ids = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObjects[i] = jsonArray.getJSONObject(i);

                    backdrop[i] = jsonObjects[i].getString("poster_path");

                    movie_backdrops.add(backdrop[i]);

                    ids[i] = jsonObjects[i].getString("id");



                }

                loadUi();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

/* ----------- create menu --------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

/* ----------- option item selected -------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_most_popular) {

         //   executeTmdbClient(1);

            new ConnectTask(this).execute(1);

            /* Async task if loading from themoviedbapi Library
            MovieTask m1 = new MovieTask();
            m1.execute(1);*/

            return true;
        }

        if (id == R.id.action_highest_rating) {

         //   executeTmdbClient(2);

            new ConnectTask(this).execute(2);

          /*  MovieTask m1 = new MovieTask();
            m1.execute(2);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


/* ---------- Check internet connection --------------*/
    public Boolean checknet()
    {

        Boolean status = false;
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nf=cm.getActiveNetworkInfo();
       //  nf != null & nf.isConnectedOrConnecting();

        if(nf == null)
        {
            return false;
        }
        else {
            return true;
        }


    }






// Async task using themoviedbapi Library (Not Using Right Now ) ---------------------

/*
    private class MovieTask extends AsyncTask<Integer, List<MovieDb>, Boolean>
    {
        @Override
        protected Boolean doInBackground(Integer... integers) {

            movies = new TmdbApi("79d3946a3fdcf57a5175db2e962a4758").getMovies();

            if(integers[0] == 1)
            {


                MovieResultsPage mvr = movies.getPopularMovieList("en", 1);
                List<MovieDb> mdb = mvr.getResults();
                publishProgress(mdb);
            }
            if(integers[0] == 2)
            {

                MovieResultsPage mvr = movies.getTopRatedMovies("en", 1);
                List<MovieDb> mdb = mvr.getResults();
                publishProgress(mdb);

            }

            return true;

        }

        @Override
        protected void onProgressUpdate(List<MovieDb>... values) {
            super.onProgressUpdate(values);

            allmovies = values[0];

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            processData();

        }
    }

    private void processData() {

        List<String> posters = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();
        for(MovieDb movieDb : allmovies)
        {
            sb.append(movieDb.getPosterPath());
            sb.append("\n");

            posters.add(movieDb.getPosterPath());

        }

        Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_LONG).show();


        loadUi();

    }

    */


    // Test Internet Connectivity on Application Launch



}
