package aws.samples.sigv4a;

import com.sigv4aSigning.SigV4ASign;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class SigV4ASignTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SigV4ASignTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SigV4ASignTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testSigV4ASign() {
        SigV4ASign.create();
        assertTrue(true);
    }
}
