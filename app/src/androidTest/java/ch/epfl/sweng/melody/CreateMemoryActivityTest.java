package ch.epfl.sweng.melody;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.melody.ViewMatcher.hasDrawable;
import static ch.epfl.sweng.melody.ViewMatcher.hasVideo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static org.hamcrest.Matchers.equalTo;
/**
 * Created by maxwell on 25/10/2017.
 */

@RunWith(AndroidJUnit4.class)
public class CreateMemoryActivityTest {
    private final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/tests%2F1508935737477.mp4?alt=media&token=5a33aae6-a8c6-46c1-9add-181b0ef258c3";
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/firebase-melody.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=0492b3f5-7e97-4c87-a3b3-f7602eb94abc";
    private final String CAMERA = "Camera";
    private final String ALBUM = "Choose from Album";
    private final String CANCEL = "Cancel";
    @Rule
    public final IntentsTestRule<CreateMemoryActivity> createMemoryActivityIntentsTestRule =
            new IntentsTestRule<CreateMemoryActivity>(CreateMemoryActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    User user;
                    final GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
                    when(googleSignInAccount.getId()).thenReturn("QWERTYU");
                    when(googleSignInAccount.getGivenName()).thenReturn("Jiacheng");
                    when(googleSignInAccount.getFamilyName()).thenReturn("Xu");
                    when(googleSignInAccount.getDisplayName()).thenReturn("Jiacheng Xu");
                    when(googleSignInAccount.getEmail()).thenReturn("jiacheng.xu@epfl.ch");
                    when(googleSignInAccount.getPhotoUrl()).thenReturn(Uri.parse(defaultProfilePhotoUrl));
                    user = new User(googleSignInAccount);

                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent intent = new Intent(targetContext,CreateMemoryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("USER", user);
                    intent.putExtras(bundle);
                    return intent;
                }
            };
    @Before
    public void prepareIntent() {
        Instrumentation.ActivityResult photoCameraResult = photoFromCameraSub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(photoCameraResult);

        Instrumentation.ActivityResult photoGalleryResult = photoFromGallerySub();
        intending(allOf(hasAction(Intent.ACTION_PICK), hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI))).respondWith(photoGalleryResult);

        Instrumentation.ActivityResult videoCameraResult = videoFromCameraSub();
        intending(hasAction(MediaStore.ACTION_VIDEO_CAPTURE)).respondWith(videoCameraResult);

        Instrumentation.ActivityResult videoGalleryResult = videoFromGallerySub();
        intending(allOf(hasAction(Intent.ACTION_PICK), hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI))).respondWith(videoGalleryResult);
    }


    @Test
    public void displayPhotoFromCameraTest() throws Exception{
        onView(withId(R.id.display_chosen_photo)).check(matches(not(hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(CAMERA)).perform(click());
        intended(hasAction(equalTo(MediaStore.ACTION_IMAGE_CAPTURE)));
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_photo)).check(matches(hasDrawable()));
    }

    @Test
    public void displayPhotoFromGalleryTest() throws Exception{
        onView(withId(R.id.display_chosen_photo)).check(matches(not(hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(ALBUM)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_PICK),hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_photo)).check(matches(hasDrawable()));
    }

    @Test
    public void cancelDisplayPhotoTest() throws Exception{
        onView(withId(R.id.display_chosen_photo)).check(matches(not(hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(CANCEL)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_photo)).check(matches(not(hasDrawable())));
    }

    @Test
    public void displayVideoFromCameraTest() throws Exception{
        onView(withId(R.id.display_chosen_video)).check(matches(not(hasVideo())));
        onView(withId(R.id.take_videos)).perform(click());
        onView(withText(CAMERA)).perform(click());
        intended(hasAction(equalTo(MediaStore.ACTION_VIDEO_CAPTURE)));
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_video)).check(matches(hasVideo()));
    }

//    @Test
//    public void displayVideoFromGalleryTest() throws Exception{
//        onView(withId(R.id.display_chosen_video)).check(matches(not(hasVideo())));
//        onView(withId(R.id.take_videos)).perform(click());
//        onView(withText(ALBUM)).perform(click());
//        intended(allOf(hasAction(Intent.ACTION_PICK),hasData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)));
//        Thread.sleep(3000);
//        onView(withId(R.id.display_chosen_video)).check(matches(hasVideo()));
//    }

    @Test
    public void cancelDisplayVideoTest() throws Exception{
        onView(withId(R.id.display_chosen_video)).check(matches(not(hasVideo())));
        onView(withId(R.id.take_videos)).perform(click());
        onView(withText(CANCEL)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_video)).check(matches(not(hasVideo())));
    }

    @Test
    public void pickAudioDialogTest() throws Exception{
        onView(withId(R.id.record_audio)).perform(click());
        onView(withText("Record")).perform(click());
        pressBack();
        onView(withId(R.id.record_audio)).perform(click());
        onView(withText(CANCEL)).perform(click());
    }

    private Instrumentation.ActivityResult photoFromCameraSub() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                createMemoryActivityIntentsTestRule.getActivity().getResources(), R.drawable.login_button));
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private Instrumentation.ActivityResult photoFromGallerySub(){
        Uri uri = Uri.parse(defaultProfilePhotoUrl);
        Intent intent = new Intent();
        intent.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK,intent);
    }

    private Instrumentation.ActivityResult videoFromCameraSub(){
        Uri uri = Uri.parse(testVideoUrl);
        Intent intent = new Intent();
        intent.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK,intent);
    }

    private Instrumentation.ActivityResult videoFromGallerySub(){
        Uri uri = Uri.parse(testVideoUrl);
        Intent intent = new Intent();
        intent.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK,intent);
    }

}