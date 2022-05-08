package ca.dal.cs.csci3130.groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.action.ViewActions;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PaySalaryTest {

    @Rule
    public ActivityScenarioRule<PaySalaryActivity> myRule = new ActivityScenarioRule<>(PaySalaryActivity.class);

    @AfterClass
    public static void tearDown() {
        System.gc();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ca.dal.cs.csci3130.groupproject", appContext.getPackageName());
    }

    @Test
    public void checkIfAmountIsEmpty() {
        onView(withId(R.id.edtAmount)).perform(typeText(""));
        onView(withId(R.id.btnPayNow)).perform(click());
        onView(withId(R.id.edtAmount)).check(matches(hasErrorText("Empty amount to be paid!")));
    }

}
