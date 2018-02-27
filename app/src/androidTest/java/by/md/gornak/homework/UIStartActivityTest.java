package by.md.gornak.homework;


import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import by.md.gornak.homework.activity.StartActivity;
import by.md.gornak.homework.util.Settings;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class UIStartActivityTest {


    private final Context context = InstrumentationRegistry.getTargetContext();
    @Rule
    public ActivityTestRule<StartActivity> activityRule = new ActivityTestRule<>(StartActivity.class);

    @Before
    public void setPref() {
        if(!Settings.getBooleanValue(context, R.string.pref_key_show_welcome, true)) {
            Intent intent = new Intent();
            Settings.setBooleanValue(context, R.string.pref_key_show_welcome, true);
            Settings.setStringValue(context, R.string.pref_key_light_theme, "false");
            activityRule.launchActivity(intent);
        }
    }

    @Test
    public void firstPageNotNextButton() {
        onView(withId(R.id.nextButton)).check(doesNotExist());
    }

    @Test
    public void swipe() {
        onView(withId(R.id.vpHello)).perform(swipeLeft());
        onView(withId(R.id.light)).check(matches(isDisplayed()));
    }


    @Test
    public void changeTheme() {
        onView(withId(R.id.vpHello)).perform(swipeLeft());
        SystemClock.sleep(1000);
        onView(withId(R.id.darkThemeRb)).perform(click());
        onView(withId(R.id.lightThemeRb)).check(matches(isNotChecked()));
        onView(withId(R.id.darkThemeRb)).check(matches(isChecked()));
        assertEquals(false,
                Boolean.parseBoolean(Settings.getStringValue(context, R.string.pref_key_light_theme)));
    }


    @Test
    public void lastPageNextButton() {
        onView(withId(R.id.vpHello)).perform(swipeLeft()).perform(swipeLeft());
        onView(withId(R.id.nextButton)).check(matches(isDisplayed()));
    }

    @Test
    public void clickNextButton() {
        onView(withId(R.id.vpHello)).perform(swipeLeft()).perform(swipeLeft());
        SystemClock.sleep(1000);

        onView(withId(R.id.nextButton)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }
}
