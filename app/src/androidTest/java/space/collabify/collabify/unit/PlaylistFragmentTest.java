package space.collabify.collabify.unit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import space.collabify.collabify.R;
import space.collabify.collabify.fragments.PlaylistFragment;
import space.collabify.collabify.test.TestFragmentActivity;

/**
 * This file was born on April 05, at 12:47
 */
public class PlaylistFragmentTest extends ActivityInstrumentationTestCase2<TestFragmentActivity>{
    private TestFragmentActivity mActivity;
    private PlaylistFragment mFragment;

    public PlaylistFragmentTest() {
        super(TestFragmentActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();

        //create a playlist fragment to replace the placeholder
        PlaylistFragment playlistFragment = new PlaylistFragment();
        mFragment = (PlaylistFragment)startFragment(playlistFragment);

    }

    private Fragment startFragment(Fragment fragment){
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_test_fragment_layout, fragment, "tag");
        transaction.commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        Fragment frag = mActivity.getSupportFragmentManager().findFragmentByTag("tag");
        return frag;
    }


    public void testValidSong_onUpvoteClick() throws InterruptedException {
        RelativeLayout testRow = (RelativeLayout)mActivity.getLayoutInflater().inflate(R.layout.playlist_collabifier_list_row, null, false);
        //setting the id, the song id textView must be first element
        TextView idTextView = (TextView)testRow.getChildAt(PlaylistFragment.ID_POS);
        assertNotNull("Song id textview must be first element...",idTextView);

        final String id = "1";
        idTextView.setText(id);

        //'click' the upvote button
        mFragment.onUpvoteClick((CompoundButton) testRow.getChildAt(PlaylistFragment.UPVOTE_POS), true);

        //wait a short bit?
        Thread.sleep(100);

        //make sure that the 'upvote' method has been called
        assertEquals(1, mActivity.upvoteSongCallCount);
        assertEquals(0, mActivity.downvoteSongCallCount);
        assertEquals(0, mActivity.deleteSongCallCount);
    }



    public void testInValidSong_onUpvoteClick() throws InterruptedException {
        RelativeLayout testRow = (RelativeLayout)mActivity.getLayoutInflater().inflate(R.layout.playlist_collabifier_list_row, null, false);
        //setting the id, the song id textView must be first element
        TextView idTextView = (TextView)testRow.getChildAt(PlaylistFragment.ID_POS);
        assertNotNull("Song id textview must be first element...",idTextView);

        final String id = "dne";
        idTextView.setText(id);

        //'click' the upvote button
        mFragment.onUpvoteClick((CompoundButton) testRow.getChildAt(PlaylistFragment.UPVOTE_POS), true);

        //wait a short bit?
        Thread.sleep(100);

        //make sure that the 'upvote' method has been called
        assertEquals(0, mActivity.upvoteSongCallCount);
        assertEquals(0, mActivity.downvoteSongCallCount);
        assertEquals(0, mActivity.deleteSongCallCount);

    }

    public void testValidSong_onDownvoteClick() throws InterruptedException {
        RelativeLayout testRow = (RelativeLayout)mActivity.getLayoutInflater().inflate(R.layout.playlist_collabifier_list_row, null, false);
        //setting the id, the song id textView must be first element
        TextView idTextView = (TextView)testRow.getChildAt(PlaylistFragment.ID_POS);
        assertNotNull("Song id textview must be first element...",idTextView);

        final String id = "1";
        idTextView.setText(id);

        //'click' the upvote button
        mFragment.onDownvoteClick((CompoundButton) testRow.getChildAt(PlaylistFragment.DOWNVOTE_POS), true);

        //wait a short bit?
        Thread.sleep(100);

        //make sure that the 'upvote' method has been called
        assertEquals(0, mActivity.upvoteSongCallCount);
        assertEquals(1, mActivity.downvoteSongCallCount);
        assertEquals(0, mActivity.deleteSongCallCount);
    }

    public void testInValidSong_onDownvoteClick() throws InterruptedException {
        RelativeLayout testRow = (RelativeLayout)mActivity.getLayoutInflater().inflate(R.layout.playlist_collabifier_list_row, null, false);
        //setting the id, the song id textView must be first element
        TextView idTextView = (TextView)testRow.getChildAt(PlaylistFragment.ID_POS);
        assertNotNull("Song id textview must be first element...",idTextView);

        final String id = "dne";
        idTextView.setText(id);

        //'click' the upvote button
        mFragment.onDownvoteClick((CompoundButton) testRow.getChildAt(PlaylistFragment.DOWNVOTE_POS), true);

        //wait a short bit?
        Thread.sleep(100);

        //make sure that the 'upvote' method has been called
        assertEquals(0, mActivity.upvoteSongCallCount);
        assertEquals(0, mActivity.downvoteSongCallCount);
        assertEquals(0, mActivity.deleteSongCallCount);
    }


    public void testMultiplePresses() throws InterruptedException {
        RelativeLayout testRow = (RelativeLayout)mActivity.getLayoutInflater().inflate(R.layout.playlist_collabifier_list_row, null, false);
        //setting the id, the song id textView must be first element
        TextView idTextView = (TextView)testRow.getChildAt(PlaylistFragment.ID_POS);
        assertNotNull("Song id textview must be first element...",idTextView);

        final String id = "1";
        idTextView.setText(id);

        //'click' the upvote button
        mFragment.onUpvoteClick((CompoundButton) testRow.getChildAt(PlaylistFragment.UPVOTE_POS), true);

        //wait a short bit?
        Thread.sleep(100);

        //make sure that the 'upvote' method has been called
        assertEquals(1, mActivity.upvoteSongCallCount);
        assertEquals(0, mActivity.downvoteSongCallCount);
        assertEquals(0, mActivity.deleteSongCallCount);
    }
}
