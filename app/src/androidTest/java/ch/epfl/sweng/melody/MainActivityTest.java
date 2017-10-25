package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.user.User;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by yusiz on 2017/10/25.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    User user;
                    final GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
                    when(googleSignInAccount.getId()).thenReturn("QWERTYU");
                    when(googleSignInAccount.getGivenName()).thenReturn("Jiacheng");
                    when(googleSignInAccount.getFamilyName()).thenReturn("Xu");
                    when(googleSignInAccount.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(googleSignInAccount.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
                    String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
                    when(googleSignInAccount.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(googleSignInAccount);

                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, UserProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER", user);
                    intent.putExtras(bundle);
                    return intent;
                }
            };


    @Test
    public void testCanRun(){}



}