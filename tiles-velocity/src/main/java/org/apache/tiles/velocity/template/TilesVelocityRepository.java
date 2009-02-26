package org.apache.tiles.velocity.template;

import javax.servlet.ServletContext;

import org.apache.tiles.template.AttributeResolver;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.GetAsStringModel;

public class TilesVelocityRepository {

    private GetAsStringVModel getAsString;
    
    public TilesVelocityRepository(ServletContext servletContext) {
        AttributeResolver attributeResolver = new DefaultAttributeResolver();
        getAsString = new GetAsStringVModel(new GetAsStringModel(
                attributeResolver), servletContext);
    }
    
    public GetAsStringVModel getGetAsString() {
        return getAsString;
    }
}
