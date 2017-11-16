package ch.epfl.sweng.melody;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest extends ActivityTest {

    @Rule
    public final IntentsTestRule<UserProfileActivity> userProfileActivityActivityTestRule =
            new IntentsTestRule<UserProfileActivity>(UserProfileActivity.class) {
            };

    @Test
    public void testCanLogOut() {
        onView(withId(R.id.log_out)).perform(click());
    }
}