package ca.dal.cs.csci3130.groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JobUnitTest {

    static Job job;

    @BeforeClass
    public static void setup() {
        job = new Job();
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
    public void checkIsValidDate() {
        assertTrue(job.isValidDate("14/10/2022"));
        assertTrue(job.isValidDate("21/05/2022"));
        assertFalse(job.isValidDate("28/02/2022"));
        assertFalse(job.isValidDate("30/02/2022"));
        assertFalse(job.isValidDate("30/02/2021"));
    }
}
