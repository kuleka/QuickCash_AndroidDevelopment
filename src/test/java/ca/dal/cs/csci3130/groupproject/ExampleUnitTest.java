package ca.dal.cs.csci3130.groupproject;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    static User user;

    @BeforeClass
    public static void setup() {
        user = new User();
    }

    @AfterClass
    public static void tearDown() {
        System.gc();
    }


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkIsValidEmailAddress() {
        assertTrue(user.isValidEmailAddress("123@dal.ca"));
        assertFalse(user.isValidEmailAddress("123dal.ca"));
    }

    @Test
    public void checkIsValidPassword() {
        assertTrue(user.isValidPassword("Ljjcsy521!"));
        assertFalse(user.isValidPassword("ljjcsy521!"));
        assertFalse(user.isValidPassword("ljjcsy521"));
    }

    @Test
    public void checkIsValidConfirmPassword() {
        assertTrue(user.isValidConfirmPassword("Ljjcsy521!", "Ljjcsy521!"));
        assertFalse(user.isValidConfirmPassword("Ljjcsy521!", "ljjcsy521!"));
    }

    @Test
    public void checkIsValidPhoneNumber() {
        assertTrue(user.isValidPhoneNumber("1234567890"));
        assertFalse(user.isValidPhoneNumber("123456789"));
    }


}
