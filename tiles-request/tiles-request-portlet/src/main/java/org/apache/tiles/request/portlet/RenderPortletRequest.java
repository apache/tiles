package org.apache.tiles.request.portlet;

import javax.portlet.PortletContext;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.MimeResponseDelegate;
import org.apache.tiles.request.portlet.delegate.PortletRequestDelegate;

public class RenderPortletRequest extends PortletRequest {

    public RenderPortletRequest(ApplicationContext applicationContext,
            PortletContext context, RenderRequest request,
            RenderResponse response) {
        super(applicationContext, context, request, response,
                new PortletRequestDelegate(request), new MimeResponseDelegate(
                        response));
    }
}
