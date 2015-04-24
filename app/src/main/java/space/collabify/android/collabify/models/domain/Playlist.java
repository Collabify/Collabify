package space.collabify.android.collabify.models.domain;

import java.util.List;

/**
 * Created by ricardolopez on 4/10/15.
 */
public class Playlist {
    private List<Song> songs;
    private Song currentSong;
    private Song nextSong;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Song getCurrentSong() {
      return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
      this.currentSong = currentSong;
    }

    public Song getNextSong() {
      return nextSong;
    }

    public void setNextSong(Song nextSong) {
      this.nextSong = nextSong;
    }
}
