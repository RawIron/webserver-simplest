
package simpleWebServer;

import junit.framework.TestCase;
import simpleWebServer.FileExtensionToContentTypeMapper;


public class FileExtensionMapperTest extends TestCase {

    protected FileExtensionToContentTypeMapper mapper;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public FileExtensionMapperTest() {
        super();
        mapper = new FileExtensionToContentTypeMapper();
    }

    public final void test_gif() {
        assertTrue(mapper.extensionsToContent.get(".gif").equals("image/gif"));
    }
}
