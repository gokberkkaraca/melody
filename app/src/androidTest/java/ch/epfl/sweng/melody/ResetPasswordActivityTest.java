package ch.epfl.sweng.melody;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.matcherUtil.ToastMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityTest extends ActivityTest{
    @Rule
    public final ActivityTestRule<ResetPasswordActivity> resetPasswordActivityActivityTestRule =
            new ActivityTestRule<>(ResetPasswordActivity.class);
    private ToastMatcher toastMatcher = new ToastMatcher();

    @Test
    public void canGobackTest() {
        onView(withId(R.id.go_back_button)).perform(click());
    }

    @Test
    public void sendEmptyResetPasswordEmailTest() throws Exception {
        onView(withId(R.id.reset_password_button)).perform(click());
        onView(withText(R.string.reset_password_empty)).inRoot(toastMatcher).check(matches(isDisplayed()));
        Thread.sleep(3000);
    }

    @Test
    public void sendValidResetPasswordEmailTest() throws Exception {
        onView(withId(R.id.reset_email)).perform(typeText("itcompiles.melody@gmail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.reset_password_button)).perform(click());

    }

    @Test
    public void sendInvalidResetPasswordEmailTest() throws Exception {
        onView(withId(R.id.reset_email)).perform(typeText("test@email.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.reset_password_button)).perform(click());
    }


}
