package space.collabify.android.unit;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.ImageButton;

import space.collabify.android.R;
import space.collabify.android.activities.LoginScreenActivity;
import space.collabify.android.activities.ModeSelectActivity;
import space.collabify.android.managers.AppManager;

/**
 * This file was born on March 20, at 14:08
 */
public class LoginScreenActivityTest extends ActivityInstrumentationTestCase2<LoginScreenActivity> {

    private ImageButton mLoginButton;
    private LoginScreenActivity mActivity;
    private AppManager mAppManager;

    public LoginScreenActivityTest(){
        super(LoginScreenActivity.class);
    }

    public LoginScreenActivityTest(Class<LoginScreenActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mAppManager = AppManager.getInstance();
        mLoginButton = (ImageButton) mActivity.findViewById(R.id.loginButton);
    }


    /**
     * Test to make sure hitting 'back' button from login screen doesn't
     * allow user to continue to mode select
     */
    public void testBackDoesntGoToModeSelect() {
        //first log user out of spotify
         mAppManager.spotifyLogout(getInstrumentation().getTargetContext());

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(ModeSelectActivity.class.getName(), null, false);

        //click on the login button
        TouchUtils.clickView(this, mLoginButton);

        //wait for activity to be ready
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //from spotify webview activity, hit back button
        this.sendKeys(KeyEvent.KEYCODE_BACK);

        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 2000);

        assertNull("Back from login should stay on LoginScreenActivity", nextActivity);
    }

}
