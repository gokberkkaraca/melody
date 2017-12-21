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
public class SignUpActivityTest extends ActivityTest{

    @Rule
    public final ActivityTestRule<SignUpActivity> signUpActivityActivityTestRule =
            new ActivityTestRule<>(SignUpActivity.class);
    private ToastMatcher toastMatcher = new ToastMatcher();

    @Test
    public void allreadyHaveAccountButtonWorksTest() {
        onView(withId(R.id.already_have_account_login_button)).perform(click());
    }

    @Test
    public void emptyEmailTest() {
        onView(withId(R.id.register_button)).perform(click());
        onView(withText(R.string.email_is_empty)).inRoot(toastMatcher).check(matches(isDisplayed()));
    }

    @Test
    public void emptyPasswordTest() {
        onView(withId(R.id.create_new_email)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withText(R.string.password_is_empty)).inRoot(toastMatcher).check(matches(isDisplayed()));
    }

    @Test
    public void emptydisplayNameTest() {
        onView(withId(R.id.create_new_email)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_new_password)).perform(typeText("hellojiacheng")).perform(closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withText(R.string.nickname_is_empty)).inRoot(toastMatcher).check(matches(isDisplayed()));
    }

    @Test
    public void invalidPasswordTest() {
        onView(withId(R.id.create_new_email)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_new_password)).perform(typeText("hello")).perform(closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withText(R.string.minimum_password)).inRoot(toastMatcher).check(matches(isDisplayed()));
    }

    @Test
    public void signUpFailedTest() {
        onView(withId(R.id.create_new_email)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_new_password)).perform(typeText("hellojiacheng")).perform(closeSoftKeyboard());
        onView(withId(R.id.create_display_name)).perform(typeText("kebab")).perform(closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
    }
}
