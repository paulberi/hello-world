package se.metria.xplore.castor.integration;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tempuri.IProsonaRealestateExchange;

@Configuration
public class ClientConfig {

	@Value("${client.address}")
	private String address;

	@Bean(name = "serviceProxy")
	public IProsonaRealestateExchange serviceProxy() {
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.setAddress(address);

		return factoryBean.create(IProsonaRealestateExchange.class);
	}
}
