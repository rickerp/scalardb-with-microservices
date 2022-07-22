# keio-acds-project
Advanced Course in Database Systems project integrating ScalarDB with microservices

# Prerequisites
- OpenJDK 8
- the JDBC instance: MySQL **AND** PostgreSQL
- Docker
- Spring Boot

If you need more details on the prerequisites, please see the [HELP.md](https://github.com/rickerp/keio-acds-project/blob/microservices/HELP.md)

# Architecture
Our project is composed of 2 microservices. 

The `order-service` is hosted on PostgreSQL and it manages all orders. The `user service` is hosted on MySQL and it manages all customers and their products. 

Here is the schema of our architecture:
![alt text](https://github.com/rickerp/keio-acds-project/blob/microservices/Architecture.png)

The communication between these 2 microservices is managed with ScalarDB and especially we use the `TwoPhaseCommitTransaction` logic. Moreover, we used Spring Boot to generate a support and a web application.  

# Execution and Command
The project must be loaded on host `localhost` and port `8081`. 

Therefor, to have access to your project please clone the repository in your local environment. Open the project with a text editor, like IntelliJ IDEA or Eclipse (Java or JEE). Then compile and build the project. Don't forget to have the port 8081 open and free. Then, open a navigator and go to the link `http://localhost:8081`.  

Our project have multiple command to POST, GET, DELETE, PUT datas in our databases. You can see the list in the following links:
- [Order request](https://github.com/rickerp/keio-acds-project/blob/microservices/order-service/src/main/java/jp/keio/acds/orderservice/api/order-requests.http) 
- [Product request](https://github.com/rickerp/keio-acds-project/blob/microservices/order-service/src/main/java/jp/keio/acds/orderservice/api/product-requests.http)
- 


