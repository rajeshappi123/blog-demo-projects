# spring-boot-azure-service-bus-queue
Spring Boot and Azure Service Bus (Pub Sub - Topic)

## projects

- `demo-publisher` - publisher app with REST API which publsihes account changes to a topic in Azure Service Bus
- `demo-subscriber` - subscriber app with a topic listener which consumes/gest account updates from the topic via subscription

## details
 - both publisher and subscriber apps use Microsoft Spring Boot Azure Servce Bus JMS starter with JMS API 2.0 support
 - subscriber app uses AUTO acknowledge mode (JMS)

