package com.jagdeep.myapps.popularmovies;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by jagdeep on 15/07/15.
 */
public class ConnectTask extends AsyncTask<Integer, String, JSONObject> {


    MainActivity act;

    public ConnectTask(MainActivity a)
    {
        act = a;
    }


    @Override
    protected JSONObject doInBackground(Integer... integers) {

        final int CONN_WAIT_TIME = 3000;
        final int CONN_DATA_WAIT_TIME = 3000;



        String line = "";

        JSONObject jsonObject = null;

        StringBuilder stringBuilder = new StringBuilder();

        String site = "";
        if(integers[0] == 1) {
            site = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=79d3946a3fdcf57a5175db2e962a4758";
        }
        else
        {

            site = "http://api.themoviedb.org/3/discover/movie?vote_count.gte=200&sort_by=vote_average.desc&api_key=79d3946a3fdcf57a5175db2e962a4758";


        }
        try{
            URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            connection.setConnectTimeout(CONN_WAIT_TIME);

            connection.setReadTimeout(CONN_DATA_WAIT_TIME);

            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line);
            }

            jsonObject = new JSONObject(stringBuilder.toString());

        }
        catch (SocketTimeoutException e)
        {

            e.printStackTrace();
            return null;
        }
        catch (ConnectException e) {
            // writing exception to log
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }



        return jsonObject;


    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        act.onRes(jsonObject);

    }
}