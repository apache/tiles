/*
 * $Id$ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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


package org.apache.struts.tiles;


import javax.servlet.ServletException;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;
import org.apache.struts.config.PlugInConfig;
import org.apache.struts.mock.MockActionServlet;
import org.apache.struts.mock.TestMockBase;
import org.apache.struts.util.RequestUtils;

/**
 * <p>Unit tests for <code>org.apache.struts.tiles.*</code>.</p>
 *
 * @version $Rev$ $Date$
 */

public class TestTilesPlugin extends TestMockBase {


  protected ModuleConfig module1;
  protected ModuleConfig module2;
  protected MockActionServlet actionServlet;

    // ----------------------------------------------------------------- Basics


    public TestTilesPlugin(String name) {
        super(name);
    }


    public static void main(String args[]) {
        junit.awtui.TestRunner.main
            (new String[] { TestTilesPlugin.class.getName() } );
    }


    public static Test suite() {
        return (new TestSuite(TestTilesPlugin.class));
    }


    // ----------------------------------------------------- Instance Variables



    // ----------------------------------------------------- Setup and Teardown


    public void setUp()
    {

    super.setUp();
    TilesUtil.testReset();
    actionServlet = new MockActionServlet(context, config);
    }


    public void tearDown() {

        super.tearDown();

    }


    // ------------------------------------------------------- Individual Tests


    /**
     * Create a module configuration
     * @param moduleName
     */
    public ModuleConfig createModuleConfig(
    	String moduleName,
    	String configFileName,
    	boolean moduleAware) {
            
    	ModuleConfig moduleConfig =
    		ModuleConfigFactory.createFactory().createModuleConfig(moduleName);
            
    	context.setAttribute(Globals.MODULE_KEY + moduleName, moduleConfig);
    
    	// Set tiles plugin
    	PlugInConfig pluginConfig = new PlugInConfig();
    	pluginConfig.setClassName("org.apache.struts.tiles.TilesPlugin");
        
    	pluginConfig.addProperty(
    		"moduleAware",
    		(moduleAware == true ? "true" : "false"));
            
    	pluginConfig.addProperty(
    		"definitions-config",
    		"/org/apache/struts/tiles/config/" + configFileName);
            
    	moduleConfig.addPlugInConfig(pluginConfig);
    	return moduleConfig;
    }

    /**
     * Fake call to init module plugins
     * @param config
     */
  public void initModulePlugIns( ModuleConfig moduleConfig)
  {
  PlugInConfig plugInConfigs[] = moduleConfig.findPlugInConfigs();
  PlugIn plugIns[] = new PlugIn[plugInConfigs.length];

  context.setAttribute(Globals.PLUG_INS_KEY + moduleConfig.getPrefix(), plugIns);
  for (int i = 0; i < plugIns.length; i++) {
      try {
          plugIns[i] =
              (PlugIn)RequestUtils.applicationInstance(plugInConfigs[i].getClassName());
          BeanUtils.populate(plugIns[i], plugInConfigs[i].getProperties());
            // Pass the current plugIn config object to the PlugIn.
            // The property is set only if the plugin declares it.
            // This plugin config object is needed by Tiles
          BeanUtils.copyProperty( plugIns[i], "currentPlugInConfigObject", plugInConfigs[i]);
          plugIns[i].init(actionServlet, moduleConfig);
      } catch (ServletException e) {
          // Lets propagate
          e.printStackTrace();
          //throw e;
      } catch (Exception e) {
          e.printStackTrace();
          //throw e;
      }
  }
  }

    // ---------------------------------------------------------- absoluteURL()


    /**
     * Test multi factory creation when moduleAware=true.
     */
    public void testMultiFactory() {
    	// init TilesPlugin
    	module1 = createModuleConfig("/module1", "tiles-defs.xml", true);
    	module2 = createModuleConfig("/module2", "tiles-defs.xml", true);
    	initModulePlugIns(module1);
    	initModulePlugIns(module2);
    
    	// mock request context
    	request.setAttribute(Globals.MODULE_KEY, module1);
    	request.setPathElements("/myapp", "/module1/foo.do", null, null);
    	// Retrieve factory for module1
    	DefinitionsFactory factory1 =
    		TilesUtil.getDefinitionsFactory(request, context);
            
    	assertNotNull("factory found", factory1);
    	assertEquals(
    		"factory name",
    		"/module1",
    		factory1.getConfig().getFactoryName());
    
    	// mock request context
    	request.setAttribute(Globals.MODULE_KEY, module2);
    	request.setPathElements("/myapp", "/module2/foo.do", null, null);
    	// Retrieve factory for module2
    	DefinitionsFactory factory2 =
    		TilesUtil.getDefinitionsFactory(request, context);
    	assertNotNull("factory found", factory2);
    	assertEquals(
    		"factory name",
    		"/module2",
    		factory2.getConfig().getFactoryName());
    
    	// Check that factory are different
    	assertNotSame("Factory from different modules", factory1, factory2);
    }

    /**
     * Test single factory creation when moduleAware=false.
     */
  public void testSingleSharedFactory()
  {
    // init TilesPlugin
  module1 = createModuleConfig( "/module1", "tiles-defs.xml", false );
  module2 = createModuleConfig( "/module2", "tiles-defs.xml", false );
  initModulePlugIns(module1);
  initModulePlugIns(module2);

    // mock request context
  request.setAttribute(Globals.MODULE_KEY, module1);
  request.setPathElements("/myapp", "/module1/foo.do", null, null);
    // Retrieve factory for module1
  DefinitionsFactory factory1 = TilesUtil.getDefinitionsFactory( request, context);
  assertNotNull( "factory found", factory1);
  assertEquals( "factory name", "/module1", factory1.getConfig().getFactoryName() );

    // mock request context
  request.setAttribute(Globals.MODULE_KEY, module2);
  request.setPathElements("/myapp", "/module2/foo.do", null, null);
    // Retrieve factory for module2
  DefinitionsFactory factory2 = TilesUtil.getDefinitionsFactory( request, context);
  assertNotNull( "factory found", factory2);
  assertEquals( "factory name", "/module1", factory2.getConfig().getFactoryName() );

    // Check that factory are different
  assertEquals("Same factory", factory1, factory2);
  }


}

