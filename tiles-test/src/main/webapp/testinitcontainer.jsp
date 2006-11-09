<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:destroyContainer/>
<tiles:initContainer containerFactory="org.apache.tiles.factory.TilesContainerFactory">
    <tiles:put name="definitions-config" value="/WEB-INF/tiles-defs.xml,/org/apache/tiles/classpath-defs.xml"/>
    <tiles:put name="org.apache.tiles.CONTEXT_FACTORY"
               value="org.apache.tiles.context.enhanced.EnhancedContextFactory"/>
    <tiles:put name="org.apache.tiles.CONTAINER_FACTORY.mutable"
               value="true"/>
</tiles:initContainer>

<tiles:insertDefinition name="test.definition" />
