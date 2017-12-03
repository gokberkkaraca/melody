package ch.epfl.sweng.melody.matcherUtil;


import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import org.hamcrest.Description;

public class ViewMatcher {
    public BoundedMatcher<View, ImageView> hasDrawable() {
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

    public BoundedMatcher<View, VideoView> hasVideo() {
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