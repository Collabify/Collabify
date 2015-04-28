package space.collabify.android.fragments;

import android.app.AlertDialog;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.activities.PrimaryViewActivity;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;


/**
 * This file was born on March 11, at 15:53
 */
public class DjTracksFragment extends SwipeRefreshListFragment {

    protected PrimaryViewActivity mParentActivity;
    protected DjPlaylistsListAdapter mDjPlaylistsListAdapter;
    protected DjTracksListAdapter mDjTracksListAdapter;
    protected ImageButton backButton;
    protected TextView djHeaderText;

    private String currentPlaylistId = "";
    private String currentPlaylistOwner = "";
    private boolean displayingTracks = false;

    private ProgressDialog progress;

    private List<Playlist> currentPlaylists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());

        mSwipeRefreshLayout.addView(inflater.inflate(R.layout.fragment_dj_tracks, container, false));

        return mSwipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParentActivity = (PrimaryViewActivity)getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Playlist> emptyPlaylist = new ArrayList<>();
        List<Song> emptySonglist = new ArrayList<>();
        User user = mParentActivity.getCurrentUser();
        mDjPlaylistsListAdapter = new DjPlaylistsListAdapter(mParentActivity.getApplicationContext(), emptyPlaylist, mParentActivity.getCurrentUser(), this);
        mDjTracksListAdapter = new DjTracksListAdapter(mParentActivity.getApplicationContext(), emptySonglist, mParentActivity.getCurrentUser(), this);
        setListAdapter(mDjPlaylistsListAdapter);

        djHeaderText = (TextView) view.findViewById(R.id.dj_tracks_text);
        backButton = (ImageButton) view.findViewById(R.id.djBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.setClickable(false);
                backButton.setVisibility(View.INVISIBLE);

                djHeaderText.setText("DJ Playlists");

                // GO BACK TO PLAYLIST VIEW HERE
                setListAdapter(mDjPlaylistsListAdapter);
                displayingTracks = false;
            }
        });

        //set action for pull down refreshes
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });

        mParentActivity.getAppManager().getSpotifyService().getPlaylists(mParentActivity.getAppManager().getEvent().getEventId(), new populatePlaylistList());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    private void initiateRefresh(){

        setRefreshing(true);

        if(displayingTracks){
            populateListWithTracks(currentPlaylistId, currentPlaylistOwner);
        }
        else{

            progress = ProgressDialog.show(this.getmParentActivity(), "Populating DJ Playlists", "Fetching playlists...", true);

            mParentActivity.getAppManager().getSpotifyService().getPlaylists(mParentActivity.getAppManager().getEvent().getEventId(), new populatePlaylistList());
        }
    }

    private class populatePlaylistList extends SpotifyCallback<kaaes.spotify.webapi.android.models.Pager<kaaes.spotify.webapi.android.models.Playlist>>{
        @Override
        public void failure(SpotifyError spotifyError) {
            mParentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if(isRefreshing()) {

                        progress.dismiss();
                        setRefreshing(false);
                    }
                    Toast.makeText(mParentActivity.getBaseContext(), "Error populating list with dj playlists", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void success(final Pager<kaaes.spotify.webapi.android.models.Playlist> playlistPager, Response response) {

            mParentActivity.runOnUiThread(new Runnable() {
                public void run() {

                    mDjPlaylistsListAdapter.clear();

                    List<kaaes.spotify.webapi.android.models.Playlist> playlists = playlistPager.items;

                    if(isRefreshing()) {

                        setRefreshing(false);
                        progress.dismiss();

                        if (playlists.isEmpty()) {
                            Toast.makeText(mParentActivity.getBaseContext(), "No dj playlists found", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    for(kaaes.spotify.webapi.android.models.Playlist playlist : playlists){

                        String artUrl = "";

                        if(playlist.images.size() >= 3) {
                            artUrl = playlist.images.get(2).url;
                        } else if (playlist.images.size() > 0) {
                            artUrl = playlist.images.get(0).url;
                        }

                        Playlist newPlaylist = new Playlist(playlist.name, playlist.id, artUrl, new ArrayList<Song>());
                        newPlaylist.setOwner(playlist.owner.id);

                        currentPlaylists.add(newPlaylist);
                        mDjPlaylistsListAdapter.add(newPlaylist);
                    }

                }
            });
        }
    }


    public void populateListWithTracks(String playlistId, String ownerId){

        currentPlaylistId = playlistId;
        currentPlaylistOwner = ownerId;

        progress = ProgressDialog.show(this.getmParentActivity(), "Populating DJ Tracks", "Fetching tracks...", true);

        mParentActivity.getAppManager().getSpotifyService().getPlaylistTracks(ownerId, playlistId, new afterFetchTracks());
    }

    private class afterFetchTracks extends SpotifyCallback<kaaes.spotify.webapi.android.models.Pager<kaaes.spotify.webapi.android.models.PlaylistTrack>>{

        @Override
        public void failure(SpotifyError spotifyError) {
            mParentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    setRefreshing(false);
                    currentPlaylistId = "";
                    progress.dismiss();
                    Toast.makeText(mParentActivity.getBaseContext(), "Error populating list with dj tracks", Toast.LENGTH_LONG).show();
                }
            });
        }


        @Override
        public void success(final Pager<kaaes.spotify.webapi.android.models.PlaylistTrack> playlistTrackPager, Response response) {

            mParentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    setRefreshing(false);
                    // TODO: switch out adapters
                    List<kaaes.spotify.webapi.android.models.PlaylistTrack> playlistTracks = playlistTrackPager.items;

                    if (playlistTracks.isEmpty()) {

                        progress.dismiss();
                        Toast.makeText(mParentActivity.getBaseContext(), "No tracks found", Toast.LENGTH_LONG).show();
                        return;
                    }

                    mDjTracksListAdapter.clear();

                    setListAdapter(mDjTracksListAdapter);

                    for (kaaes.spotify.webapi.android.models.PlaylistTrack playlistTrack : playlistTracks) {

                        Track track = playlistTrack.track;

                        String url = "";

                        if(track.album.images.size() >=3){
                            url = track.album.images.get(2).url;
                        }

                        String artists = track.artists.get(0).name;
                        if (track.artists.size() > 1) {
                            for (int i = 1; i < track.artists.size() && i < 3; i++) {
                                artists += ", " + track.artists.get(i).name;
                            }
                        }

                        Song newSong = new Song(track.name, artists, track.album.name, 9999, track.id, url, mParentActivity.getAppManager().getUser().getId());

                        mDjTracksListAdapter.add(newSong);
                    }

                    displayingTracks = true;
                    djHeaderText.setText("DJ Tracks");
                    enableBackButton();
                    progress.dismiss();
                }
            });
        }
    }


    public void setupAddDialog(final String songDescription, final Song song) {
        // prompt to add song
        final AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
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
        // TODO: FIX ME!
        progress = ProgressDialog.show(mParentActivity, "Adding Song", "Adding song to playlist...", true);
        mParentActivity.getAppManager().addSong(song, new afterAddSong());
    }


    private class afterAddSong implements CollabifyCallback<space.collabify.android.collabify.models.domain.Playlist> {

        @Override
        public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
            progress.dismiss();
        }

        @Override
        public void failure(RetrofitError error) {
            progress.dismiss();

            mParentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(mParentActivity.getBaseContext(), "Error adding song to playlist", Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public void exception(Exception e) {
            progress.dismiss();
            Toast.makeText(mParentActivity.getBaseContext(), "Error adding song to playlist", Toast.LENGTH_LONG).show();
        }
    }

    public void enableBackButton(){

        backButton.setVisibility(View.VISIBLE);
        backButton.setClickable(true);
    }

    public void setmParentActivity(PrimaryViewActivity mParentActivity){
        this.mParentActivity = mParentActivity;
    }

    public PrimaryViewActivity getmParentActivity(){
        return this.mParentActivity;
    }
}
