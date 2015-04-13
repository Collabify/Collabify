package space.collabify.android.test;

import android.os.Bundle;

import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.fragments.PlaylistFragment;
import space.collabify.android.models.Song;

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
    public Song getSongFromId(String songId) {
        if (songId.equalsIgnoreCase("1")) {
            return new Song("on the sunny side of the street", "sonny stitt, etc.",
                    "sonny side up", 1957, "0", "", "");
        }else {
            return null;
        }
    }
}
