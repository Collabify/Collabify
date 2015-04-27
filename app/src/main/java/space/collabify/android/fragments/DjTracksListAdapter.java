package space.collabify.android.fragments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import space.collabify.android.R;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * Created by thefa_000 on 4/23/2015.
 */
public class DjTracksListAdapter extends ArrayAdapter<Song> {

    protected User mUser;
    protected DjTracksFragment mDjTracksFragment;

    public DjTracksListAdapter(Context context, List<Song> songs, User user, DjTracksFragment fragment) {
        super(context, R.layout.playlist_collabifier_list_row, songs);
        this.mDjTracksFragment = fragment;
        this.mUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.song_details_row, parent, false);

        final Song song = getItem(position);
        TextView rowTitle = (TextView) customView.findViewById(R.id.song_row_title);
        TextView rowArtist = (TextView) customView.findViewById(R.id.song_row_artist);
        ImageView albumArt = (ImageView) customView.findViewById(R.id.song_details_album_art);
        ImageButton addButton = (ImageButton) customView.findViewById(R.id.song_row_add);

        if(!"".equals(song.getId()) && song.getArtwork() != null && !song.getArtwork().isEmpty()){
            //use picasso to load album art
            Picasso.with(getContext()).load(song.getArtwork()).into(albumArt);
        }

        final String newSongDescription = song.getTitle() + "\n(" + song.getArtist() + ")";

        rowTitle.setText(song.getTitle());
        rowArtist.setText("(" + song.getArtist() + ")");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDjTracksFragment.setupAddDialog(newSongDescription, song);
            }
        });

        return customView;
    }
}
