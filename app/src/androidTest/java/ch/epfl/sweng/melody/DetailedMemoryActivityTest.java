package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DetailedMemoryActivityTest extends ActivityTest{

    @Rule
    public final IntentsTestRule<DetailedMemoryActivity> memoryDetailActivityIntentsTestRule =
            new IntentsTestRule<DetailedMemoryActivity>(DetailedMemoryActivity.class, false, true) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, DetailedMemoryActivity.class);
                    intent.putExtra("memoryId", "9223370526068641586");
                    return intent;
                }
    };


    @Test
    public void getMemoryTest() throws Exception {
        Thread.sleep(5000);
        //onView(withId(R.id.memoryAuthor)).check(matches(withText("Jiacheng Xu")));
        onView(withId(R.id.memoryLocation)).check(matches(withText("Lausanne,Switzerland")));
    }
}
