package space.collabify.collabify.unit;

import android.test.ActivityInstrumentationTestCase2;

import space.collabify.collabify.activities.CollabifierActivity;

/**
 * This file was born on April 05, at 15:50
 */
public class CollabifierActivityTest extends ActivityInstrumentationTestCase2<CollabifierActivity> {
    private CollabifierActivity mActivity;

    public CollabifierActivityTest(){
        super(CollabifierActivity.class);
    }

    public CollabifierActivityTest(Class<CollabifierActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }


}
