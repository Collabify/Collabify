package space.collabify.android.collabify.models.domain;

import java.util.List;

/**
 * Created by ricardolopez on 4/10/15.
 */
public class Playlist {
    private List<Song> songs;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
