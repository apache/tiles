package org.apache.tiles.autotag.core.runtime;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.tiles.autotag.core.runtime.util.NullWriter;

public abstract class AbstractModelBody implements ModelBody {

    private Writer defaultWriter;

    public AbstractModelBody(Writer defaultWriter) {
        this.defaultWriter = defaultWriter;
    }

    @Override
    public void evaluate() throws IOException {
        evaluate(defaultWriter);
    }

    @Override
    public String evaluateAsString() throws IOException {
        StringWriter writer = new StringWriter();
        try {
            evaluate(writer);
        } finally {
            writer.close();
        }
        String body = writer.toString();
        if (body != null) {
            body = body.replaceAll("^\\s*|\\s*$", "");
            if (body.length() <= 0) {
                body = null;
            }
        }
        return body;
    }

    @Override
    public void evaluateWithoutWriting() throws IOException {
        NullWriter writer = new NullWriter();
        try {
            evaluate(writer);
        } finally {
            writer.close();
        }
    }

}
