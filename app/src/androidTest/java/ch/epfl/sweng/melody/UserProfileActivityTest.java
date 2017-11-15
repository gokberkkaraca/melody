package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UserProfileActivityTest {

    @Rule
    public final IntentsTestRule<UserProfileActivity> userProfileActivityActivityTestRule =
            new IntentsTestRule<UserProfileActivity>(UserProfileActivity.class) {
                @Override
                protected Intent getActivityIntent() {

                    User user;
                    final GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
                    when(googleSignInAccount.getId()).thenReturn(UUID.randomUUID().toString());
                    when(googleSignInAccount.getGivenName()).thenReturn("Jiacheng");
                    when(googleSignInAccount.getFamilyName()).thenReturn("Xu");
                    when(googleSignInAccount.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(googleSignInAccount.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
                    String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
                    when(googleSignInAccount.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(googleSignInAccount);

                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, UserProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER", user);
                    intent.putExtras(bundle);
                    return intent;
                }
            };

    @Test
    public void testCanLogOut() {
        onView(withId(R.id.log_out)).perform(click());
    }

    /******************************************************
     ******************* Menu Button Tests ****************
     *****************************************************/
    @Test
    public void goToPublicMemoryTest() throws Exception {
        onView(withId(R.id.home)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(PublicMemoryActivity.class.getName()));
    }

    @Test
    public void goToCreateNewMemoryTest() throws Exception {
        onView(withId(R.id.plus)).perform(click());
        Thread.sleep(100);
        intended(hasComponent(CreateMemoryActivity.class.getName()));
    }

    @Test
    public void goToUserProfileTest() throws Exception {
        onView(withId(R.id.person)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(UserProfileActivity.class.getName()));
    }

    // TODO Activate this test when Map Activity is implemented
    @Ignore @Test
    public void goToMapTest() throws Exception {
        onView(withId(R.id.planet)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(UserProfileActivity.class.getName()));
    }

    // TODO Activate this test when Map Activity is implemented
    @Ignore
    @Test
    public void goToNotification() throws Exception {
        onView(withId(R.id.bell)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(UserProfileActivity.class.getName()));
    }
}