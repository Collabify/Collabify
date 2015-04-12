package space.collabify.android.unit;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;

import space.collabify.android.activities.CollabifierActivity;
import space.collabify.android.activities.JoinEventActivity;
import space.collabify.android.models.Event;

/**
 * This file was born on March 24, at 09:04
 */
public class JoinEventActivityTest extends ActivityInstrumentationTestCase2<JoinEventActivity> {
    private JoinEventActivity mActivity;

    public JoinEventActivityTest(){
        super(JoinEventActivity.class);
    }

    public JoinEventActivityTest(Class<JoinEventActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    public void testRotateCrash() {
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //wait for a crash...
        //TODO: figure out way to continue running tests if activity crashes
        //and a better way so that this always crashes...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //we're good
        assertTrue(true);
    }

    public void testInvalidEventPassword_toCollabify(){
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CollabifierActivity.class.getName(), null, false);

        Event event = new Event("test", 1, "password", true, false);
        mActivity.toCollabifier(event, "not the password");

        //shouldn't go to next
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 2000);

        assertNull("Bad event password shouldn't continue to collabifier activity", nextActivity);
    }

    public void testValidEventPassword_toCollabify(){
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CollabifierActivity.class.getName(), null, false);

        Event event = new Event("test", 1, "password", true, false);
        mActivity.toCollabifier(event, "password");

        //shoul go to next
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 2000);

        assertNotNull("Correct event password should continue to collabifier activity", nextActivity);
    }

}
