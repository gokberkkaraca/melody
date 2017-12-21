package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

public class FriendListActivityRequestsTest {

    @Rule
    public final IntentsTestRule<FriendListActivity> memoryDetailActivityIntentsTestRule =
            new IntentsTestRule<FriendListActivity>(FriendListActivity.class, false, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, DetailedMemoryActivity.class);
                    intent.putExtra(PublicMemoryActivity.EXTRA_GOING_TO_USER_LIST, "requests");
                    return intent;
                }
            };

    @Test
    public void testCanPass() {
    }
}