package org.apache.tiles.request.portlet;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate;
import org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate;

public class EventPortletRequest extends PortletRequest {

    public EventPortletRequest(ApplicationContext applicationContext,
            PortletContext context, EventRequest request,
            EventResponse response) {
        super(applicationContext, context, request, response,
                new StateAwareRequestDelegate(request, response),
                new StateAwareResponseDelegate());
    }
}
