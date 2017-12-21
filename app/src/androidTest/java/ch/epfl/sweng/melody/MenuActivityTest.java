package ch.epfl.sweng.melody;


import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

abstract class MenuActivityTest extends ActivityTest {
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

    @Test
    public void goToNotification() throws Exception {
        onView(withId(R.id.bell)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(FriendListActivity.class.getName()));
    }
}
