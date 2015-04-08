package space.collabify.collabify.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import space.collabify.collabify.R;
import space.collabify.collabify.activities.CollabifierActivity;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.fragments.PlaylistFragment;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;

/**
 * This file was born on April 05, at 12:35
 * fragment testing: http://blog.denevell.org/android-testing-fragments.html
 */
public class TestFragmentActivity extends CollabifyActivity implements PlaylistFragment.OnPlaylistUpdateRequestListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
    }


    public int upvoteSongCallCount = 0;
    public int downvoteSongCallCount = 0;
    public int deleteSongCallCount = 0;
    public int clearSongVoteCallCount = 0;

    @Override
    public void upvoteSong(Song song) {
        upvoteSongCallCount++;
    }

    @Override
    public void downvoteSong(Song song) {
        downvoteSongCallCount++;
    }

    @Override
    public void deleteSong(Song song) {
        deleteSongCallCount++;
    }

    @Override
    public void clearSongVote(Song song) {
        clearSongVoteCallCount++;
    }

    @Override
    public Song getSongFromId(String songId) {
        if (songId.equalsIgnoreCase("1")) {
            return new Song("on the sunny side of the street", "sonny stitt, etc.",
                    "sonny side up", 1957, "0", "", 0);
        }else {
            return null;
        }
    }
}
