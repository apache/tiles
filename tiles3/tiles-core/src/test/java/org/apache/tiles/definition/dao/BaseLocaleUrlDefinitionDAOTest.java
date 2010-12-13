/**
 *
 */
package org.apache.tiles.definition.dao;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsReader;
import org.apache.tiles.definition.RefreshMonitor;
import org.apache.tiles.definition.digester.DigesterDefinitionsReader;
import org.apache.tiles.request.Request;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link BaseLocaleUrlDefinitionDAO}.
 *
 * @version $Rev$ $Date$
 */
public class BaseLocaleUrlDefinitionDAOTest {

    /**
     * The time (in milliseconds) to wait to be sure that the system updates the
     * modify date of a file.
     */
    private static final int SLEEP_MILLIS = 2000;

    /**
     * The dao to test.
     */
    private BaseLocaleUrlDefinitionDAO dao;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        dao = createMockBuilder(BaseLocaleUrlDefinitionDAO.class).withConstructor().createMock();
    }

    /**
     * Test method for {@link org.apache.tiles.definition.dao.BaseLocaleUrlDefinitionDAO#refreshRequired()}.
     * @throws URISyntaxException If something goes wrong.
     * @throws IOException If something goes wrong.
     * @throws InterruptedException If something goes wrong.
     */
    @Test
    public void testRefreshRequired() throws URISyntaxException, IOException, InterruptedException {
        // Set up multiple data sources.
        Map<String, Attribute> attribs = new HashMap<String, Attribute>();
        attribs.put("testparm", new Attribute("testval"));
        Definition rewriteTest = new Definition("rewrite.test",
                Attribute.createTemplateAttribute("/test.jsp"), attribs);
        expect(dao.getDefinition("rewrite.test", null)).andReturn(rewriteTest);

        replay(dao);
        URL url = this.getClass().getClassLoader().getResource(
                "org/apache/tiles/config/temp-defs.xml");

        URI uri = null;
        String urlPath = null;

        // The following madness is necessary b/c of the way Windows hanndles
        // URLs.
        // We must add a slash to the protocol if Windows does not. But we
        // cannot
        // add a slash to Unix paths b/c they already have one.
        if (url.getPath().startsWith("/")) {
            urlPath = "file:" + url.getPath();
        } else {
            urlPath = "file:/" + url.getPath();
        }

        // The following second madness is necessary b/c sometimes spaces
        // are encoded as '%20', sometimes they are not. For example in
        // Windows 2000 under Eclipse they are encoded, under the prompt of
        // Windows 2000 they are not.
        // It seems to be in the different behaviour of
        // sun.misc.Launcher$AppClassLoader (called under Eclipse) and
        // java.net.URLClassLoader (under maven).
        // And an URL accepts spaces while URIs need '%20'.
        try {
            uri = new URI(urlPath);
        } catch (URISyntaxException e) {
            uri = new URI(urlPath.replaceAll(" ", "%20"));
        }

        List<URL> sourceURLs = new ArrayList<URL>();
        sourceURLs.add(uri.toURL());
        dao.setSourceURLs(sourceURLs);
        DefinitionsReader reader = new DigesterDefinitionsReader();
        dao.setReader(reader);

        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
                + "<!DOCTYPE tiles-definitions PUBLIC "
                + "\"-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN\" "
                + "\"http://tiles.apache.org/dtds/tiles-config_3_0.dtd\">\n\n"
                + "<tiles-definitions>"
                + "<definition name=\"rewrite.test\" template=\"/test.jsp\">"
                + "<put-attribute name=\"testparm\" value=\"testval\"/>"
                + "</definition>" + "</tiles-definitions>";

        File file = new File(uri);
        FileOutputStream fileOut = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                fileOut));
        writer.write(xml);
        writer.close();

        Map<String, String> params = new HashMap<String, String>();
        params.put(DefinitionsFactory.DEFINITIONS_CONFIG, urlPath);
        Request context = createMock(Request.class);
        expect(context.getContext("session")).andReturn(
                new HashMap<String, Object>()).anyTimes();
        expect(context.getRequestLocale()).andReturn(null).anyTimes();
        replay(context);

        Definition definition = dao.getDefinition("rewrite.test",
                null);
        assertNotNull("rewrite.test definition not found.", definition);
        assertEquals("Incorrect initial template value", "/test.jsp",
                definition.getTemplateAttribute().getValue());

        RefreshMonitor reloadable = dao;
        dao.loadDefinitionsFromURL(url);
        assertEquals("Factory should be fresh.", false, reloadable
                .refreshRequired());

        // Make sure the system actually updates the timestamp.
        Thread.sleep(SLEEP_MILLIS);

        // Set up multiple data sources.
        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n"
                + "<!DOCTYPE tiles-definitions PUBLIC "
                + "\"-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN\" "
                + "\"http://tiles.apache.org/dtds/tiles-config_3_0.dtd\">\n\n"
                + "<tiles-definitions>"
                + "<definition name=\"rewrite.test\" template=\"/newtest.jsp\">"
                + "<put-attribute name=\"testparm\" value=\"testval\"/>"
                + "</definition>" + "</tiles-definitions>";

        file = new File(uri);
        fileOut = new FileOutputStream(file);
        writer = new BufferedWriter(new OutputStreamWriter(fileOut));
        writer.write(xml);
        writer.close();
        file = new File(uri);

        assertEquals("Factory should be stale.", true, reloadable
                .refreshRequired());

        verify(context, dao);
    }

    /**
     * Test method for {@link BaseLocaleUrlDefinitionDAO#loadDefinitionsFromURL(URL)}.
     * @throws MalformedURLException If something goes wrong.
     */
    @Test
    public void testLoadDefinitionsFromURLFileNotFound() throws MalformedURLException {
        URL url = new URL("file:///hello/there.txt");
        replay(dao);
        DefinitionsReader reader = createMock(DefinitionsReader.class);
        replay(reader);

        dao.setReader(reader);
        assertNull(dao.loadDefinitionsFromURL(url));
        verify(dao, reader);
    }

}
