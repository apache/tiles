package org.apache.tiles.request.jsp;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.util.ApplicationContextUtil;

public final class JspUtil {

    private JspUtil() {
    }

    public static ApplicationContext getApplicationContext(JspContext jspContext) {
        return (ApplicationContext) jspContext.getAttribute(
                ApplicationContextUtil.APPLICATION_CONTEXT_ATTRIBUTE,
                PageContext.APPLICATION_SCOPE);
    }
}
