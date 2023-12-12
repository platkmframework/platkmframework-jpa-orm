package org.platkmframework.jpa.orm.proxy;

import java.lang.reflect.Method;

import org.apache.commons.lang3.NotImplementedException;
import org.platkmframework.util.proxy.ProxyProcesorException;
import org.platkmframework.util.proxy.ProxyProcessor;

public class NativeQueryProxyProcessor implements ProxyProcessor {

	@Override
	public Object run(Object proxy, Class<?> classInterface, Method method, Object[] args)
			throws ProxyProcesorException {
		throw new NotImplementedException("NativeQueryProxyProcessor");
	}

	 
}
