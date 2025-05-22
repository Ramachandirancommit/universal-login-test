package com.neptune;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {
    public AppTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(LoginTest.class)); // <== No import needed
        suite.addTest(new TestSuite(InvalidLoginTest.class)); // if exists
        return suite;
    }

    public void testApp() {
        assertTrue(true);
    }
}
