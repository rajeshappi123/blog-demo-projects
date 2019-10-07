package com.github.mogikanen9.chat;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder implements Decoder.Text<Message> {

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
    public Message decode(String s) throws DecodeException {
        try {
            log.debug(String.format("decode->%s",s));
            return objectMapper.readValue(s, Message.class);
        } catch (IOException e) {
            throw new DecodeException(s, e.getMessage(), e);
        }
    }

    @Override
    public boolean willDecode(String s) {        
         return (s != null);
    }

}