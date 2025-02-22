package it.unina.dietiestates25.notification.infrastructure.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

public class SubscribeChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            throw new IllegalArgumentException("Accessor is null");
        }
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination == null) {
                throw new IllegalArgumentException("Destination is null");
            }
            Principal user = accessor.getUser();
            if (user == null) {
                throw new IllegalArgumentException("User is null");
            }
            String username = user.getName();
            if (username == null) {
                throw new IllegalArgumentException("Username is null");
            }
            String expectedTopic = "/user/" + username + "/topic/notifications/";
            if (!destination.equals(expectedTopic)) {
                throw new IllegalArgumentException("Cannot subscribe to other users' topics");
            }
        }
        return message;
    }
}
