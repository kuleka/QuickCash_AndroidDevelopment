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
public class PostJobTest {
    @Rule
    public ActivityScenarioRule<PostActivity> myRule = new ActivityScenarioRule<>(PostActivity.class);

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
    public void checkIfDateFieldIsEmpty() {
        onView(withId(R.id.dateField)).perform(typeText(""));
        onView(withId(R.id.expectedDurationField)).perform(typeText("20"));
        onView(withId(R.id.placeField)).perform(typeText("6070 South Street"));
        onView(withId(R.id.salaryField)).perform(typeText("20"));
        pressBack();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.dateField)).check(matches(hasErrorText("Date is required!")));
    }

    @Test
    public void checkIfExpectedDurationFieldIsEmpty() {
        onView(withId(R.id.dateField)).perform(typeText("21/05/2022"));
        onView(withId(R.id.expectedDurationField)).perform(typeText(""));
        onView(withId(R.id.placeField)).perform(typeText("6070 South Street"));
        onView(withId(R.id.salaryField)).perform(typeText("20"));
        pressBack();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.expectedDurationField)).check(matches(hasErrorText("Expected duration is required!")));
    }

    @Test
    public void checkIfEmptyPlaceFieldIsEmpty() {
        onView(withId(R.id.dateField)).perform(typeText("21/05/2022"));
        onView(withId(R.id.expectedDurationField)).perform(typeText("20"));
        onView(withId(R.id.placeField)).perform(typeText(""));
        onView(withId(R.id.salaryField)).perform(typeText("20"));
        pressBack();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.placeField)).check(matches(hasErrorText("Place is required!")));
    }

    @Test
    public void checkIfEmptySalaryFieldIsEmpty() {
        onView(withId(R.id.dateField)).perform(typeText("21/05/2022"));
        onView(withId(R.id.expectedDurationField)).perform(typeText("20"));
        onView(withId(R.id.placeField)).perform(typeText("6070 South Street"));
        onView(withId(R.id.salaryField)).perform(typeText(""));
        pressBack();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.salaryField)).check(matches(hasErrorText("Salary is required!")));
    }

    @Test
    public void checkIfDateIsValid1() {
        onView(withId(R.id.dateField)).perform(typeText("30/02/2022"));
        onView(withId(R.id.expectedDurationField)).perform(typeText("20"));
        onView(withId(R.id.placeField)).perform(typeText("6070 South Street"));
        onView(withId(R.id.salaryField)).perform(typeText("20"));
        pressBack();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.dateField)).check(matches(hasErrorText("Date is invalid!")));
    }

    @Test
    public void checkIfDateIsValid2() {
        onView(withId(R.id.dateField)).perform(typeText("30/02/2021"));
        onView(withId(R.id.expectedDurationField)).perform(typeText("20"));
        onView(withId(R.id.placeField)).perform(typeText("6070 South Street"));
        onView(withId(R.id.salaryField)).perform(typeText("20"));
        pressBack();
        onView(withId(R.id.postJobButton)).perform(click());
        onView(withId(R.id.dateField)).check(matches(hasErrorText("Date is invalid!")));
    }





}
