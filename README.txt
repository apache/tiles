WHAT IS THIS?

This is a standalone version of Tiles, extricated from Struts 1.1. 


HOW IS IT DIFFERENT FROM STRUTS TILES?

There are a few differences between this version of Tiles and Tiles included 
in Struts 1.1.

1. Instead of org.apache.struts.tiles, standalone Tiles uses org.apache.tiles. 
   This means:

   a. Your Tiles servlet declaration in web.xml should look like this:

        <servlet>
           <servlet-name>Tiles Servlet</servlet-name>
           <servlet-class>org.apache.tiles.servlets.TilesServlet</servlet-class>
           ...
           <load-on-startup>1</load-on-startup>
        </servlet>

       (Note the classname for the Tiles servlet: 
        org.apache.tiles.servlets.TilesServlet)

    b. The fully qualified name for Tiles controllers has changed to 
       org.apache.tiles.Controller.

2. Instead of http://jakarta.apache.org/struts/tags-tiles for the tag library 
   uri, standalone Tiles uses http://jakarta.apache.org/tiles. So your taglib 
   declaration should like this: 

      <%@ taglib uri="http://jakarta.apache.org/tiles" %>


HOW DO I GET STARTED?

First, you must build the tiles-core.jar file. Copy build.properties.sample
to build.properties and edit properties as appropriate. Then change to
the core-library directory and do "ant dist". That will create, among other
things, core-library/dist/lib/tiles-core.jar, which you include in the lib
directory of your WAR file.

To get started using Tiles, you'll find an examples/simple directory in  
the top-level directory (in which this file resides). That directory contains
a simple example that illustrates Tiles fundamentals. That application is
documented in tiles.pdf, which is also in the top-level directory.
