package org.arquitectura.reelclipsv2.chat.internal.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefijo para mensajes que van al broker (el cliente se suscribe aquí)
        registry.enableSimpleBroker("/topic", "/queue");

        // Prefijo para mensajes que van a un @MessageMapping
        registry.setApplicationDestinationPrefixes("/app");

        // Prefijo para mensajes privados (uno a uno)
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Con SockJS (para el frontend)
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(usuarioIdHandshakeHandler())
                .withSockJS();

        // Sin SockJS (para pruebas con Hoppscotch, Postman, wscat)
        registry.addEndpoint("/ws-chat-native")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(usuarioIdHandshakeHandler());
    }

    private DefaultHandshakeHandler usuarioIdHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler,
                                              @NonNull Map<String, Object> attributes) {
                String usuarioId = obtenerParametro(request);
                if (usuarioId == null || usuarioId.isBlank()) {
                    return super.determineUser(request, wsHandler, attributes);
                }
                return () -> usuarioId;
            }
        };
    }

    private String obtenerParametro(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query == null || query.isBlank()) {
            return null;
        }

        for (String parametro : query.split("&")) {
            String[] partes = parametro.split("=", 2);
            if (partes.length == 2 && partes[0].equals("usuarioId")) {
                return partes[1];
            }
        }
        return null;
    }
}
