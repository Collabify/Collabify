package space.collabify.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.controls.ImageToggleButton;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.managers.CollabifyResponseCallback;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;


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

    private AppManager mAppManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppManager = AppManager.getInstance();
    }

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
        callLoadPlaylist();
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
        initiateRefresh();
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
        if (!mAppManager.isPlaylistUpdating()) {
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
                mAppManager.upvoteSong(song, null);
            } else if (!downvoteButton.isChecked()) {
                mAppManager.clearSongVote(song, null);
            }
        }
    }

    public void onDownvoteClick(CompoundButton view, boolean isChecked) {
        if (!mAppManager.isPlaylistUpdating()) {
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
                mAppManager.downvoteSong(song, null);
            } else if (!upvoteButton.isChecked()) {
                mAppManager.clearSongVote(song, null);
            }
        }
    }

    public void onDeleteClick(View view) {
        if (!mAppManager.isPlaylistUpdating()) {
            ViewGroup rowViewGroup = (ViewGroup) view.getParent();
            Song song = getSongFromLayout(rowViewGroup);

            if (song == null) {
                Log.e(TAG, "Couldn't find song to delete");
                return;
            }

            callRemoveSong(song);
        }
    }

    private void callRemoveSong(Song song) {
        mAppManager.removeSong(song, new CollabifyResponseCallback() {
            @Override
            public void success(Response response) {
                callLoadPlaylist();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG, "Failed to remove the song:\n" + retrofitError.getMessage());
            }

            @Override
            public void exception(Exception e) {

            }
        });
    }

    private void callLoadPlaylist() {
        setRefreshing(true);
        mAppManager.loadEventPlaylist(new LoadPlaylistCallback());
    }

    private class LoadPlaylistCallback implements CollabifyCallback<Playlist> {
        @Override
        public void success(Playlist playlist, Response response) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(false);
                }
            });
            updatePlaylist(playlist);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(false);
                }
            });
            //don't do anything, keep existing playlist?
            Log.e(TAG, "Failed to load playlist:\n" + retrofitError.getMessage());
        }

        @Override
        public void exception(Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(false);
                }
            });
        }
    }
}
