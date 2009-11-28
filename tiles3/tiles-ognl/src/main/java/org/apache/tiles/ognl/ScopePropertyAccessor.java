package org.apache.tiles.ognl;

import java.util.Map;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.PropertyAccessor;

import org.apache.tiles.request.Request;

public class ScopePropertyAccessor implements PropertyAccessor {

	@SuppressWarnings("unchecked")
	@Override
	public Object getProperty(Map context, Object target, Object name)
			throws OgnlException {
		Request request = (Request) target;
		String scope = (String) name;
		if (scope.endsWith("Scope")) {
			String scopeName = scope.substring(0, scope.length() - 5);
			return request.getContext(scopeName);
		}
		return null;
	}

	@Override
	public String getSourceAccessor(OgnlContext context, Object target,
			Object index) {
		String scope = (String) index;
		if (scope.endsWith("Scope")) {
			String scopeName = scope.substring(0, scope.length() - 5);
			return ".getContext(\"" + scopeName + "\")";
		}
		return null;
	}

	@Override
	public String getSourceSetter(OgnlContext context, Object target,
			Object index) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setProperty(Map context, Object target, Object name,
			Object value) throws OgnlException {
		// Does nothing.
	}

}
