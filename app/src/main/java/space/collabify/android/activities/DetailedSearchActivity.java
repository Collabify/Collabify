package space.collabify.android.activities;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.Json;
import space.collabify.android.R;
import space.collabify.android.models.Playlist;
import space.collabify.android.fragments.SearchDetailsFragment;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Song;

// for json data from spotify search
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;


public class DetailedSearchActivity extends PrimaryViewActivity {

    private static final String TAG = DetailedSearchActivity.class.getSimpleName();

    private SearchDetailsFragment mSearchDetailsFragment;

    private ProgressDialog progress;

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

        progress = ProgressDialog.show(this, "Performing Search", "Searching tracks...", true);

        mAppManager.getmSpotifyService().searchTracks(query, new afterSpotifySearch());

        return true;
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

                        dialog.cancel();

                        addSong(song);
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

    private void addSong(final Song song){
        progress = ProgressDialog.show(this, "Adding Song", "Adding song to playlist...", true);

        mAppManager.addSong(song, new afterAddSong());
    }

    private class afterAddSong implements CollabifyCallback<Playlist>{

        @Override
        public void success(Playlist playlist, Response response) {

            System.out.println("this is a test");
        }

        @Override
        public void failure(RetrofitError error) {

        }

        @Override
        public void exception(Exception e) {

        }
    }

    private class afterSpotifySearch extends SpotifyCallback<TracksPager>{

        @Override
        public void failure(SpotifyError spotifyError) {

        }

        @Override
        public void success(TracksPager tracksPager, Response response) {

            List<Track> tracks = tracksPager.tracks.items;

            List<Song> songs = new ArrayList<>();

            for(Track track : tracks){

                Song song = new Song(track.name, track.artists.toString(), track.album.name, 9999, track.id, track.uri, mAppManager.getUser().getId());
            }

            mSearchDetailsFragment.populateSongList(songs);

            progress.dismiss();
        }
    }
}