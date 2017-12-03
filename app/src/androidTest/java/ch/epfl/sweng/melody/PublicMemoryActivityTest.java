package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicMemoryActivityTest extends ActivityTest {

    @Rule
    public final IntentsTestRule<PublicMemoryActivity> publicMemoryActivityIntentsTestRule =
            new IntentsTestRule<PublicMemoryActivity>(PublicMemoryActivity.class, false, true) {
                @Override
                protected Intent getActivityIntent() {

                    User user;
                    final FirebaseUser firebaseUser = mock(FirebaseUser.class);
                    when(firebaseUser.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(firebaseUser.getEmail()).thenReturn("xjcmaxwell@163.com");
                    String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";
                    when(firebaseUser.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(firebaseUser);

                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, PublicMemoryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER", user);
                    intent.putExtras(bundle);
                    return intent;
                }
            };

    @Test
    public void datePickerWorks() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000);
        onView(withText("Time travel")).perform(click());
        onView(withText("OK")).perform(click());
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());    We no longer have this button
        //Date currtTime = Calendar.getInstance().getTime();
        //onView(withId(R.id.dateButton)).check(matches(withText(dateFormat.format(currtTime))));
    }

    @Test
    public void openFriendsList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Friends")).perform(click());
    }

    @Test
    public void openFriendRequestList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000);
        onView(withText("Friends Requests")).perform(click());
    }
}

