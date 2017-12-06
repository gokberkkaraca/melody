package ch.epfl.sweng.melody;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditUserInfoActivityTest extends ActivityTest {

    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";

    private final String biography = "";
    @Rule
    public final IntentsTestRule<EditUserInfoActivity> editUserInfoActivityIntentsTestRule =
            new IntentsTestRule<EditUserInfoActivity>(EditUserInfoActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    User user;
                    final FirebaseUser firebaseUser = mock(FirebaseUser.class);
                    when(firebaseUser.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(firebaseUser.getEmail()).thenReturn("xjcmaxwell@163.com");
                    when(firebaseUser.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(firebaseUser);
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, CreateMemoryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER", user);
                    intent.putExtras(bundle);
                    return intent;
                }
            };

    @Test
    public void getUserInfoTest() throws Exception {
        onView(withId(R.id.memoryAuthor)).check(matches(withText("Jiacheng Xu")));
    }
}
