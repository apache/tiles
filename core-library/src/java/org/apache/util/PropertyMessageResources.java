/*
 * Copyright 2004-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Concrete subclass of <code>MessageResources</code> that reads message keys
 * and corresponding strings from named property resources in the same manner
 * that <code>java.util.PropertyResourceBundle</code> does.  The
 * <code>base</code> property defines the base property resource name, and
 * must be specified.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - This class trades memory for
 * speed by caching all messages located via generalizing the Locale under
 * the original locale as well.
 * This results in specific messages being stored in the message cache
 * more than once, but improves response time on subsequent requests for
 * the same locale + key combination.
 *
 * @author Craig R. McClanahan
 * @author David Graham
 * @version $Revision: 1.8 $ $Date: 2003/04/19 19:06:02 $
 */
public class PropertyMessageResources extends MessageResources {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new PropertyMessageResources according to the
     * specified parameters.
     *
     * @param factory The MessageResourcesFactory that created us
     * @param config The configuration parameter for this MessageResources
     */
    public PropertyMessageResources(MessageResourcesFactory factory,
                                    String config) {

        super(factory, config);
        log.info("Initializing, config='" + config + "'");

    }


    /**
     * Construct a new PropertyMessageResources according to the
     * specified parameters.
     *
     * @param factory The MessageResourcesFactory that created us
     * @param config The configuration parameter for this MessageResources
     * @param returnNull The returnNull property we should initialize with
     */
    public PropertyMessageResources(MessageResourcesFactory factory,
                                    String config, boolean returnNull) {

        super(factory, config, returnNull);
        log.info("Initializing, config='" + config +
                 "', returnNull=" + returnNull);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The set of locale keys for which we have already loaded messages, keyed
     * by the value calculated in <code>localeKey()</code>.
     */
    protected HashMap locales = new HashMap();


    /**
     * The <code>Log</code> instance for this class.
     */
    protected static final Log log =
        LogFactory.getLog(PropertyMessageResources.class);


    /**
     * The cache of messages we have accumulated over time, keyed by the
     * value calculated in <code>messageKey()</code>.
     */
    protected HashMap messages = new HashMap();


    // --------------------------------------------------------- Public Methods


    /**
     * Returns a text message for the specified key, for the default Locale.
     * A null string result will be returned by this method if no relevant
     * message resource is found for this key or Locale, if the
     * <code>returnNull</code> property is set.  Otherwise, an appropriate
     * error message will be returned.
     * <p>
     * This method must be implemented by a concrete subclass.
     *
     * @param locale The requested message Locale, or <code>null</code>
     *  for the system default Locale
     * @param key The message key to look up
     * @return text message for the specified key and locale
     */
    public String getMessage(Locale locale, String key) {

        if (log.isDebugEnabled()) {
            log.debug("getMessage(" + locale + "," + key + ")");
        }

        // Initialize variables we will require
        String localeKey = localeKey(locale);
        String originalKey = messageKey(localeKey, key);
        String messageKey = null;
        String message = null;
        int underscore = 0;
        boolean addIt = false;  // Add if not found under the original key

        // Loop from specific to general Locales looking for this message
        while (true) {

            // Load this Locale's messages if we have not done so yet
            loadLocale(localeKey);

            // Check if we have this key for the current locale key
            messageKey = messageKey(localeKey, key);
            synchronized (messages) {
                message = (String) messages.get(messageKey);
                if (message != null) {
                    if (addIt) {
                        messages.put(originalKey, message);
                    }
                    return (message);
                }
            }

            // Strip trailing modifiers to try a more general locale key
            addIt = true;
            underscore = localeKey.lastIndexOf("_");
            if (underscore < 0) {
                break;
            }
            localeKey = localeKey.substring(0, underscore);

        }

        // Try the default locale if the current locale is different
        if (!defaultLocale.equals(locale)) {
            localeKey = localeKey(defaultLocale);
            messageKey = messageKey(localeKey, key);
            loadLocale(localeKey);
            synchronized (messages) {
                message = (String) messages.get(messageKey);
                if (message != null) {
                    messages.put(originalKey, message);
                    return (message);
                }
            }
        }

        // As a last resort, try the default Locale
        localeKey = "";
        messageKey = messageKey(localeKey, key);
        loadLocale(localeKey);
        synchronized (messages) {
            message = (String) messages.get(messageKey);
            if (message != null) {
                messages.put(originalKey, message);
                return (message);
            }
        }

        // Return an appropriate error indication
        if (returnNull) {
            return (null);
        } else {
            return ("???" + messageKey(locale, key) + "???");
        }

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Load the messages associated with the specified Locale key.  For this
     * implementation, the <code>config</code> property should contain a fully
     * qualified package and resource name, separated by periods, of a series
     * of property resources to be loaded from the class loader that created
     * this PropertyMessageResources instance.  This is exactly the same name
     * format you would use when utilizing the
     * <code>java.util.PropertyResourceBundle</code> class.
     *
     * @param localeKey Locale key for the messages to be retrieved
     */
    protected synchronized void loadLocale(String localeKey) {

        if (log.isTraceEnabled()) {
            log.trace("loadLocale(" + localeKey + ")");
        }
        
        // Have we already attempted to load messages for this locale?
        if (locales.get(localeKey) != null) {
            return;
        }
        locales.put(localeKey, localeKey);

        // Set up to load the property resource for this locale key, if we can
        String name = config.replace('.', '/');
        if (localeKey.length() > 0) {
            name += "_" + localeKey;
        }
        name += ".properties";
        InputStream is = null;
        Properties props = new Properties();

        // Load the specified property resource
        if (log.isTraceEnabled()) {
            log.trace("  Loading resource '" + name + "'");
        }
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        
        is = classLoader.getResourceAsStream(name);
        if (is != null) {
            try {
                props.load(is);
                
            } catch (IOException e) {
                log.error("loadLocale()", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("loadLocale()", e);
                }
            }
        }
        
        if (log.isTraceEnabled()) {
            log.trace("  Loading resource completed");
        }

        // Copy the corresponding values into our cache
        if (props.size() < 1) {
            return;
        }
        
        synchronized (messages) {
            Iterator names = props.keySet().iterator();
            while (names.hasNext()) {
                String key = (String) names.next();
                if (log.isTraceEnabled()) {
                    log.trace("  Saving message key '" + messageKey(localeKey, key));
                }
                messages.put(messageKey(localeKey, key), props.getProperty(key));
            }
        }

    }


}
