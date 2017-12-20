package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class DetailedPhotoMemoryTest extends ActivityTest {

    @Rule
    public final IntentsTestRule<DetailedMemoryActivity> memoryDetailActivityIntentsTestRule =
            new IntentsTestRule<DetailedMemoryActivity>(DetailedMemoryActivity.class, false, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, DetailedMemoryActivity.class);
                    intent.putExtra("memoryId", "9223370525978343766");
                    return intent;
                }
            };


    @Test
    public void getMemoryTest() throws Exception {
        Thread.sleep(5000);
        onView(withId(R.id.memoryAuthor)).check(matches(withText("Black.R")));
        onView(withId(R.id.memoryAuthorPic)).check(matches(allOf(isEnabled(), isClickable()))).perform(click());
        Thread.sleep(100);
        intended(hasComponent(UserProfileActivity.class.getName()));
    }
}
