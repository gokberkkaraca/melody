package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Rule;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.melody.user.FriendAdapter;
import ch.epfl.sweng.melody.user.UserContactInfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FriendListActivityTest {

    @Rule
    public final IntentsTestRule<FriendListActivity> friendListActivityIntentsTestRule =
            new IntentsTestRule<FriendListActivity>(FriendListActivity.class, false, true) {
                @Override
                protected Intent getActivityIntent() {
                    RecyclerView recyclerView= Mockito.mock(RecyclerView.class);

                    List<UserContactInfo> friendList = new ArrayList<>();

                    final UserContactInfo userContactInfo = mock(UserContactInfo.class);
                    when(userContactInfo.getDisplayName()).thenReturn("Name");
                    when(userContactInfo.getProfilePhotoUrl()).thenReturn("https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc");
                    when(userContactInfo.getEmail()).thenReturn("blabla@mail.com");
                    friendList.add(userContactInfo);

                    FriendAdapter adapter = Mockito.mock(FriendAdapter.class);

                    recyclerView.setAdapter(adapter);

                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext, CreateMemoryActivity.class);
                    return intent;
                }
            };
}
