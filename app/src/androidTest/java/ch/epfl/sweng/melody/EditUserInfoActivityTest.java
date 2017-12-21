package ch.epfl.sweng.melody;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

public class EditUserInfoActivityTest extends ActivityTest{

    @Rule
    public final IntentsTestRule<EditUserInfoActivity> editUserInfoActivityIntentsTestRule =
            new IntentsTestRule<>(EditUserInfoActivity.class);
    private final String defaultProfilePhotoUrl = "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=c417d908-030f-421f-885f-ea8510267a91";
    private final String biography = "";
    private final String CAMERA = "Camera";
    private final String CANCEL = "Cancel";
    private final String ALBUM = "Choose from Album";
    private ViewMatcher viewMatcher;

    @Before
    public void prepareIntent() {
        Instrumentation.ActivityResult photoCameraResult = photoFromCameraSub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(photoCameraResult);

        Instrumentation.ActivityResult photoGalleryResult = photoFromGallerySub();
        intending(allOf(hasAction(Intent.ACTION_PICK), hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI))).respondWith(photoGalleryResult);

        viewMatcher = new ViewMatcher();

    }


    @Test
    public void displayPhotoFromCameraTest() throws Exception {
//        onView(withId(R.id.profile_image)).check(matches(viewMatcher.hasDrawable()));
        onView(withId(R.id.profile_image)).perform(click());
        onView(withText(CAMERA)).perform(click());
        intended(hasAction(equalTo(MediaStore.ACTION_IMAGE_CAPTURE)));
        Thread.sleep(3000);
        onView(withId(R.id.profile_image)).check(matches(viewMatcher.hasDrawable()));
    }

    @Test
    public void displayPhotoFromGalleryTest() throws Exception {
        onView(withId(R.id.profile_image)).check(matches(viewMatcher.hasDrawable()));
        onView(withId(R.id.profile_image)).perform(click());
        onView(withText(ALBUM)).perform(click());
        intended(allOf(hasAction(Intent.ACTION_PICK), hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        Thread.sleep(3000);
        onView(withId(R.id.profile_image)).check(matches(viewMatcher.hasDrawable()));
    }

    @Test
    public void cancelDisplayPhotoTest() throws Exception {
        onView(withId(R.id.profile_image)).check(matches(viewMatcher.hasDrawable()));
        onView(withId(R.id.profile_image)).perform(click());
        onView(withText(CANCEL)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.profile_image)).check(matches(viewMatcher.hasDrawable()));
    }

    private Instrumentation.ActivityResult photoFromCameraSub() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                editUserInfoActivityIntentsTestRule.getActivity().getResources(), R.mipmap.login_button));
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
}
