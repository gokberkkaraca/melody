package ch.epfl.sweng.melody;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class PublicMemoryActivityTest extends ActivityTest {

    @Rule
    public final IntentsTestRule<PublicMemoryActivity> publicMemoryActivityIntentsTestRule =
            new IntentsTestRule<PublicMemoryActivity>(PublicMemoryActivity.class, false, true);

    @Test
    public void datePickerWorks() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000);
        onView(withText("Time travel")).perform(click());
        onView(withText("OK")).perform(click());
    }

    @Test
    public void openFriendsList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Friends")).perform(click());
    }

    @Test
    public void openUserSearch() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Search Users")).perform(click());
    }

    @Test
    public void openFriendRequestList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000);
        onView(withText("Friends Requests")).perform(click());
    }
}

