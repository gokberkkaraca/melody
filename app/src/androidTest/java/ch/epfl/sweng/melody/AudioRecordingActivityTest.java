package ch.epfl.sweng.melody;

import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class AudioRecordingActivityTest {

    @Rule
    public final ActivityTestRule<AudioRecordingActivity> audioRecordingActivityTestRule =
            new ActivityTestRule<>(AudioRecordingActivity.class);

    private AudioRecordingActivity audioRecordingActivity;

    @Before
    public void setActivity() {
        audioRecordingActivity = audioRecordingActivityTestRule.getActivity();
    }

    @Before
    public void grantAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.RECORD_AUDIO");
        }
    }

    @Test
    public void canRecord() throws Exception {
        onView(withId(R.id.start_button)).perform(click());
        Thread.sleep(2500);
        onView(withId(R.id.stop_button)).perform(click());
        Thread.sleep(2500);
        onView(withId(R.id.play_button)).perform(click());
        Thread.sleep(2500);
        onView(withId(R.id.stop_play_button)).perform(click());
        Thread.sleep(2500);
        onView(withId(R.id.submit_button)).perform(click());
        Thread.sleep(2500);
    }

}