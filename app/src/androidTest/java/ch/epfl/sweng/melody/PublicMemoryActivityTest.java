package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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
                    final GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
                    when(googleSignInAccount.getId()).thenReturn("QWERTYU");
                    when(googleSignInAccount.getGivenName()).thenReturn("Jiacheng");
                    when(googleSignInAccount.getFamilyName()).thenReturn("Xu");
                    when(googleSignInAccount.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(googleSignInAccount.getEmail()).thenReturn("xjcmaxwell@163.com");
                    String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
                    when(googleSignInAccount.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(googleSignInAccount);

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
    public void datePickerWorks() {
        //Need to find a way to test menus
        
        /*onView(withId(R.id.time_changing_item)).perform(click());
        onView(withText("OK")).perform(click());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        Date currtTime = Calendar.getInstance().getTime();*/
        //onView(withId(R.id.dateButton)).check(matches(withText(dateFormat.format(currtTime))));
    }

    @Test
    public void openFriendsList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("See my friends")).perform(click());
    }
}

