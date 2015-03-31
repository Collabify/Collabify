package space.collabify.collabify.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.controls.ImageToggleButton;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;

/**
 * This file was born on March 28 at 14:58
 */
public class CollabifierPlaylistFragment extends PlaylistFragment {
    private static final String TAG = CollabifierPlaylistFragment.class.getSimpleName();
    private final int ID_POS = 0;
    private final int ALBUM_ART_POS = 1;
    private final int SONG_DESCRIPTION_POS = 2;
    private final int UPVOTE_POS = 3;
    private final int DOWNVOTE_POS = 4;
    private final int DELETE_POS  = 5;

    private OnPlaylistUpdateRequestListener mListener;
    private CollabifyActivity mParentActivity;
    private CollabifierPlaylistListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_playlist, container, false);
        mParentActivity = (CollabifyActivity)getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Song> temp = new ArrayList<>();
        temp.add(new Song("temsong", "temp artist", "temp album", -1, "temp id", "no artwork", false));
        mAdapter = new CollabifierPlaylistListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser());
        setListAdapter(mAdapter);
        return view;
    }

    /**
     * Updates the list of songs visible. Must be called by parent activity for anything interesting to happen
     * @param playlist the new playlist to be shown
     */
    public void updatePlaylist(Playlist playlist){
        // Remove all items from the ListAdapter, and then replace them with the new items
        CollabifierPlaylistListAdapter adapter = (CollabifierPlaylistListAdapter) getListAdapter();
        adapter.clear();
        for (Song song: playlist.getmList()) {
            adapter.add(song);
        }
    }


    /**
     * Parent activity(mListener) must supply the following operations
     */
    public interface OnPlaylistUpdateRequestListener {
        public void onPlaylistUpdateRequest();
        public void upvoteSong(Song song);
        public void downvoteSong(Song song);
        public void deleteSong(Song song);
        public void clearSongVote(Song song);
        public Song getSongFromId(String songId);
    };

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
        mListener.onPlaylistUpdateRequest();
    }

    /**
     * Finds the song id TextView in a playlist row and returns the corresdponding song object
     * @param layout the row layout
     * @return the song if found, null otherwise
     */
    private Song getSongFromLayout(ViewGroup layout) {
        TextView idTextView = (TextView) layout.getChildAt(ID_POS);
        return  mListener.getSongFromId(idTextView.getText().toString());
    }

    private void onUpvoteClick(CompoundButton view, boolean isChecked){
        //upvote_icon, or unupvote the song in the row
        ViewGroup rowLayout = (ViewGroup) view.getParent();
        ImageToggleButton downvoteButton = (ImageToggleButton)rowLayout.getChildAt(DOWNVOTE_POS);

        //get the song in the same listview row
        Song song =  getSongFromLayout(rowLayout);
        if(song == null) {
            Log.w(TAG, "Song couldn't be found in the playlist row");
            return;
        }

        if(isChecked){
            downvoteButton.setChecked(false);
            mListener.upvoteSong(song);
        }else if(!downvoteButton.isChecked()){
            mListener.clearSongVote(song);
        }
    }

    private void onDownvoteClick(CompoundButton view, boolean isChecked){
        //upvote_icon, or unupvote the song in the row
        ViewGroup rowLayout = (ViewGroup) view.getParent();
        ImageToggleButton upvoteButton  = (ImageToggleButton)rowLayout.getChildAt(UPVOTE_POS);

        //get the song in the same listview row
        Song song =  getSongFromLayout(rowLayout);
        if(song == null) {
            Log.w(TAG, "Song couldn't be found in the playlist row");
            return;
        }

        if(isChecked){
            //TODO: warning, causes onDownvoteClick to be called, resulting in
            //two calls to the server when only one may be necessary...
            upvoteButton.setChecked(false);
            mListener.downvoteSong(song);
        }else if(!upvoteButton.isChecked()){
            mListener.clearSongVote(song);
        }
    }

    private void onDeleteClick(View view){
        ViewGroup rowViewGroup = (ViewGroup) view.getParent();
        Song song = getSongFromLayout(rowViewGroup);

        if(song == null){
            Log.e(TAG, "Couldn't find song to delete");
            return;
        }

        mListener.deleteSong(song);

        //need to redraw the listview?
        mListener.onPlaylistUpdateRequest();
    }


    private class CollabifierPlaylistListAdapter extends ArrayAdapter<Song> {
        private User mUser;
        public CollabifierPlaylistListAdapter(Context context, List<Song> songs, User user){
            super(context,  R.layout.playlist_collabifier_list_row, songs);
            this.mUser = user;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView  = inflater.inflate(R.layout.playlist_collabifier_list_row, parent, false);

            TextView songDescriptionTextView = (TextView) customView.findViewById(R.id.playlist_collabifier_song_description);
            TextView songIdView = (TextView)customView.findViewById(R.id.playlist_row_song_id);
            //TODO: set upvote_icon,downvote_icon button image backgrounds depending on user vote?
            ImageButton deleteButton = (ImageButton) customView.findViewById(R.id.playlist_collabifier_delete_button);
            ImageToggleButton upvoteButton = (ImageToggleButton) customView.findViewById(R.id.playlist_collabifier_upvote_button);
            ImageToggleButton downvoteButton = (ImageToggleButton) customView.findViewById(R.id.playlist_collabifier_downvote_button);

            //add onclick listeners to the row
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClick(v);
                }
            });

            upvoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onUpvoteClick(buttonView, isChecked);
                }
            });

            downvoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onDownvoteClick(buttonView, isChecked);
                }
            });


            //set up the row elements
            Song songItem = getItem(position);
            String newSongDescription = songItem.getArtist() + " - " + songItem.getTitle();
            songDescriptionTextView.setText(newSongDescription);
            songIdView.setText(songItem.getId());


            int visibility = isDeleteVisible(songItem)? View.VISIBLE: View.INVISIBLE;
            deleteButton.setVisibility(visibility);

            return customView;
        }


        private boolean isDeleteVisible(Song song){
            return song.wasAddedByUser() || mUser.getRole().isDJ();
        }
    }
}
