![messaging-demo-app](https://user-images.githubusercontent.com/15968335/143904209-6867c5b5-ce2d-4e4f-a5b5-d0e561068a67.png)

### Requirements implemented

* As a non-user, I can create my account by providing my nickname (nicknames are unique)
* As a user, I can send a message to another user identified by his nickname( you cannot send a message to yourself)
* As a user, I can view all messages that I received
* As a user, I can view all messages that I have sent
* All messages should be persisted in PostgreSQL
* Sending a message should put a message on RabbitMq

### Summary

Messaging app covers more or less happy flows.

There are three entity models - User, Message and Conversation

User - has unique nickname and id is autogenerated Message - has sender nickname, recipient nickname and conversation
id. message id is auto generated Conversation - is used to either start a new conversation or use an existing one.

It has three services handling logic for User management, Message persistence and Conversation management.

### Limitations

* User nickname db column length: 20 characters
* Message db column length: 140 characters

### Build application

mvn clean package

### Run application in docker

docker-compose build && docker-compose up -d

### Use application

* Use swagger-ui at http://localhost:8081/swagger-ui/ or postman to perform functions
* While sending a message please take not of conversationId in requestBody. To start a new conversation do not provide
  any value. To continue the existing conversation, use conversation id from your previous messages to recipient.
* Controllers of importance in Swagger UI- user-controller, message-controller, conversation-controller,
  operation-controller

### Note

* When fetching messages send by sender or received by recipient provide userId as a request header.
* The rabbitmq takes few seconds to create initial connection, hence one might see few connection errors with rabbitmq.
  It does not impact the regular functioning or startup of app.
* Messages sent are published to RabbitMQ queue "message". These are marked as status SENT in database. Once consumed by
  RabbitMq consumer, it is marked as DELIVERED in db.
* Postgres database and pgAdmin are mapped to physical volumes on disk to save data on docker restarts.

### Utility URLS

* Swagger: http://localhost:8081/swagger-ui/
* RabbitMq: http://localhost:15672/ (guest/guest)
* pgAdmin: http://localhost:5050/  (admin@admin.com/admin)

### To Improve

* Add more unit tests for minimum 85% code coverage.
* Enhance exception handling.
* Implement alternate scenarios and edge cases.
* Add Notification for recipient when message arrives.
* Enhance Websocket further to make it real time chat.
* User services and Message services can be separated into microservices.
