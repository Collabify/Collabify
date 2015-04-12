package space.collabify.android.unit;

import android.test.ActivityInstrumentationTestCase2;

import space.collabify.android.activities.DjActivity;

/**
 * This file was born on April 05, at 15:49
 */
public class DjActivityTest extends ActivityInstrumentationTestCase2<DjActivity> {
    private DjActivity mActivity;


    public DjActivityTest(){
        super(DjActivity.class);
    }

    public DjActivityTest(Class<DjActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }
}
