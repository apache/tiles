package org.apache.tiles.autotag.jsp.runtime;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.tiles.autotag.core.runtime.AbstractModelBody;

public class JspModelBody extends AbstractModelBody {

    private JspFragment jspFragment;

    public JspModelBody(JspFragment jspFragment, JspContext jspContext) {
        super(jspContext.getOut());
        this.jspFragment = jspFragment;
    }

    @Override
    public void evaluate(Writer writer) throws IOException {
        if (jspFragment == null) {
            return;
        }

        try {
            jspFragment.invoke(writer);
        } catch (JspException e) {
            throw new IOException("JspException when evaluating the body", e);
        }
    }

}
