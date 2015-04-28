package space.collabify.android.base;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import space.collabify.android.R;
import space.collabify.android.collabify.models.domain.Song;

/**
 * Created by ricardolopez on 4/23/15.
 */
public class CollabifierPlaylistInfo extends RelativeLayout {

    LayoutInflater mInflater;
    private ImageView mAlbumArt;
    private TextView mSongStatus;
    private ImageView mSongStatusIcon;
    private TextView mSongName;
    private TextView mSongArtist;

    public CollabifierPlaylistInfo(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public CollabifierPlaylistInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public CollabifierPlaylistInfo(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View v = mInflater.inflate(R.layout.collabifier_playlist_info, this, true);

        mAlbumArt = (ImageView) v.findViewById(R.id.song_album_art);
        mSongStatus = (TextView) v.findViewById(R.id.playlist_status);
        mSongStatusIcon = (ImageView) v.findViewById(R.id.playlist_status_icon);
        mSongName = (TextView) v.findViewById(R.id.currently_playing_song_title);
        mSongArtist = (TextView) v.findViewById(R.id.currently_playing_song_artist);

        mSongStatusIcon.setVisibility(INVISIBLE);
    }

    public void updateSong(Song song, Context context) {

        if (song != null && context != null) {
            Picasso.with(context).load(song.getArtworkUrl()).into(mAlbumArt);
            mSongStatus.setText(R.string.label_currently_song);
            mSongName.setText(song.getTitle());
            mSongArtist.setText(song.getArtist());
        }
        else {
            mAlbumArt.setImageResource(R.drawable.ic_album);
            mSongStatus.setText(R.string.label_nothing_to_play);
            mSongName.setText("");
            mSongArtist.setText("");
        }
    }

    public void songPaused() {

    }

    public void songPlaying() {

    }

    public void playlistEmpty() {
        updateSong(null, null);
    }
}
