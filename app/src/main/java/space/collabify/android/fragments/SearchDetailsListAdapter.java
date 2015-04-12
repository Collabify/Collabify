package space.collabify.android.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import space.collabify.android.R;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * Handles the display of events in a row
 */
public class SearchDetailsListAdapter extends ArrayAdapter<Song> {

    protected User mUser;
    protected SearchDetailsFragment mSearchDetailsFragment;

    public SearchDetailsListAdapter(Context context, List<Song> songs, User user, SearchDetailsFragment fragment) {
        super(context,  R.layout.playlist_collabifier_list_row, songs);
        this.mSearchDetailsFragment = fragment;
        this.mUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.song_details_row, parent, false);

        final Song song = getItem(position);
        TextView rowDesc = (TextView) customView.findViewById(R.id.song_row_description);
        ImageView albumArt = (ImageView) customView.findViewById(R.id.song_details_album_art);
        ImageButton addButton = (ImageButton) customView.findViewById(R.id.song_row_add);

        //set up the row elements
        String title = song.getTitle();
        title = title.substring(0, Math.min(title.length(), 30));
        String artist = song.getArtist();
        artist = artist.substring(0, Math.min(artist.length(), 30));

        final String newSongDescription = title + "\n(" + artist + ")";

        rowDesc.setText(newSongDescription);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchDetailsFragment.getmParentActivity().setupAddDialog(newSongDescription, song);
            }
        });

        return customView;
    }


}
