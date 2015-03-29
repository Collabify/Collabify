package space.collabify.collabify.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.R;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;

/**
 * This file was born on March 28 at 14:58
 */
public class CollabifierPlaylistFragment extends PlaylistFragment {
    private ImageButton mUpvoteButton;
    private ImageButton mDownvoteButton;
    private ImageButton mDeleteButton;
    private OnPlaylistUpdateRequestListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_playlist, container, false);
        List<Song> temp = new ArrayList<>();
        temp.add(new Song("temsong", "temp artist", "temp album", -1, "temp id", "no artwork", false));
        CollabifierPlaylistListAdapter adapter = new CollabifierPlaylistListAdapter(getActivity().getApplicationContext(), temp);
        setListAdapter(adapter);
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

    public interface OnPlaylistUpdateRequestListener {
        public void onPlaylistUpdateRequest();
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
        mListener.onPlaylistUpdateRequest();
    }

    private class CollabifierPlaylistListAdapter extends ArrayAdapter<Song> {
        public CollabifierPlaylistListAdapter(Context context, List<Song> songs){
            super(context,  R.layout.playlist_collabifier_list_row, songs);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView  = inflater.inflate(R.layout.playlist_collabifier_list_row, parent, false);

            Song songItem = getItem(position);
            TextView songDescriptionTextView = (TextView) customView.findViewById(R.id.playlist_collabifier_song_description);
            //TODO: set upvote,downvote button image backgrounds depending on user vote?
            ImageButton deleteButton = (ImageButton) customView.findViewById(R.id.playlist_collabifier_delete_button);

            String newSongDescription = songItem.getArtist() + " - " + songItem.getTitle();
            songDescriptionTextView.setText(newSongDescription);
            int visibility = songItem.wasAddedByUser()? View.VISIBLE: View.INVISIBLE;
            deleteButton.setVisibility(visibility);

            return customView;
        }
    }
}
