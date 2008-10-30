package org.apache.tiles.freemarker.context;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.freemarker.FreeMarkerTilesException;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;

import freemarker.core.Environment;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.template.TemplateModelException;

public class FreeMarkerTilesRequestContext implements TilesRequestContext {

    private TilesApplicationContext applicationContext;
    
    private Environment env;
    
    private TilesRequestContext enclosedRequest;
    
    public FreeMarkerTilesRequestContext(
            TilesApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
        try {
            HttpRequestHashModel request = (HttpRequestHashModel) env
                    .getDataModel().get("Request");
            enclosedRequest = new ServletTilesRequestContext(applicationContext,
                    request.getRequest(), request.getResponse());
        } catch (TemplateModelException e) {
            throw new FreeMarkerTilesException(
                    "Cannot obtain a hash from FreeMarker", e);
        }
    }
    
	public void dispatch(String path) throws IOException {
	    include(path);
	}

	public TilesApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public Map<String, String> getHeader() {
		return enclosedRequest.getHeader();
	}

	public Map<String, String[]> getHeaderValues() {
		return enclosedRequest.getHeaderValues();
	}

	public Map<String, String> getParam() {
		return enclosedRequest.getParam();
	}

	public Map<String, String[]> getParamValues() {
		return enclosedRequest.getParamValues();
	}

	public Object getRequest() {
		return env;
	}

	public Locale getRequestLocale() {
		return env.getLocale();
	}

	public Map<String, Object> getRequestScope() {
		return enclosedRequest.getRequestScope();
	}

	public Object getResponse() {
		return env;
	}

	public Map<String, Object> getSessionScope() {
		return enclosedRequest.getSessionScope();
	}

	public void include(String path) throws IOException {
	    enclosedRequest.include(path);
	}

	public boolean isUserInRole(String role) {
		return enclosedRequest.isUserInRole(role);
	}
}
