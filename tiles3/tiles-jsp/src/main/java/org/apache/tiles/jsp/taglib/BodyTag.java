package org.apache.tiles.jsp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.jsp.JspModelBody;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;
import org.apache.tiles.template.body.ModelBody;

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
