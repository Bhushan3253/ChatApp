package com.bhushan.chat.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.bhushan.chat.model.ChatMessage;
import com.bhushan.chat.model.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
          private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
     private final SimpMessageSendingOperations  messageTemplate;
       
     @EventListener
     public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
          StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
          String username=(String)headerAccessor.getSessionAttributes().get("username");
          if(username!=null){
            log.info("User disconnected:{}",username);
            var chatMessage=ChatMessage.builder().type(MessageType.LEAVE).sender(username).build();
               messageTemplate.convertAndSend("/topic/public", chatMessage);
          }
     }

}
