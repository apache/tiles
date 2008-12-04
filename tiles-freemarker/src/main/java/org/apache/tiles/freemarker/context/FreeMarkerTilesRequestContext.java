package org.apache.tiles.freemarker.context;

import java.io.IOException;
import java.util.Locale;

import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.context.TilesRequestContextWrapper;

import freemarker.core.Environment;

public class FreeMarkerTilesRequestContext extends TilesRequestContextWrapper implements TilesRequestContext {
    
    private Environment env;
    
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
}
