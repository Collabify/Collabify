package space.collabify.android.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.collabify.api.CollabifyApi;
import space.collabify.android.collabify.api.CollabifyApiException;
import space.collabify.android.collabify.models.Converter;
import space.collabify.android.controls.ImageToggleButton;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;
import space.collabify.android.collabify.CollabifyClient;
import space.collabify.android.requests.PlaylistRequest;


/**
 * This file was born on March 11, at 15:52
 */
public class PlaylistFragment extends SwipeRefreshListFragment {
    protected static final String TAG = PlaylistFragment.class.getSimpleName();
    public static final int ID_POS = 0;
    public static final int ALBUM_ART_POS = 1;
    public static final int SONG_DESCRIPTION_POS = 2;
    public static final int UPVOTE_POS = 3;
    public static final int DOWNVOTE_POS = 4;
    public static final int DELETE_POS = 5;

    protected OnPlaylistUpdateRequestListener mListener;
    protected CollabifyActivity mParentActivity;
    protected PlaylistListAdapter mAdapter;

    private CollabifyClient mClient = CollabifyClient.getInstance();
    private CollabifyApi mCollabifyApi = new CollabifyApi();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParentActivity = (CollabifyActivity) getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Song> temp = new ArrayList<>();
        temp.add(new Song("temp song", "temp artist", "temp album", -1, "temp id", "no artwork", ""));
        User user = mParentActivity.getCurrentUser();
        mAdapter = new PlaylistListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser(), this);
        setListAdapter(mAdapter);

        //set action for pull down refreshes
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
    }

    /**
     * Starts a background task to get playlist updates
     */
    private void initiateRefresh() {
        setRefreshing(true);
        try {
            mClient.getEventPlaylist(new LoadPlaylistCallback());
        } catch (CollabifyApiException e) {
            setRefreshing(false);
            e.printStackTrace();
        }
    }

    /**
     * Updates the list of songs visible. Must be called by parent activity for anything interesting to happen
     *
     * @param playlist the new playlist to be shown
     */
    private void updatePlaylist(Playlist playlist) {
        // Remove all items from the ListAdapter, and then replace them with the new items
        PlaylistListAdapter adapter = (PlaylistListAdapter) getListAdapter();
        adapter.clear();

        List<Song> songs = playlist.getmList();
        if (songs.size() != 0) {
            for (Song song : songs) {
                adapter.add(song);
            }
        } else {
            adapter.add(new Song("Your playlist is empty :(", "", "", 0, "", "", ""));
        }
    }

    /**
     * wraps updatePlaylist for server models
     * @param playlist
     */
    private void updatePlaylist(space.collabify.android.collabify.models.domain.Playlist playlist){
        updatePlaylist(Converter.getAppPlaylist(playlist));
    }


    /**
     * Parent activity(mListener) must supply the following operations
     */
    public interface OnPlaylistUpdateRequestListener {
        public Song getSongFromId(String songId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPlaylistUpdateRequestListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPlaylistupdateRequestListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRefreshing(true);
        try {
            mClient.getEventPlaylist(new LoadPlaylistCallback());
        } catch (CollabifyApiException e) {
            setRefreshing(false);
            e.printStackTrace();
        }
    }

    /**
     * Finds the song id TextView in a playlist row and returns the corresdponding song object
     *
     * @param layout the row layout
     * @return the song if found, null otherwise
     */
    protected Song getSongFromLayout(ViewGroup layout) {
        TextView idTextView = (TextView) layout.getChildAt(ID_POS);
        return mListener.getSongFromId(idTextView.getText().toString());
    }

    public void onUpvoteClick(CompoundButton view, boolean isChecked) {
        if (!mClient.isPlaylistUpdating()) {
            //upvote_icon, or unupvote the song in the row
            ViewGroup rowLayout = (ViewGroup) view.getParent();
            ImageToggleButton downvoteButton = (ImageToggleButton) rowLayout.getChildAt(DOWNVOTE_POS);

            //get the song in the same listview row
            Song song = getSongFromLayout(rowLayout);
            if (song == null) {
                Log.w(TAG, "Song couldn't be found in the playlist row");
                return;
            }

            if (isChecked) {
                downvoteButton.setChecked(false);
                mClient.upvoteSong(song);
            } else if (!downvoteButton.isChecked()) {
                mClient.clearSongVote(song);
            }
        }
    }

    public void onDownvoteClick(CompoundButton view, boolean isChecked) {
        if (!mClient.isPlaylistUpdating()) {
            //upvote_icon, or unupvote the song in the row
            ViewGroup rowLayout = (ViewGroup) view.getParent();
            ImageToggleButton upvoteButton = (ImageToggleButton) rowLayout.getChildAt(UPVOTE_POS);

            //get the song in the same listview row
            Song song = getSongFromLayout(rowLayout);
            if (song == null) {
                Log.w(TAG, "Song couldn't be found in the playlist row");
                return;
            }

            if (isChecked) {
                //TODO: warning, causes onDownvoteClick to be called, resulting in
                //two calls to the server when only one may be necessary...
                upvoteButton.setChecked(false);
                mClient.downvoteSong(song);
            } else if (!upvoteButton.isChecked()) {
                mClient.clearSongVote(song);
            }
        }
    }

    public void onDeleteClick(View view) {
        if (!mClient.isPlaylistUpdating()) {
            ViewGroup rowViewGroup = (ViewGroup) view.getParent();
            Song song = getSongFromLayout(rowViewGroup);

            if (song == null) {
                Log.e(TAG, "Couldn't find song to delete");
                return;
            }

            mClient.deleteSong(song);

            //update the playlist after deleting song
            try {
                setRefreshing(true);
                mClient.getEventPlaylist(new LoadPlaylistCallback());
            } catch (CollabifyApiException e){
                setRefreshing(false);
                e.printStackTrace();
            }
        }
    }


    private class LoadPlaylistCallback implements Callback<space.collabify.android.collabify.models.domain.Playlist> {
        @Override
        public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(false);
                }
            });
            updatePlaylist(playlist);
        }

        @Override
        public void failure(RetrofitError error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(false);
                }
            });
            //don't do anything, keep existing playlist?
            Log.d(TAG, "Failed to load playlist:\n" + error.toString());
        }
    }
}
