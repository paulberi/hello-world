package com.yajava.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class EchoClient {

	public static void main(String[] args) {
		
		System.out.println("Looking up EJBEcho");
		
		Hello bean = null;
		try {
			bean = lookupHelloEJB();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		System.out.println("Call remote function on ...");
		if(bean != null) {
			System.out.println(bean.echo("Hello YH Java 19!"));			
			System.out.println(bean.echoReverse("Hello YH Java 19!"));			
		}
	}
	
	private static Hello lookupHelloEJB() throws NamingException {
		final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
	    jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        //use HTTP upgrade, an initial upgrade requests is sent to upgrade to the remoting protocol
        jndiProperties.put(Context.PROVIDER_URL,"remote+http://localhost:8080");
        final Context context = new InitialContext(jndiProperties);

		return (Hello) context
				.lookup("EJBEcho/HelloBean!com.yajava.ejb.Hello");
	}
}
