package org.apache.tiles.autotag.jsp.runtime;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;

public abstract class BodyTag extends SimpleTagSupport {

    @Override
    public void doTag() throws IOException {
        JspContext pageContext = getJspContext();
        Request request = JspRequest.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil
                        .getApplicationContext(pageContext),
                (PageContext) pageContext);
        ModelBody modelBody = new JspModelBody(getJspBody(), pageContext);
        execute(request, modelBody);
    }

    protected abstract void execute(Request request, ModelBody modelBody)
            throws IOException;
}
