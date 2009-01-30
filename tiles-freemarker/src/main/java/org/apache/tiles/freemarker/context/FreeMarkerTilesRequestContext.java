package org.apache.tiles.freemarker.context;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextWrapper;

import freemarker.core.Environment;

public class FreeMarkerTilesRequestContext extends TilesRequestContextWrapper implements TilesRequestContext {
    
    private Environment env;
    
    private transient Object[] requestObjects;
    
    public FreeMarkerTilesRequestContext(
            TilesRequestContext enclosedRequest, Environment env) {
        super(enclosedRequest);
        this.env = env;
    }
    
	public void dispatch(String path) throws IOException {
	    include(path);
	}

	public Object getRequest() {
		return env;
	}

	public Locale getRequestLocale() {
		return env.getLocale();
	}

	public Object getResponse() {
		return env;
	}

    @Override
    public PrintWriter getPrintWriter() throws IOException {
        Writer writer = env.getOut();
        if (writer instanceof PrintWriter) {
            return (PrintWriter) writer;
        } else {
            return new PrintWriter(writer);
        }
    }

    @Override
    public Writer getWriter() throws IOException {
        return env.getOut();
    }

    @Override
    public Object[] getRequestObjects() {
        if (requestObjects == null) {
            requestObjects = new Object[1];
            requestObjects[0] = env;
        }
        return requestObjects;
    }
}
