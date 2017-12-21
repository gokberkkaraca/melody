package ch.epfl.sweng.melody;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.SeekBar;

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class ShowMapActivityTest {
    @Rule
    public final ActivityTestRule<ShowMapActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(ShowMapActivity.class);
    private Random rng = new Random();

    private static ViewAction clickSeekBar(final int pos) {
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {
                        SeekBar seekBar = (SeekBar) view;
                        final int[] screenPos = new int[2];
                        seekBar.getLocationOnScreen(screenPos);

                        // get the width of the actual bar area by removing padding
                        int trueWidth = seekBar.getWidth()
                                - seekBar.getPaddingLeft() - seekBar.getPaddingRight();

                        // what is the position on a 0-1 scale
                        // add 0.3f to avoid round off to the next smaller position
                        float relativePos = (0.3f + pos) / (float) seekBar.getMax();
                        if (relativePos > 1.0f)
                            relativePos = 1.0f;

                        // determine where to click
                        final float screenX = trueWidth * relativePos + screenPos[0]
                                + seekBar.getPaddingLeft();
                        final float screenY = seekBar.getHeight() / 2f + screenPos[1];

                        return new float[]{screenX, screenY};
                    }
                },
                Press.FINGER);
    }

    @Test
    public void moveSeekBar() throws Exception {
        int cur_progress;

        // do an initial move in case the first random number is 0
        //  -- if it didn't move, the OnSeekBarChangeListener isn't called
        onView(withId(R.id.seekBar)).perform(clickSeekBar(25));

        // try 10 random locations
        for (int i = 0; i < 10; i++) {
            cur_progress = rng.nextInt(101);            // 0..100

            // move it to a random location
            onView(withId(R.id.seekBar)).perform(clickSeekBar(cur_progress));

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                // implement later on
            }
        }
    }

    @Test
    public void onBackPressedTest() {
        pressBack();
    }
}
