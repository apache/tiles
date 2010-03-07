package org.apache.tiles.autotag.jsp.runtime;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.jsp.JspRequest;

public abstract class BodylessTag extends SimpleTagSupport {

    @Override
    public void doTag() throws IOException {
        JspContext pageContext = getJspContext();
        Request request = JspRequest.createServletJspRequest(
                org.apache.tiles.request.jsp.JspUtil
                        .getApplicationContext(pageContext),
                (PageContext) pageContext);
        execute(request);
    }

    protected abstract void execute(Request request) throws IOException;
}
