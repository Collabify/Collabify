package space.collabify.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.base.CollabifierPlaylistInfo;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.collabify.models.Converter;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.controls.ImageToggleButton;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.managers.CollabifyResponseCallback;
import space.collabify.android.models.Song;


/**
 * This file was born on March 11, at 15:52
 */
public class PlaylistFragment extends SwipeRefreshListFragment {
    protected static final String TAG = PlaylistFragment.class.getSimpleName();
    public static final int ID_POS = 0;
    public static final int ALBUM_ART_POS = 1;
    public static final int SONG_DESCRIPTION_POS = 2;
    public static final int UPVOTE_POS = 5;
    public static final int DOWNVOTE_POS = 6;
    public static final int DELETE_POS = 7;

    protected OnPlaylistUpdateRequestListener mListener;
    protected CollabifyActivity mParentActivity;
    protected PlaylistListAdapter mAdapter;

    private AppManager mAppManager;
    private FrameLayout rootView;
    private View listFragment;
    private CollabifierPlaylistInfo info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppManager = AppManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listFragment =  super.onCreateView(inflater, container, savedInstanceState);

        rootView = new FrameLayout(container.getContext());

        rootView.addView(listFragment, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        if (!mAppManager.getUser().getRole().isDJ()) {
            info = new CollabifierPlaylistInfo(container.getContext());
            info.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            rootView.addView(info);

            info.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, info.getMeasuredHeight(), 0, 0);
            listFragment.setLayoutParams(params);

            rootView.bringChildToFront(info);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParentActivity = (CollabifyActivity) getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Song> temp = new ArrayList<>();
        temp.add(new Song("Your playlist is empty :(", "", "", 0, "", "", ""));
        mAdapter = new PlaylistListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser(), this);
        setListAdapter(mAdapter);

        //set action for pull down refreshes
        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
        initiateRefresh();
    }

    /**
     * Starts a background task to get playlist updates
     */
    private void initiateRefresh() {

        setRefreshing(true);

        mAppManager.getEventSettings(new CollabifyResponseCallback() {
            @Override
            public void exception(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Error refreshing playlist", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void success(Response response) {

                callLoadPlaylist();
            }

            @Override
            public void failure(RetrofitError error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Could not refresh playlist", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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

        List<Song> songs = Converter.toPlaylist(playlist);

        if (songs.size() != 0) {
            for (Song song : songs) {
                adapter.add(song);
            }
        } else {
            adapter.add(new Song("Your playlist is empty :(", "", "", 0, "", "", ""));
        }


        if (info != null && playlist.getCurrentSong() != null) {
          info.updateSong(playlist.getCurrentSong(), getActivity());
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
                mAppManager.upvoteSong(song, new afterVoteClick());
            } else if (!downvoteButton.isChecked()) {
                mAppManager.clearSongVote(song, new afterVoteClick());
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
                mAppManager.downvoteSong(song, new afterVoteClick());
            } else if (!upvoteButton.isChecked()) {
                mAppManager.clearSongVote(song, new afterVoteClick());
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

    public void moveSong(View view, int position, int updated) {
      Log.d("MOVE", "Moving from " + position + " to " + updated);
      ViewGroup rowViewGroup = (ViewGroup) view.getParent();
      Song song = getSongFromLayout(rowViewGroup);

      mAppManager.moveSong(song, position, updated, new CollabifyCallback<Playlist>() {

        @Override
        public void success(final Playlist playlist, Response response) {
          getActivity().runOnUiThread(new Runnable() {
            public void run() {
              updatePlaylist(playlist);
            }
          });
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

    private void callRemoveSong(Song song) {
        mAppManager.removeSong(song.getId(), new CollabifyResponseCallback() {
            @Override
            public void success(Response response) {
                getActivity().runOnUiThread(new Runnable() {
                  public void run() {
                    initiateRefresh();
                  }
                });
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
        mAppManager.loadEventPlaylist(new LoadPlaylistCallback());
    }

    private class afterVoteClick extends CollabifyResponseCallback {

        @Override
        public void exception(Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Error sending vote...", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void success(Response response) {

        }

        @Override
        public void failure(RetrofitError error) {

            initiateRefresh();
        }
    }




    private class LoadPlaylistCallback implements CollabifyCallback<Playlist> {
        @Override
        public void success(final Playlist playlist, Response response) {
            getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                setRefreshing(false);
                updatePlaylist(playlist);
              }
            });
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
