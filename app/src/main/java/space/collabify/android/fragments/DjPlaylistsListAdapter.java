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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.dj_playlist_list_row, parent, false);

        final Playlist playlist = getItem(position);
        TextView rowTitle = (TextView) customView.findViewById(R.id.dj_playlist_title);
        ImageView playlistArt = (ImageView) customView.findViewById(R.id.dj_playlist_art);
        ImageButton addButton = (ImageButton) customView.findViewById(R.id.dj_playlist_tracks_add);

        if(!"".equals(playlist.getId()) && playlist.getArtUrl() != null && !playlist.getArtUrl().isEmpty()){
            Picasso.with(getContext()).load(playlist.getArtUrl()).into(playlistArt);
        }

        rowTitle.setText(playlist.getName());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDjTracksFragment.setupViewPlaylistTracksDialog(playlist.getName(), playlist);
            }
        });

        return customView;
    }
}
