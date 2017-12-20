package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class FriendListActivityAllTest {

    @Rule
    public final IntentsTestRule<FriendListActivity> memoryDetailActivityIntentsTestRule =
            new IntentsTestRule<FriendListActivity>(FriendListActivity.class, false, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, DetailedMemoryActivity.class);
                    intent.putExtra(PublicMemoryActivity.EXTRA_GOING_TO_USER_LIST, "all");
                    return intent;
                }
            };

    @Test
    public void testCanPass() {

    }

    @Test
    public void testCanSearchWithEmail() {
        onView(withId(R.id.search_view)).perform(typeText("jiacheng.xu@epfl.ch")).perform(closeSoftKeyboard());
    }

    @Test
    public void testCanSearchWithName() {
        onView(withId(R.id.search_view)).perform(typeText("Kebab")).perform(closeSoftKeyboard());
    }
}