# spring-boot-azure-service-bus-queue
Spring Boot and Azure Service Bus (PTP - Queue)

## projects

- `profile-sender` - sender app with REST API which sends profile and account details to 2 separate queues in Azure Service Bus
- `profile-receiver` - receiver app with 2 listeners which consumes/gest profile and account details from 2 queues

## profile details
 - both sender and receiver apps use Azure Service Bus Java SDK to send and receive messages via Service Bus Queue


## account detals
 - both sender and receiver apps Microsoft Spring Boot Azure Servce Bus JMS starter with JMS API 2.0 support
 - receiver app uses CLIENT acknowledge mode (JMS)
 - receiver app stores the valid accounts in RDBMS (HSQLDB)
 - receiver app relies on JMS Transaction Manager to provide transactional support (No ZA transaction is used/configured though)

## Doc and tutorials used
- [Bootiful Azure by Josh Long](https://github.com/joshlong/bootiful-azure-article/tree/master/bootiful-azure)
- [Azure Service Bus JMS Queue Sample](https://github.com/microsoft/azure-spring-boot/tree/master/azure-spring-boot-samples/azure-servicebus-jms-queue-spring-boot-sample)


