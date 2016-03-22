
package server;

import junit.framework.TestCase;
import static server.HttpConstants.*;


public class HttpConstantsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public HttpConstantsTest() {
        super();
    }

    public final void test_httpCode300() {
        assertEquals(HTTP_MULT_CHOICE, 300);
    }
}
