# keio-acds-project
Advanced Course in Database Systems project integrating ScalarDB with microservices

# Prerequisites
- OpenJDK 8
- the JDBC instance: MySQL **AND** PostgreSQL
- Docker
- Spring Boot

# Architecture
Our project is composed of 2 microservices. 

The `order-service` is hosted in PostgerSQL and it manages all orders. The `user-service` is hosted in MySQL and it manages the customers and their products. 

Here is the schema of your architecture:
![alt text](https://github.com/rickerp/keio-acds-project/blob/microservices/Architecture.png)

The communication between these 2 microservices is managed with ScalarDB and especially the `TwoWayTransaction???`. Moreover, we used Spring Boot to generate a web support and application.  

# Execution and Command
The project mist be load in the `localhost` host and the port `8180`.  
