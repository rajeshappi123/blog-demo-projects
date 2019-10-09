# Spring WebSocket Example

## Description

Sample app is based on [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/) guide.

## Details

 - Uses both WebSocket and STOMP
 - `GreetingController` defines mapping for `/app/hello` to topic destination to support STOMP
 - `WebSocketConfig` config setups 3 elements: endpoint, destination prefix and broker
 - `WebSocketSecurityConfig` config protects broker and destination: authorized users only, does not expicetly defines which roles though.
 - No separate config for Spring Security - it uses the default user and pwd (generated and printed in console when app starts)
  - `AddController` allows to publich message to the destination (broker) via REST/POST request
