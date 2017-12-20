package ch.epfl.sweng.melody;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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

abstract public class ActivityTest {

    @Before
    public void setUp() {
        final FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getDisplayName()).thenReturn("Jiacheng Xu");
        when(firebaseUser.getEmail()).thenReturn("xjcmaxwell@163.com");
        String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";
        when(firebaseUser.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
        MainActivity.setUser(new User(firebaseUser));
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

    @Test
    public void goToMapTest() throws Exception {
        onView(withId(R.id.planet)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(ShowMapActivity.class.getName()));
    }

    @Ignore
    @Test
    public void goToNotification() throws Exception {
        onView(withId(R.id.bell)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(FriendListActivity.class.getName()));
    }
}
