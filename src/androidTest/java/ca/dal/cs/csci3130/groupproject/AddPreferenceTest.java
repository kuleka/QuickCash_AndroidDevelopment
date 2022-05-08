package ca.dal.cs.csci3130.groupproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
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
public class AddPreferenceTest {
    @Rule
    public ActivityScenarioRule<PreferenceActivity> myRule = new ActivityScenarioRule<>(PreferenceActivity.class);

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
    public void checkIfSalaryRangeField1IsEmpty() {
        onView(withId(R.id.SalaryRangeField1)).perform(typeText(""));
        onView(withId(R.id.SalaryRangeField2)).perform(typeText("10"));
        pressBack();
        onView(withId(R.id.ExpectedDurationField1)).perform(typeText("5"));
        onView(withId(R.id.ExpectedDurationField2)).perform(typeText("10"));
        pressBack();
        onView(withId(R.id.preferencePage_Confirm)).perform(click());
        onView(withId(R.id.SalaryRangeField1)).check(matches(hasErrorText("Salary range lower bound is required!")));
    }

    @Test
    public void checkIfSalaryRangeField2IsEmpty() {
        onView(withId(R.id.SalaryRangeField1)).perform(typeText("5"));
        onView(withId(R.id.SalaryRangeField2)).perform(typeText(""));
        pressBack();
        onView(withId(R.id.ExpectedDurationField1)).perform(typeText("5"));
        onView(withId(R.id.ExpectedDurationField2)).perform(typeText("10"));
        pressBack();
        onView(withId(R.id.preferencePage_Confirm)).perform(click());
        onView(withId(R.id.SalaryRangeField2)).check(matches(hasErrorText("Salary range upper bound is required!")));
    }

    @Test
    public void checkIfExpectedDurationField1IsEmpty() {
        onView(withId(R.id.SalaryRangeField1)).perform(typeText("5"));
        onView(withId(R.id.SalaryRangeField2)).perform(typeText("10"));
        pressBack();
        onView(withId(R.id.ExpectedDurationField1)).perform(typeText(""));
        onView(withId(R.id.ExpectedDurationField2)).perform(typeText("10"));
        pressBack();
        onView(withId(R.id.preferencePage_Confirm)).perform(click());
        onView(withId(R.id.ExpectedDurationField1)).check(matches(hasErrorText("Expected duration range lower bound is required!")));
    }

    @Test
    public void checkIfExpectedDurationField2IsEmpty() {
        onView(withId(R.id.SalaryRangeField1)).perform(typeText("5"));
        onView(withId(R.id.SalaryRangeField2)).perform(typeText("10"));
        pressBack();
        onView(withId(R.id.ExpectedDurationField1)).perform(typeText("5"));
        onView(withId(R.id.ExpectedDurationField2)).perform(typeText(""));
        pressBack();
        onView(withId(R.id.preferencePage_Confirm)).perform(click());
        onView(withId(R.id.ExpectedDurationField2)).check(matches(hasErrorText("Expected duration range upper bound is required!")));
    }
}
