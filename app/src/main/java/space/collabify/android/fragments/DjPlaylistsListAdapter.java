package space.collabify.android.fragments;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import space.collabify.android.R;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * Created by Brandon on 4/23/2015.
 */
public class DjPlaylistsListAdapter extends ArrayAdapter<Playlist> {

    public DjPlaylistsListAdapter(Context context, List<Playlist> playlists, User user, SearchDetailsFragment fragment){
        super(context, R.layout.dj_playlist_list_row, playlists);
    }
}
