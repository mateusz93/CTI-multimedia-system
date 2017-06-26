package pl.lodz.p.cti.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import javax.validation.constraints.NotNull;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @NotNull
    @Value("${web-socket.destinationPrefixes}")
    private String destinationPrefixes;

    @NotNull
    @Value("${web-socket.prefixes}")
    private String prefixes;

    @NotNull
    @Value("${web-socket.paths}")
    private String paths;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(destinationPrefixes);
        config.setApplicationDestinationPrefixes(prefixes);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(paths).withSockJS();
    }

}
