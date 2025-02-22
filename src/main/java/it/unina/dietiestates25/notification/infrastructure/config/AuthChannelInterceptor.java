package it.unina.dietiestates25.notification.infrastructure.config;

import it.unina.dietiestates25.auth.infrastructure.util.JwtUtil;
import it.unina.dietiestates25.exception.InvalidTokenException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import java.security.Principal;

public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                String username = null;
                try {
                    username = JwtUtil.extractUsername(token);
                } catch (InvalidTokenException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
                if (username != null) {
                    Principal principal = new StompPrincipal(username);
                    accessor.setUser(principal);
                }
            }
        }
        return message;
    }
}

