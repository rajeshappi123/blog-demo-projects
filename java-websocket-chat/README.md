# Java WebSocket Example

## Description
`api` and `client` are two separate services which run on a different domain/ports and provide a simple demo of a chat app. There is no security portion (atthentication or authorization);


## Details

- `api` project is based on Java WebSocket API (JSR 356) and {Glassfish Tyrus}(https://tyrus-project.github.io/) implementation
- `ChatEndpointConfigurator` enables cross-origin by allowing request from a different domain
- `MyChatEndpoint` defines  WebSocket endpoint for chat and si responsible for broadcasting the messages to all connected clients
- `client`app is a Spring Boot based service with browser JS client. It uses standard [JS WebSocket API](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket) to communicate with 'api'. Bootstrap is used just for design.




