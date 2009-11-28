package org.apache.tiles.ognl;

import java.util.Map;

import org.apache.tiles.request.Request;

import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.PropertyAccessor;

public class AnyScopePropertyAccessor implements PropertyAccessor {

	@SuppressWarnings("unchecked")
	@Override
	public Object getProperty(Map context, Object target, Object name)
			throws OgnlException {
		Request request = (Request) target;
		String attributeName = (String) name;
		for (String scopeName: request.getAvailableScopes()) {
			Map<String, Object> scope = request.getContext(scopeName);
			if (scope.containsKey(attributeName)) {
				return scope.get(attributeName);
			}
		}
		return null;
	}

	@Override
	public String getSourceAccessor(OgnlContext context, Object target,
			Object index) {
		Request request = (Request) target;
		String attributeName = (String) index;
		for (String scopeName: request.getAvailableScopes()) {
			Map<String, Object> scope = request.getContext(scopeName);
			if (scope.containsKey(attributeName)) {
				return ".getContext(\"" + scopeName + "\").get(index)";
			}
		}
		return null;
	}

	@Override
	public String getSourceSetter(OgnlContext context, Object target,
			Object index) {
		Request request = (Request) target;
		String attributeName = (String) index;
		String[] availableScopes = request.getAvailableScopes();
		for (String scopeName: availableScopes) {
			Map<String, Object> scope = request.getContext(scopeName);
			if (scope.containsKey(attributeName)) {
				return ".getContext(\"" + scopeName + "\").put(index, target)";
			}
		}
		return ".getContext(\"" + availableScopes[0] + "\").put(index, target)";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setProperty(Map context, Object target, Object name,
			Object value) throws OgnlException {
		Request request = (Request) target;
		String attributeName = (String) name;
		String[] availableScopes = request.getAvailableScopes();
		for (String scopeName: availableScopes) {
			Map<String, Object> scope = request.getContext(scopeName);
			if (scope.containsKey(attributeName)) {
				scope.put(attributeName, value);
				return;
			}
		}
		if (availableScopes.length > 0) {
			request.getContext(availableScopes[0]).put(attributeName, value);
		}
	}

}
