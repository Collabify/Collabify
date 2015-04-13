package space.collabify.collabify.unit;

import android.test.ActivityInstrumentationTestCase2;

import space.collabify.collabify.activities.ModeSelectActivity;

/**
 * This file was born on April 08, at 18:13
 */
public class ModeSelectActivityTest extends ActivityInstrumentationTestCase2<ModeSelectActivity>{
    private ModeSelectActivity mActivity;

    public ModeSelectActivityTest(){
        super(ModeSelectActivity.class);
    }

    public ModeSelectActivityTest(Class<ModeSelectActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        mActivity = getActivity();
    }


}
