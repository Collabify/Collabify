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

        final Song song = getItem(position);

        // declare a viewholder
        ViewHolder viewHolder;

        // create a new viewHolder
        if (convertView == null) {
            // create a new viewholder
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_details_row, parent, false);

            viewHolder.rowTitle = (TextView) convertView.findViewById(R.id.song_row_title);
            viewHolder.rowArtist = (TextView) convertView.findViewById(R.id.song_row_artist);
            viewHolder.albumArt = (ImageView) convertView.findViewById(R.id.song_details_album_art);
            viewHolder.addButton = (ImageButton) convertView.findViewById(R.id.song_row_add);

            // attach to actual view
            convertView.setTag(viewHolder);
        }
        // reuse view
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(!"".equals(song.getId()) && song.getArtwork() != null && !song.getArtwork().isEmpty()){
            //use picasso to load album art
            Picasso.with(getContext()).load(song.getLowArtwork()).into(viewHolder.albumArt);
        }

        final String newSongDescription = song.getTitle() + "\n(" + song.getArtist() + ")";

        viewHolder.rowTitle.setText(song.getTitle());
        viewHolder.rowArtist.setText("(" + song.getArtist() + ")");

        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDjTracksFragment.setupAddDialog(newSongDescription, song);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView rowTitle;
        TextView rowArtist;
        ImageView albumArt;
        ImageButton addButton;
    }
}
