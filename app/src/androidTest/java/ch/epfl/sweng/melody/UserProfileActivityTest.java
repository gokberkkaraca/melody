package ch.epfl.sweng.melody;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest {

    @Rule
    public final ActivityTestRule<UserProfileActivity> userProfileActivityActivityTestRule =
            new ActivityTestRule<UserProfileActivity>(UserProfileActivity.class);

    @Test
    public void testCanLogOut() {
//        onView(withId(R.id.log_out)).perform(click());
    }
}