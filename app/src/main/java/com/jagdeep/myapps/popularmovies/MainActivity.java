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
    JSONObject jsonObject;
    JSONObject[] jsonObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_grid_movies);

        gridview = (GridView) findViewById(R.id.gridview);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        /*  Access using themoviedbapi Library
            MovieTask m1 = new MovieTask();
            m1.execute(1);
        */



        if(checknet())
        {
            Tmdbclient c1 = new Tmdbclient(this);
            try {
                jsonObject = c1.execute(1).get();
                loadJsonData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }




    }

    private void loadUi() {
        gridview.setAdapter(new ImageAdapter(this, movie_backdrops));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

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
        });



    }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_most_popular) {

            Tmdbclient c1 = new Tmdbclient(this);
            try {
                jsonObject = c1.execute(1).get();
                loadJsonData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            /* Async task if loading from themoviedbapi Library
            MovieTask m1 = new MovieTask();
            m1.execute(1);*/

            return true;
        }

        if (id == R.id.action_highest_rating) {

            Tmdbclient c1 = new Tmdbclient(this);
            try {
                jsonObject = c1.execute(2).get();
                loadJsonData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


          /*  MovieTask m1 = new MovieTask();
            m1.execute(2);*/
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



// Async task using themoviedbapi Library ---------------------
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


    // Test Internet Connectivity on Application Launch

    public Boolean checknet()
    {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nf=cm.getActiveNetworkInfo();
        if(nf==null)
        {
            return false;
        }
        else{
            return true;
        }

    }

}
