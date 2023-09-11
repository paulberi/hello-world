package se.metria.matdatabas.service.scheduler.config;

import org.springframework.web.context.request.RequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Used by threads instantiated outside web request to set the required attributes
 * for request scope (i.e scheduled jobs).
 * For more information see:
 * https://medium.com/@pranav_maniar/spring-accessing-request-scope-beans-outside-of-web-request-faad27b5ed57
 */

public class CustomRequestScopeAttr implements RequestAttributes {

	private Map<String, Object> requestAttributeMap = new HashMap<>();

	@Override
	public Object getAttribute(String name, int scope) {
		if(scope== RequestAttributes.SCOPE_REQUEST) {
			return this.requestAttributeMap.get(name);
		}
		return null;
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		if(scope== RequestAttributes.SCOPE_REQUEST){
			this.requestAttributeMap.put(name, value); }
	}
	@Override
	public void removeAttribute(String name, int scope) {
		if(scope== RequestAttributes.SCOPE_REQUEST) {
			this.requestAttributeMap.remove(name); }
	}

	@Override
	public String[] getAttributeNames(int scope) {
		if (scope == RequestAttributes.SCOPE_REQUEST) {
			return this.requestAttributeMap.keySet().toArray(new String[0]);
		}
		return new String[0];
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback, int scope) {
	}

	@Override
	public Object resolveReference(String key) {
		return null;
	}

	@Override
	public String getSessionId() {
		return null;
	}

	@Override
	public Object getSessionMutex() {
		return null;
	}
}
