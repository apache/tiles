package org.apache.tiles.request.portlet;

import javax.portlet.PortletContext;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.MimeResponseDelegate;
import org.apache.tiles.request.portlet.delegate.PortletRequestDelegate;

public class ResourcePortletRequest extends PortletRequest {

    public ResourcePortletRequest(ApplicationContext applicationContext,
            PortletContext context, ResourceRequest request,
            ResourceResponse response) {
        super(applicationContext, context, request, response,
                new PortletRequestDelegate(request), new MimeResponseDelegate(
                        response));
    }
}
