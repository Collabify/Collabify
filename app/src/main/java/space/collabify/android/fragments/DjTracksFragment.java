package space.collabify.android.fragments;

import android.app.AlertDialog;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.activities.PrimaryViewActivity;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;


/**
 * This file was born on March 11, at 15:53
 */
public class DjTracksFragment extends ListFragment {

    protected PrimaryViewActivity mParentActivity;
    protected DjPlaylistsListAdapter mDjPlaylistsListAdapter;
    protected DjTracksListAdapter mDjTracksListAdapter;

    private ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View listFragment = inflater.inflate(R.layout.fragment_dj_tracks, container, false);
        return listFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParentActivity = (PrimaryViewActivity)getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Playlist> temp = new ArrayList<>();
        User user = mParentActivity.getCurrentUser();
        mDjPlaylistsListAdapter = new DjPlaylistsListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser(), this);
        // mDjTracksListAdapter = new DjTracksListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser(), this);
        setListAdapter(mDjPlaylistsListAdapter);

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

    private class populatePlaylistList extends SpotifyCallback<kaaes.spotify.webapi.android.models.Pager<kaaes.spotify.webapi.android.models.Playlist>>{
        @Override
        public void failure(SpotifyError spotifyError) {

        }

        @Override
        public void success(final Pager<kaaes.spotify.webapi.android.models.Playlist> playlistPager, Response response) {

            mParentActivity.runOnUiThread(new Runnable() {
                public void run() {
                    mDjPlaylistsListAdapter.clear();

                    List<kaaes.spotify.webapi.android.models.Playlist> playlists = playlistPager.items;

                    for(kaaes.spotify.webapi.android.models.Playlist playlist : playlists){

                        String artUrl = "";

                        if(playlist.images.size() >= 3){
                            artUrl = playlist.images.get(2).url;
                        }

                        Playlist newPlaylist = new Playlist(playlist.name, playlist.id, artUrl, new ArrayList<Song>());

                        mDjPlaylistsListAdapter.add(newPlaylist);
                    }

                }
            });
        }
    }

    public void setupViewPlaylistTracksDialog(final String playlistName, final Playlist playlist) {
        // prompt to add song
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getmParentActivity());
        builder.setTitle(getString(R.string.populate_playlist_tracks_title));
        builder.setMessage(playlistName);
        builder.setPositiveButton(getString(R.string.populate_playlist_tracks_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: send song to server

                        dialog.cancel();

                        populateListWithTracks(playlist);
                    }
                });
        builder.setNegativeButton(getString(R.string.populate_playlist_tracks_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void populateListWithTracks(Playlist playlist){

        progress = ProgressDialog.show(this.getmParentActivity(), "Populating DJ Tracks", "Fetching tracks...", true);

        mParentActivity.getAppManager().getSpotifyService().getPlaylistTracks(mParentActivity.getAppManager().getEvent().getEventId(), playlist.getId(), new afterFetchTracks());
    }

    private class afterFetchTracks extends SpotifyCallback<kaaes.spotify.webapi.android.models.Pager<kaaes.spotify.webapi.android.models.PlaylistTrack>>{

        @Override
        public void failure(SpotifyError spotifyError) {
            progress.dismiss();
        }

        @Override
        public void success(Pager<kaaes.spotify.webapi.android.models.PlaylistTrack> playlistTrackPager, Response response) {
            progress.dismiss();
        }
    }

    public void setmParentActivity(PrimaryViewActivity mParentActivity){
        this.mParentActivity = mParentActivity;
    }

    public PrimaryViewActivity getmParentActivity(){
        return this.mParentActivity;
    }
}
