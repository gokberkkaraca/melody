package ch.epfl.sweng.melody;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.melody.user.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

public class PublicMemoryActivityTest extends ActivityTest {

    @Rule
    public final IntentsTestRule<PublicMemoryActivity> publicMemoryActivityIntentsTestRule =
            new IntentsTestRule<PublicMemoryActivity>(PublicMemoryActivity.class, false, true);

    @Test
    @Ignore
    public void datePickerWorks() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000);
        onView(withText("Time travel")).perform(click());
        onView(withText("OK")).perform(click());
    }

    @Test
    @Ignore
    public void openFriendsList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Friends")).perform(click());
    }

    @Test
    @Ignore
    public void openUserSearch() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(3000);
        onView(withText("Search Users")).perform(click());
    }

    @Test
    @Ignore
    public void openFriendRequestList() throws Exception {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Thread.sleep(2000);
        onView(withText("Friends Requests")).perform(click());
    }

    @Test
    public void testThemeColor() throws Exception {
        PublicMemoryActivity.ColorCode colorCode;

        colorCode = PublicMemoryActivity.getColorCode("1");
        assertTrue(colorCode.getThemeColor() == User.ThemeColor.RED);
        assertTrue(colorCode.getThemeColorValue() == R.color.red);

        colorCode = PublicMemoryActivity.getColorCode("2");
        assertTrue(colorCode.getThemeColor() == User.ThemeColor.GREEN);
        assertTrue(colorCode.getThemeColorValue() == R.color.green);

        colorCode = PublicMemoryActivity.getColorCode("3");
        assertTrue(colorCode.getThemeColor() == User.ThemeColor.BLUELIGHT);
        assertTrue(colorCode.getThemeColorValue() == R.color.blueLight);

        colorCode = PublicMemoryActivity.getColorCode("4");
        assertTrue(colorCode.getThemeColor() == User.ThemeColor.BLUEDARK);
        assertTrue(colorCode.getThemeColorValue() == R.color.blueDark);

        colorCode = PublicMemoryActivity.getColorCode("5");
        assertTrue(colorCode.getThemeColor() == User.ThemeColor.BLACK);
        assertTrue(colorCode.getThemeColorValue() == R.color.black);

        colorCode = PublicMemoryActivity.getColorCode("11");
        assertTrue(colorCode.getThemeColor() == User.ThemeColor.RED);
        assertTrue(colorCode.getThemeColorValue() == R.color.red);
    }
}

