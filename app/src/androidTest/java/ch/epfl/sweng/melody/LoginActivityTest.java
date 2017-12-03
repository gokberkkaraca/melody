package ch.epfl.sweng.melody;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.matcherUtil.ToastMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);
    private ToastMatcher toastMatcher = new ToastMatcher();

    @Test
    public void testCanLogIn() throws Exception {
        onView(withId(R.id.email)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("helloworld")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(5000);
    }

    @Test
    public void LogInWithInvalidPassword() {
        onView(withId(R.id.email)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText("Enter password!")).inRoot(toastMatcher).check(matches(isDisplayed()));
        onView(withId(R.id.password)).perform(typeText("hello")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());
    }

    @Test
    public void CanGoToSignUp() {
        onView(withId(R.id.create_new_account_button)).perform(click());
    }
}


