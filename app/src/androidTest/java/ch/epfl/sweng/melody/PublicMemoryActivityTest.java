package ch.epfl.sweng.melody;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PublicMemoryActivityTest {
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
    Random rng = new Random();

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
    public void goToPublicMemoryTest() throws Exception{
        onView(withId(R.id.home)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click public memory feed button";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        Thread.sleep(100);
        intended(hasComponent(PublicMemoryActivity.class.getName()));
    }

    @Test
    public void goToCreateNewMemoryTest() throws Exception{
        onView(withId(R.id.plus)).perform(click());
        Thread.sleep(100);
        intended(hasComponent(CreateMemoryActivity.class.getName()));
    }

    @Test
    public void goToUserProfileTest() throws Exception{
        onView(withId(R.id.person)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click user profile button";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        Thread.sleep(100);
        intended(hasComponent(UserProfileActivity.class.getName()));
    }

    @Test
    public void filterByLocation () throws Exception{
        onView(withId(R.id.filterByLocation)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click filter by location button";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

    }

    @Test
    public void moveSeekBar() throws Exception{
        int cur_progress;

        // do an initial move in case the first random number is 0
        //  -- if it didn't move, the OnSeekBarChangeListener isn't called
        onView(withId(R.id.filterByLocation)).perform(click());
        Thread.sleep(200);
        onView(withId(R.id.rediusSeekBar)).perform(clickSeekBar(25));

        // try 10 random locations
        for( int i=0; i<10; i++ ) {
            cur_progress = rng.nextInt(101);            // 0..100

            // move it to a random location
            onView(withId(R.id.rediusSeekBar)).perform(clickSeekBar(cur_progress));

            try {
                Thread.sleep(1000);
            } catch ( Exception e ) {
                // do nothing
            }
            // Check that the text was changed
            Resources str = getInstrumentation().getTargetContext().getResources();
            //str.getString(R.string.showRadiusMessage);
            //String showRadiusMessage = getInstrumentation().getContext().getString(ch.epfl.sweng.melody.R.string.showRadiusMessage);
            //String km = getInstrumentation().getContext().getString(ch.epfl.sweng.melody.R.string.km);
            String expected =str.getString(R.string.showRadiusMessage) + cur_progress + str.getString(R.string.km);
            onView(withId(R.id.radiusValue))
                    .check(matches(withText(expected)));
        }
    }

    public static ViewAction clickSeekBar(final int pos){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        SeekBar seekBar = (SeekBar) view;
                        final int[] screenPos = new int[2];
                        seekBar.getLocationOnScreen(screenPos);

                        // get the width of the actual bar area
                        // by removing padding
                        int trueWidth = seekBar.getWidth()
                                - seekBar.getPaddingLeft() - seekBar.getPaddingRight();

                        // what is the position on a 0-1 scale
                        //  add 0.3f to avoid roundoff to the next smaller position
                        float relativePos = (0.3f + pos)/(float) seekBar.getMax();
                        if ( relativePos > 1.0f )
                            relativePos = 1.0f;

                        // determine where to click
                        final float screenX = trueWidth*relativePos + screenPos[0]
                                + seekBar.getPaddingLeft();
                        final float screenY = seekBar.getHeight()/2f + screenPos[1];
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

}

