package com.github.mogikanen9.chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageEncoder implements Encoder.Text<Message> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(EndpointConfig config) {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(Message object) throws EncodeException {
        try {
            log.debug(String.format("encode->%s",object));
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EncodeException(object, e.getMessage(), e);
        }
    }

}