package space.collabify.android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.Endpoints;
import space.collabify.android.Json;
import space.collabify.android.R;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.fragments.SearchDetailsFragment;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Song;
import space.collabify.android.requests.PlaylistRequest;

// for json data from spotify search
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;



public class DetailedSearchActivity extends PrimaryViewActivity {

    private static final String TAG = DetailedSearchActivity.class.getSimpleName();

    private SearchDetailsFragment mSearchDetailsFragment;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_search);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = true;
        SHOW_LOGOUT = true;

        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mSearchDetailsFragment = new SearchDetailsFragment();
            mSearchDetailsFragment.setmParentActivity(this);
            transaction.replace(R.id.song_details_list_frame, mSearchDetailsFragment, TAG);
            transaction.commit();
        }else {
            // TODO: get search details fragment reference from savedInstanceState?
        }

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        this.query = query;

        handleQuery(query);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean handleQuery(String query) {

        new CallSpotifySearch().execute(query);

        return true;
    }

    private void onSpotifySearchComplete(final List<Song> songs) {

        mSearchDetailsFragment.populateSongList(songs);
    }

    public void setupAddDialog(final String songDescription, final Song song) {
        // prompt to add song
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_song_dialog_title));
        builder.setMessage(songDescription);
        builder.setPositiveButton(getString(R.string.add_song_dialog_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: send song to server
                        addSong(song);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(getString(R.string.add_song_dialog_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void addSong(Song song){
        // TODO make server call
        mAppManager.addSong(song, null);
    }


    private class CallSpotifySearch extends AsyncTask<String, Void, JSONArray> {

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

            for(int i = 0; i < jsonArray.length(); i++){

                try {

                    JSONObject track = jsonArray.getJSONObject(i);

                    String id = track.getString("id");

                    String title = track.getString("name");

                    JSONArray artists = track.getJSONArray("artists");

                    String artist = "";

                    if(artists != null && artists.length() >= 1){

                        artist = artists.getJSONObject(0).getString("name");

                        for(int j = 1; j < artists.length(); j++){

                            artist += ", " + artists.getJSONObject(j).getString("name");
                        }
                    }

                    JSONObject album = track.getJSONObject("album");

                    String albumName = album.getString("name");

                    String artUrl = album.getJSONArray("images").getJSONObject(0).getString("url");

                    songs.add(new Song(title, artist, albumName, 9999, id, artUrl, AppManager.getInstance().getUser().getId()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            onSpotifySearchComplete(songs);
        }
    }
}


/**
 * This file was born on March 11 at 14:01
 */
/*public class DetailedSearchActivity extends ListActivity {

    private String query;

    private List<Song> songs;

    private SearchDetailsListAdapter adapter;

    private SearchDetailsFragment mSearchDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_search);

        songs = new ArrayList<>();

        adapter = new SearchDetailsListAdapter(getApplicationContext(), songs);
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

    private void setupAddDialog(final String songDescription, final Song song) {
        // prompt to add song
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_song_dialog_title));
        builder.setMessage(songDescription);
        builder.setPositiveButton(getString(R.string.add_song_dialog_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: send song to server
                        addSong(song);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(getString(R.string.add_song_dialog_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void addSong(final Song song) {

        // TODO : call async to send data
    }

    *//**
     * Handles the display of events in a row
     *//*
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

            final Song song = getItem(position);
            TextView rowDesc = (TextView) customView.findViewById(R.id.song_row_description);
            ImageView albumArt = (ImageView) customView.findViewById(R.id.song_details_album_art);
            ImageButton addButton = (ImageButton) customView.findViewById(R.id.song_row_add);

            //set up the row elements
            String title = song.getTitle();
            title = title.substring(0, Math.min(title.length(), 30));
            String artist = song.getArtist();
            artist = artist.substring(0, Math.min(artist.length(), 30));

            final String newSongDescription = title + "\n(" + artist + ")";

            rowDesc.setText(newSongDescription);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setupAddDialog(newSongDescription, song);
                }
            });

            return customView;
        }
    }

    private class CallSpotifySearch extends AsyncTask<String, Void, JSONArray> {

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

            for(int i = 0; i < jsonArray.length(); i++){

                try {

                    JSONObject track = jsonArray.getJSONObject(i);

                    String id = track.getString("id");

                    String title = track.getString("name");

                    JSONArray artists = track.getJSONArray("artists");

                    String artist = "";

                    if(artists != null && artists.length() >= 1){

                        artist = artists.getJSONObject(0).getString("name");

                        for(int j = 1; j < artists.length(); j++){

                            artist += ", " + artists.getJSONObject(j).getString("name");
                        }
                    }

                    JSONObject album = track.getJSONObject("album");

                    String albumName = album.getString("name");

                    String artUrl = album.getJSONArray("images").getJSONObject(0).getString("url");

                    songs.add(new Song(title, artist, albumName, 9999, id, artUrl, AppManager.getInstance().getUser().getId()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            onSpotifySearchComplete(songs);
        }
    }


    private class addSongToServer extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... uri) {

            return null;
        }
    }
}*/



