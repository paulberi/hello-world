package com.eshop.dt.client;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.eshop.bt.to.ServiceDAO;


public class JNDIClient {
	
	public static void main(String [] args) throws SQLException {
		System.out.println("looking for serviceDAO");
		ServiceDAO bean=null;
		try {
			bean=lookupHelloEJB();
		}catch(NamingException e){
			e.printStackTrace();
		}
		System.out.println("call remote ejb");
		if(bean!=null) {
			System.out.println(bean.find(1));
			
		}
	}
	
	private static ServiceDAO lookupHelloEJB() throws NamingException {
		final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
	    jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        //use HTTP upgrade, an initial upgrade requests is sent to upgrade to the remoting protocol
        jndiProperties.put(Context.PROVIDER_URL,"remote+http://localhost:8080");
        final Context context = new InitialContext(jndiProperties);

		return (ServiceDAO) context
				.lookup("eshop-BE/CustomerResources!com.eshop.bt.to.ServiceDAO");
	
	}

}
