package se.metria.matdatabas.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientCodecCustomizer;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import se.metria.matdatabas.common.ProxyProperties;

import java.util.List;

@Configuration
@ConditionalOnClass({WebClient.class})
@AutoConfigureAfter({CodecsAutoConfiguration.class, ClientHttpConnectorAutoConfiguration.class})
public class WebClientAutoConfiguration {
    private final WebClient.Builder webClientBuilder = WebClient.builder();

    public WebClientAutoConfiguration(ObjectProvider<WebClientCustomizer> customizerProvider) {
        customizerProvider.orderedStream().forEach((customizer) -> {
            customizer.customize(this.webClientBuilder);
        });
    }

    @Bean
    @Primary
    @Scope("prototype")
    public WebClient.Builder webClientBuilder() {
        return this.webClientBuilder.clone();
    }

    @Bean
    @Qualifier("proxyWebclientBuilder")
    @Scope("prototype")
    public WebClient.Builder proxyWebclientBuilder(WebClient.Builder webClientBuilder,
                                                   ProxyProperties proxyProperties) {
        if (StringUtils.isNotEmpty(proxyProperties.getHost())) {
            HttpClient httpClient = HttpClient.create()
                    .proxy(proxy -> proxy
                            .type(ProxyProvider.Proxy.HTTP)
                            .host(proxyProperties.getHost())
                            .port(proxyProperties.getPort()));

            ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

            return  this.webClientBuilder.clone().clientConnector(connector);
        } else {
            return this.webClientBuilder.clone();
        }

    }


    @Configuration
    @ConditionalOnBean({CodecCustomizer.class})
    protected static class WebClientCodecsConfiguration {
        protected WebClientCodecsConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean
        @Order(0)
        public WebClientCodecCustomizer exchangeStrategiesCustomizer(List<CodecCustomizer> codecCustomizers) {
            return new WebClientCodecCustomizer(codecCustomizers);
        }
    }
}
