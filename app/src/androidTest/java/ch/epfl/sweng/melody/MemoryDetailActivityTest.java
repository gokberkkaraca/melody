package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.melody.ViewMatcher.hasDrawable;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemoryDetailActivityTest {
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";

    @Rule
    public final IntentsTestRule<MemoryDetailActivity> memoryDetailActivityIntentsTestRule =
            new IntentsTestRule<MemoryDetailActivity>(MemoryDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    User user;
                    final GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
                    when(googleSignInAccount.getId()).thenReturn("QWERTYU");
                    when(googleSignInAccount.getGivenName()).thenReturn("Jiacheng");
                    when(googleSignInAccount.getFamilyName()).thenReturn("Xu");
                    when(googleSignInAccount.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(googleSignInAccount.getEmail()).thenReturn("xjcmaxwell@163.com");
                    when(googleSignInAccount.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(googleSignInAccount);
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext,MemoryDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER", user);
                    intent.putExtra("memoryId", "0465873b-d4e2-452e-b205-047c70114c3c");
                    intent.putExtras(bundle);
                    return intent;
                }
            };


    @Test
    public void getMemoryTest() throws Exception{
        Thread.sleep(5000);
        onView(withId(R.id.memoryAuthor)).check(matches(withText("Yusi Zou")));
        onView(withId(R.id.memoryLocation)).check(matches(withText("Lausanne,Switzerland")));
        onView(withId(R.id.memoryPicture)).check(matches(not(hasDrawable())));
    }
}
