package space.collabify.collabify.unit;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.ImageButton;

import com.spotify.sdk.android.authentication.AuthenticationRequest;

import space.collabify.collabify.R;
import space.collabify.collabify.activities.LoginScreenActivity;
import space.collabify.collabify.activities.ModeSelectActivity;

/**
 * This file was born on March 20, at 14:08
 */
public class LoginScreenActivityTest extends ActivityInstrumentationTestCase2<LoginScreenActivity> {

    private ImageButton mLoginButton;
    private LoginScreenActivity mActivity;

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
        mLoginButton = (ImageButton) mActivity.findViewById(R.id.loginButton);
    }


    /**
     * Test to make sure hitting 'back' button from login screen doesn't
     * allow user to continue to mode select
     */
    public void testBackDoesntGoToModeSelect() {
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
