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
import space.collabify.android.models.Playlist;
import space.collabify.android.models.User;

/**
 * Created by Brandon on 4/23/2015.
 */
public class DjPlaylistsListAdapter extends ArrayAdapter<Playlist> {

    protected User mUser;
    protected DjTracksFragment mDjTracksFragment;

    public DjPlaylistsListAdapter(Context context, List<Playlist> playlists, User user, DjTracksFragment fragment){
        super(context, R.layout.dj_playlist_list_row, playlists);
        this.mDjTracksFragment = fragment;
        this.mUser = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        // get item for this position
        final Playlist playlist = getItem(position);

        // declare a viewholder
        ViewHolder viewHolder;

        // create a new viewHolder
        if (convertView == null) {
            // create a new viewholder
            viewHolder = new ViewHolder();

            // inflate the view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.dj_playlist_list_row, parent, false);

            // add new views to viewholder
            viewHolder.rowTitle = (TextView) convertView.findViewById(R.id.dj_playlist_title);
            viewHolder.playlistArt = (ImageView) convertView.findViewById(R.id.dj_playlist_art);

            // attach to actual view
            convertView.setTag(viewHolder);
        }
        // reuse view
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate with data

        // add playlist art
        if(!"".equals(playlist.getId()) && playlist.getArtUrl() != null && !playlist.getArtUrl().isEmpty()){
            Picasso.with(getContext()).load(playlist.getArtUrl()).into(viewHolder.playlistArt);
        }

        // add row title
        viewHolder.rowTitle.setText(playlist.getName());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDjTracksFragment.populateListWithTracks(playlist.getId(), playlist.getOwner(), playlist.getName());
            }
        });

        return convertView;
    }


    private static class ViewHolder {
        TextView rowTitle;
        ImageView playlistArt;
    }
}
