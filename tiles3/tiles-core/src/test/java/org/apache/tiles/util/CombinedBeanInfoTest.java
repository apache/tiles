/**
 *
 */
package org.apache.tiles.util;

import static org.junit.Assert.*;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.reflect.ClassUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link CombinedBeanInfo}.
 *
 * @version $Rev$ $Date$
 */
public class CombinedBeanInfoTest {

    /**
     * The bean info to test.
     */
    private CombinedBeanInfo beanInfo;

    /**
     * The property descriptors.
     */
    private List<FeatureDescriptor> descriptors;

    /**
     * The map of property descriptors for request.
     */
    private Map<String, PropertyDescriptor> requestMap;

    /**
     * The map of property descriptors for application.
     */
    private Map<String, PropertyDescriptor> applicationMap;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        beanInfo = new CombinedBeanInfo(Request.class, ApplicationContext.class);
        requestMap = new LinkedHashMap<String, PropertyDescriptor>();
        ClassUtil.collectBeanInfo(Request.class, requestMap);
        applicationMap = new LinkedHashMap<String, PropertyDescriptor>();
        ClassUtil.collectBeanInfo(ApplicationContext.class, applicationMap);
        descriptors = new ArrayList<FeatureDescriptor>();
        descriptors.addAll(requestMap.values());
        descriptors.addAll(applicationMap.values());
    }

    /**
     * Test method for {@link org.apache.tiles.util.CombinedBeanInfo#getDescriptors()}.
     */
    @Test
    public void testGetDescriptors() {
        assertEquals(descriptors, beanInfo.getDescriptors());
    }

    /**
     * Test method for {@link org.apache.tiles.util.CombinedBeanInfo#getMappedDescriptors(java.lang.Class)}.
     */
    @Test
    public void testGetMappedDescriptors() {
        assertEquals(requestMap, beanInfo.getMappedDescriptors(Request.class));
        assertEquals(applicationMap, beanInfo.getMappedDescriptors(ApplicationContext.class));
    }

    /**
     * Test method for {@link org.apache.tiles.util.CombinedBeanInfo#getProperties(java.lang.Class)}.
     */
    @Test
    public void testGetProperties() {
        assertEquals(requestMap.keySet(), beanInfo.getProperties(Request.class));
        assertEquals(applicationMap.keySet(), beanInfo.getProperties(ApplicationContext.class));
    }

}
