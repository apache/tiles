package org.apache.tiles.autotag.core.runtime;

import java.io.IOException;
import java.io.Writer;

public interface ModelBody {

    String evaluateAsString() throws IOException;

    void evaluateWithoutWriting() throws IOException;

    void evaluate() throws IOException;

    void evaluate(Writer writer) throws IOException;
}
