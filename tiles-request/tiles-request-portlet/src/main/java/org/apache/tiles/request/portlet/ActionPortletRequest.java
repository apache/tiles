package org.apache.tiles.request.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.portlet.delegate.StateAwareRequestDelegate;
import org.apache.tiles.request.portlet.delegate.StateAwareResponseDelegate;

public class ActionPortletRequest extends PortletRequest {

    public ActionPortletRequest(ApplicationContext applicationContext,
            PortletContext context, ActionRequest request, ActionResponse response) {
        super(applicationContext, context, request, response,
                new StateAwareRequestDelegate(request, response),
                new StateAwareResponseDelegate());
    }
}
