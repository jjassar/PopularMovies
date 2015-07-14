/*
This class uses themoviedbapi by holgerbrandl
Used for testing only
Finally project is using Discover method only

Class uses HTTPUrlConnection to communicate with server using async task
It returns JSONObject containing information received from Server


*/
package com.jagdeep.myapps.popularmovies;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;



public class Tmdbclient extends AsyncTask<Integer, String, JSONObject>{
	
	Context con;
	

	public Tmdbclient(Context c)

	{

	con=c;

	}

	@Override
	protected JSONObject doInBackground(Integer... integers) {


		JSONObject jsonObject = null;

		try {
			jsonObject =  loadPopularMovies(integers[0]);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;

	}


	

	
	
	
	
private JSONObject loadPopularMovies(int req) throws JSONException {
		
String line="";

	JSONObject jsonObject = null;

	StringBuilder stringBuilder = new StringBuilder();

	String site = "";
	if(req == 1) {
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

		connection.connect();

		InputStream inputStream = connection.getInputStream();

		BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

		while ((line = rd.readLine()) != null) {
			stringBuilder.append(line);
		}

		jsonObject = new JSONObject(stringBuilder.toString());

	} catch (IOException e) {
		// writing exception to log
		e.printStackTrace();
	}
		return jsonObject;
	}



}








