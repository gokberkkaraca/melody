package ch.epfl.sweng.melody;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DetailedMemoryActivityTest extends ActivityTest{

    @Rule
    public final ActivityTestRule<DetailedMemoryActivity> memoryDetailActivityIntentsTestRule =
            new ActivityTestRule<DetailedMemoryActivity>(DetailedMemoryActivity.class, false, true) {
            };


    @Test
    public void getMemoryTest() throws Exception {
        Thread.sleep(5000);
        //onView(withId(R.id.memoryAuthor)).check(matches(withText("Jiacheng Xu")));
        onView(withId(R.id.memoryLocation)).check(matches(withText("Lausanne,Switzerland")));
    }
}
