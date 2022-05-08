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
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<SignUpActivity> myRule = new ActivityScenarioRule<>(SignUpActivity.class);

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
    public void checkIfEmailAddressIsEmpty() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText(""));
        onView(withId(R.id.passwordField)).perform(typeText("200154Cjh@"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("200154Cjh@"));
        onView(withId(R.id.phoneNumberField)).perform(typeText("1234567890"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.emailAddressField)).check(matches(hasErrorText("Email address is required!")));
    }

    @Test
    public void checkIfEmailAddressIsValid() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText("urhnfjcns.ff"));
        onView(withId(R.id.passwordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.phoneNumberField)).perform(typeText("9024235609"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());

        onView(withId(R.id.emailAddressField)).check(matches(hasErrorText("Email address is invalid!")));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText("iu789909@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText(""));
        onView(withId(R.id.confirmPasswordField)).perform(typeText(""));
        onView(withId(R.id.phoneNumberField)).perform(typeText("9024235609"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.passwordField)).check(matches(hasErrorText("Password is required!")));
    }

    @Test
    public void checkIfConfirmPasswordIsEmpty() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText("iu789909@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText(""));
        onView(withId(R.id.phoneNumberField)).perform(typeText("9024235609"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.confirmPasswordField)).check(matches(hasErrorText("Confirmed password is required!")));
    }

    @Test
    public void checkIfPasswordIsMatched() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText("jh816494@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText("200154Cjh@"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("200254Cjh@"));
        onView(withId(R.id.phoneNumberField)).perform(typeText("9024235609"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.confirmPasswordField)).check(matches(hasErrorText("Confirmed password is not equal to password")));
    }

    @Test
    public void checkIfFirstNameIsEmpty() {
        onView(withId(R.id.lastNameField)).perform(typeText(("qwertyu")));
        onView(withId(R.id.emailAddressField)).perform(typeText("iu789909@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.phoneNumberField)).perform(typeText("9024235609"));
        pressBack();
        onView(withId(R.id.signUp)).perform(ViewActions.click());
        onView(withId(R.id.firstNameField)).check(matches(hasErrorText("First name is required!")));
    }

    @Test
    public void checkIfLastNameIsEmpty() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("")));
        onView(withId(R.id.emailAddressField)).perform(typeText("iu789909@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.phoneNumberField)).perform(typeText("9024235609"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.lastNameField)).check(matches(hasErrorText("Last name is required!")));
    }

    @Test
    public void checkIfPhoneNumberEmpty() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText("iu789909@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("mkl1122..?"));
        onView(withId(R.id.phoneNumberField)).perform(typeText(""));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.phoneNumberField)).check(matches(hasErrorText("Phone number is required!")));
    }

    @Test
    public void checkIfPhoneNumberIsValid() {
        onView(withId(R.id.firstNameField)).perform(typeText("qwertyu"));
        onView(withId(R.id.lastNameField)).perform(typeText(("asdfgh")));
        onView(withId(R.id.emailAddressField)).perform(typeText("iu789909@dal.ca"));
        onView(withId(R.id.passwordField)).perform(typeText("200154Cjh@"));
        onView(withId(R.id.confirmPasswordField)).perform(typeText("200154Cjh@"));
        onView(withId(R.id.phoneNumberField)).perform(typeText("90242356098888888"));
        pressBack();
        onView(withId(R.id.signUp)).perform(click());
        onView(withId(R.id.phoneNumberField)).check(matches(hasErrorText("Phone number is invalid!")));
    }
}
