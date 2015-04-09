package space.collabify.collabify.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import space.collabify.collabify.Json;
import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.models.Song;

// for json data from spotify search
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * This file was born on March 11 at 14:01
 */
public class DetailedSearchActivity extends ListActivity {

    private ArrayList<String> listItems = new ArrayList<>();

    private String query;

    private List<Song> songs;

    private  ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_search);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        this.query = query;

        new CallSpotifySearch().execute(query);
    }

    private void onSpotifySearchComplete(List<Song> songs){

        for (Song song : this.songs = songs) {


        }

        adapter.notifyDataSetChanged();
    }

    private class CallSpotifySearch extends AsyncTask<String, Void, JSONArray> {

        private Exception exception;


        protected JSONArray doInBackground(String... urls) {
            String[] splitQuery = urls[0].split("\\s+");

            String searchQuery = "https://api.spotify.com/v1/search?q=";

            if(splitQuery.length >= 1){

                searchQuery += splitQuery[0];
            }

            for(int i = 1; i < splitQuery.length; i++){

                searchQuery += "+" + splitQuery[i];
            }

            searchQuery += "&type=track";

            JSONObject jsonObject = Json.getJsonObject(searchQuery);

            JSONArray items = null;

            try {
                items = jsonObject.getJSONObject("tracks").getJSONArray("items");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return items;
        }

        protected void onPostExecute(JSONArray jsonArray) {

            List<Song> songs = new ArrayList<>();



            onSpotifySearchComplete(songs);
        }
    }
}



