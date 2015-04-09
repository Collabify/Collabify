package space.collabify.collabify.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import space.collabify.collabify.Json;
import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.models.Role;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;

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

    private String query;

    private List<Song> songs;

    private SongDetailsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_search);

        songs = new ArrayList<>();

        adapter = new SongDetailsListAdapter(getApplicationContext(), songs);
        setListAdapter(adapter);

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        this.query = query;

        new CallSpotifySearch().execute(query);
    }

    private void onSpotifySearchComplete(List<Song> songs){

        adapter.clear();

        for (Song song : this.songs = songs) {

            adapter.add(song);
        }
    }

    /**
     * Handles the display of events in a row
     */
    private class SongDetailsListAdapter extends ArrayAdapter<Song> {
        private SongDetailsListAdapter(Context context, List<Song> objects) {
            super(context, R.layout.song_details_row, objects);
            songs = objects;
        }

        private SongDetailsListAdapter(Context context, Song[] songs) {
            super(context, R.layout.song_details_row, songs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.song_details_row, parent, false);

            Song song = getItem(position);
            TextView rowName = (TextView) customView.findViewById(R.id.song_row_title);
            TextView rowArtist = (TextView) customView.findViewById(R.id.song_row_artist);
            ImageButton addButton = (ImageButton) customView.findViewById(R.id.song_row_add);

            rowName.setText(song.getTitle());
            rowArtist.setText(song.getArtist());

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: have popup for adding song on click
                }
            });

            return customView;
        }
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



