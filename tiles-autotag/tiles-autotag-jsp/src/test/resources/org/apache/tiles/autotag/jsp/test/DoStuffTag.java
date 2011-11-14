package org.apache.tiles.autotag.jsp.test;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.tiles.autotag.core.runtime.ModelBody;
import org.apache.tiles.autotag.core.runtime.AutotagRuntime;
import org.apache.tiles.request.Request;

/**
 * Documentation of the DoStuff class.
 */
public class DoStuffTag extends SimpleTagSupport {

    /**
     * The template model.
     */
    private org.apache.tiles.autotag.template.DoStuffTemplate model = new org.apache.tiles.autotag.template.DoStuffTemplate();

    /**
     * Parameter one.
     */
    private java.lang.String one;

    /**
     * Parameter two.
     */
    private int two;

    /**
     * Parameter three.
     */
    private boolean three;

    /**
     * Getter for one property.
     *
     * @return
     * Parameter one.
     */
    public java.lang.String getOne() {
        return one;
    }

    /**
     * Setter for one property.
     *
     * @param one
     * Parameter one.
     */
    public void setOne(java.lang.String one) {
        this.one = one;
    }

    /**
     * Getter for two property.
     *
     * @return
     * Parameter two.
     */
    public int getTwo() {
        return two;
    }

    /**
     * Setter for two property.
     *
     * @param two
     * Parameter two.
     */
    public void setTwo(int two) {
        this.two = two;
    }

    /**
     * Getter for three property.
     *
     * @return
     * Parameter three.
     */
    public boolean isThree() {
        return three;
    }

    /**
     * Setter for three property.
     *
     * @param three
     * Parameter three.
     */
    public void setThree(boolean three) {
        this.three = three;
    }

    /** {@inheritDoc} */
    @Override
    public void doTag() throws JspException, IOException {
        AutotagRuntime runtime = new org.apache.tiles.autotag.jsp.test.Runtime();
        if (runtime instanceof SimpleTagSupport) {
            SimpleTagSupport tag = (SimpleTagSupport) runtime;
            tag.setJspContext(getJspContext());
            tag.setJspBody(getJspBody());
            tag.setParent(getParent());
            tag.doTag();
        }
        Request request = runtime.createRequest();        
        ModelBody modelBody = runtime.createModelBody();
        model.execute(
            one,
            two,
            three,
            request, modelBody
        );
    }
}
