package space.collabify.collabify.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import space.collabify.collabify.R;
import space.collabify.collabify.controls.ImageToggleButton;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;

/**
 * This file was born on April 01 at 16:34
 */
public class PlaylistListAdapter extends ArrayAdapter<Song> {
    protected User mUser;
    protected PlaylistFragment mPlaylistFragment;

    public PlaylistListAdapter(Context context, List<Song> songs, User user, PlaylistFragment fragment){
        super(context,  R.layout.playlist_collabifier_list_row, songs);
        this.mPlaylistFragment = fragment;
        this.mUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.playlist_collabifier_list_row, parent, false);
        Song songItem = getItem(position);

        TextView songDescriptionTextView = (TextView) customView.findViewById(R.id.playlist_collabifier_song_description);
        TextView songIdView = (TextView) customView.findViewById(R.id.playlist_row_song_id);
        //TODO: set upvote_icon,downvote_icon button image backgrounds depending on user vote?
        ImageButton deleteButton = (ImageButton) customView.findViewById(R.id.playlist_collabifier_delete_button);
        ImageToggleButton upvoteButton = (ImageToggleButton) customView.findViewById(R.id.playlist_collabifier_upvote_button);
        ImageToggleButton downvoteButton = (ImageToggleButton) customView.findViewById(R.id.playlist_collabifier_downvote_button);

        //initialize button states
        upvoteButton.setChecked(songItem.isUpvoted());
        downvoteButton.setChecked(songItem.isDownvoted());
        
        //add onclick listeners to the row
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaylistFragment.onDeleteClick(v);
            }
        });

        upvoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlaylistFragment.onUpvoteClick(buttonView, isChecked);
            }
        });

        downvoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlaylistFragment.onDownvoteClick(buttonView, isChecked);
            }
        });


        //set up the row elements
        String title = songItem.getTitle();
        title = title.substring(0, Math.min(title.length(), 30));
        String artist = songItem.getArtist();
        artist = artist.substring(0, Math.min(artist.length(), 30));

        String newSongDescription = title + "\n(" + artist + ")";
        songDescriptionTextView.setText(newSongDescription);
        songIdView.setText(songItem.getId());


        int visibility = isDeleteVisible(songItem) ? View.VISIBLE : View.INVISIBLE;
        deleteButton.setVisibility(visibility);

        return customView;
    }

    protected boolean isDeleteVisible(Song song){
        return !mUser.getRole().isBlacklisted() &&
                (song.wasAddedByUser() || mUser.getRole().isDJ() || mUser.getRole().isPromoted());
    }
}
