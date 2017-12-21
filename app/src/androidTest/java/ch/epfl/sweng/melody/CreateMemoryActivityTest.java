package ch.epfl.sweng.melody;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.melody.matcherUtil.ToastMatcher;
import ch.epfl.sweng.melody.matcherUtil.ViewMatcher;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CreateMemoryActivityTest {
    @Rule
    public final IntentsTestRule<CreateMemoryActivity> createMemoryActivityIntentsTestRule =
            new IntentsTestRule<>(CreateMemoryActivity.class);
    private final String testVideoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/test%2F1511912988772.mp4?alt=media&token=63b36ece-25b4-4f6f-a664-095a95ce2562";
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";
    private final String CAMERA = "Camera";
    private final String CANCEL = "Cancel";
    private final String ALBUM = "Choose from Album";
    private ToastMatcher toastMatcher;
    private ViewMatcher viewMatcher;

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

        toastMatcher = new ToastMatcher();
        viewMatcher = new ViewMatcher();
    }


    @Test
    public void displayPhotoFromCameraTest() throws Exception {
        onView(withId(R.id.display_chosen_photo)).check(matches(not(viewMatcher.hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(CAMERA)).perform(click());
        intended(hasAction(equalTo(MediaStore.ACTION_IMAGE_CAPTURE)));
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_photo)).check(matches(viewMatcher.hasDrawable()));
    }

    @Test
    public void displayPhotoFromGalleryTest() throws Exception {
        onView(withId(R.id.display_chosen_photo)).check(matches(not(viewMatcher.hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(ALBUM)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_PICK), hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_photo)).check(matches(viewMatcher.hasDrawable()));
    }

    @Test
    public void cancelDisplayPhotoTest() throws Exception {
        onView(withId(R.id.display_chosen_photo)).check(matches(not(viewMatcher.hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(CANCEL)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_photo)).check(matches(not(viewMatcher.hasDrawable())));
    }

    @Test
    public void displayVideoFromCameraTest() throws Exception {
        onView(withId(R.id.display_chosen_video)).check(matches(not(viewMatcher.hasVideo())));
        onView(withId(R.id.take_videos)).perform(click());
        onView(withText(CAMERA)).perform(click());
        intended(hasAction(equalTo(MediaStore.ACTION_VIDEO_CAPTURE)));
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_video)).check(matches(viewMatcher.hasVideo()));
    }

    @Test
    public void displayVideoFromGalleryTest() throws Exception {
        onView(withId(R.id.display_chosen_video)).check(matches(not(viewMatcher.hasVideo())));
        onView(withId(R.id.take_videos)).perform(click());
    }

    @Test
    public void cancelDisplayVideoTest() throws Exception {
        onView(withId(R.id.display_chosen_video)).check(matches(not(viewMatcher.hasVideo())));
        onView(withId(R.id.take_videos)).perform(click());
        onView(withText(CANCEL)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.display_chosen_video)).check(matches(not(viewMatcher.hasVideo())));
    }

    @Test
    public void selectMemoryPrivacyTest() throws Exception {
        onView(withId(R.id.private_memory_button)).perform(click());
        onView(withText("Memory is private!")).inRoot(toastMatcher).check(matches(isDisplayed()));
        Thread.sleep(2000);
        onView(withId(R.id.shared_memory_button)).perform(click());
        onView(withText("Memory is shared!")).inRoot(toastMatcher).check(matches(isDisplayed()));
        Thread.sleep(2000);
        onView(withId(R.id.public_memory_button)).perform(click());
        onView(withText("Memory is public!")).inRoot(toastMatcher).check(matches(isDisplayed()));
        Thread.sleep(2000);
    }

    @Test
    public void sendPhotoMemoryTest() {
        onView(withId(R.id.display_chosen_photo)).check(matches(not(viewMatcher.hasDrawable())));
        onView(withId(R.id.take_photos)).perform(click());
        onView(withText(ALBUM)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_PICK), hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        onView(withId(R.id.memory_send)).perform(click());
        onView(withText("Say something!")).inRoot(toastMatcher).check(matches(isDisplayed()));
        onView(withId(R.id.memory_description)).perform(typeText("Test got text memory"));
        closeSoftKeyboard();
        onView(withId(R.id.memory_send)).perform(click());
    }

    @Test
    public void sendVideoMemoryTest() {
        onView(withId(R.id.display_chosen_video)).check(matches(not(viewMatcher.hasVideo())));
        onView(withId(R.id.take_videos)).perform(click());
        onView(withText(CAMERA)).perform(click());
        intended(hasAction(equalTo(MediaStore.ACTION_VIDEO_CAPTURE)));
        onView(withId(R.id.memory_description)).perform(typeText("Test got text memory"));
        closeSoftKeyboard();
        onView(withId(R.id.memory_send)).perform(click());
    }

    private Instrumentation.ActivityResult photoFromCameraSub() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                createMemoryActivityIntentsTestRule.getActivity().getResources(), R.mipmap.login_button));
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private Instrumentation.ActivityResult photoFromGallerySub() {
        Uri uri = Uri.parse(defaultProfilePhotoUrl);
        Intent intent = new Intent();
        intent.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
    }

    private Instrumentation.ActivityResult videoFromCameraSub() {
        Uri uri = Uri.parse(testVideoUrl);
        Intent intent = new Intent();
        intent.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
    }

    private Instrumentation.ActivityResult videoFromGallerySub() {
        Uri uri = Uri.parse(testVideoUrl);
        Intent intent = new Intent();
        intent.setData(uri);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
    }

}