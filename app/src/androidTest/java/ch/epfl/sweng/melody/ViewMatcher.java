package ch.epfl.sweng.melody;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import org.hamcrest.Description;

/**
 * Created by maxwell on 25/10/2017.
 */

class ViewMatcher {
    static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }
    static BoundedMatcher<View, VideoView> hasVideo(){
        return new BoundedMatcher<View, VideoView>(VideoView.class) {
            @Override
            protected boolean matchesSafely(VideoView item) {
                return item.isPlaying();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has video");
            }
        };
    }
}
