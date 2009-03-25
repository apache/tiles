/**
 * 
 */
package org.apache.tiles.template;

import static org.easymock.EasyMock.*;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.junit.Before;
import org.junit.Test;

/**
 * @author antonio
 *
 */
public class InsertTemplateModelTest {

    /**
     * The model to test.
     */
    private InsertTemplateModel model;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        model = new InsertTemplateModel();
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel#start(org.apache.tiles.TilesContainer, java.lang.Object[])}.
     */
    @Test
    public void testStart() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        
        expect(container.startContext(requestItem)).andReturn(attributeContext);
        
        replay(container, attributeContext);
        model.start(container, requestItem);
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel#end(TilesContainer, String, String, String, Object...)}.
     */
    @Test
    public void testEnd() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        container.endContext(requestItem);
        attributeContext.setPreparer("myPreparer");
        attributeContext.setTemplateAttribute((Attribute) notNull());
        container.renderContext(requestItem);
        
        replay(container, attributeContext);
        model.end(container, "myTemplate", "myRole", "myPreparer", requestItem);
        verify(container, attributeContext);
    }

    /**
     * Test method for {@link org.apache.tiles.template.InsertTemplateModel#execute(TilesContainer, String, String, String, Object...)}.
     */
    @Test
    public void testExecute() {
        TilesContainer container = createMock(TilesContainer.class);
        Integer requestItem = new Integer(1);
        AttributeContext attributeContext = createMock(AttributeContext.class);
        
        expect(container.startContext(requestItem)).andReturn(attributeContext);
        expect(container.getAttributeContext(requestItem)).andReturn(attributeContext);
        container.endContext(requestItem);
        attributeContext.setPreparer("myPreparer");
        attributeContext.setTemplateAttribute((Attribute) notNull());
        container.renderContext(requestItem);
        
        replay(container, attributeContext);
        model.execute(container, "myTemplate", "myRole", "myPreparer", requestItem);
        verify(container, attributeContext);
    }

}
