package ch.epfl.sweng.melody;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Ignore    @Test
    //TODO: Reactive this test after dealing with JENKINS
    public void testCanLogIn() {
        onView(withId(R.id.email)).perform(typeText("itcompiles-melody@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("sweng2017")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
    }


}