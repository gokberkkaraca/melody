package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest extends ActivityTest{

    @Rule
    public final IntentsTestRule<UserProfileActivity> userProfileActivityActivityTestRule =
            new IntentsTestRule<UserProfileActivity>(UserProfileActivity.class) {
            };

    @Test
    public void testCanLogOut() {
        onView(withId(R.id.log_out)).perform(click());
    }
}