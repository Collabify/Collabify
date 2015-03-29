package space.collabify.collabify.fragments;

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

import java.util.List;

import space.collabify.collabify.R;
import space.collabify.collabify.models.Song;

/**
 * This file was born on March 28 at 14:58
 */
public class CollabifierPlaylistFragment extends PlaylistFragment {
    private ImageButton mUpvoteButton;
    private ImageButton mDownvoteButton;
    private ImageButton mDeleteButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_playlist, container, false);
        return view;
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
