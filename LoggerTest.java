
package simpleWebServer;

import junit.framework.TestCase;
import simpleWebServer.Logger;


public class LoggerTest extends TestCase {

    protected Logger logger;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public LoggerTest() {
        super();
        logger = new SimpleLogger();
    }

    public final void test_oneMessageLogged() {
        logger.log("a simple message");
        assertEquals(logger.totalMessagesLogged, 1);
    }
}
