# ScalarDB in a Microservices Architecture

This branch represents the second phase of the project, where the idea was to use and implement ScalarDB 3.6.0 in a microservice architecture.\
The project was developed using SpringBoot as the backend.\
ScalarDB was implementd in two different Spring layers:
- In the persistence layer (Repository classes): Use ScalarDB Java API to access the databases
- In the buissness logic layer (Service classes): Implement transaction logic and exception handling\
For transactions that span multiple microservices, a `TwoPhaseCommitTransaction` was used, acording to the ScalarDB docs.

For phase one of the project (monolith architecture) check the `monolith` branch.

# Prerequisites
- OpenJDK 8
- Spring Boot
- PostgreSQL and MySQL

# Architecture
Our project is composed of 2 microservices.

The `order-service` is hosted on PostgreSQL and it manages all orders and associated products. The `user-service` is hosted on MySQL and it manages all customers. 

Here is the schema of our architecture:
![alt text](https://github.com/rickerp/keio-acds-project/blob/4f0d9b7cd9b00a327aa95462d4c6ab2c8b389d66/images/Architecture.png)

## API

The available REST calls can be seen in the following tables:

### For `user-service`

| URI | HTTP | Description |
| --- | ---------- | ------------ |
| /stores | POST | Create new store |
| /stores | GET | Get all stores |
| /stores/{store_id} | PUT | Update store |
| /stores/{store_id} | GET | Get specific store |
| /stores/{storeId}/checkUser | PUT | Verify if a user exists |
| /suppliers | POST | Create a new supplier |
| /suppliers | GET | Get all suppliers |
| /suppliers/{supplierId} | PUT | Update supplier |
| /suppliers/{supplierId} | GET | Get a supplier |

### For `order-service`

| URI | HTTP | Description |
| --- | ---------- | ------------ |
| /api/orders | POST | Create a new order |
| /api/orders | GET | List all orders |
| /api/orders/{order_id} | GET | Get specific order |
| /api/products | POST | Create a new product |
| /api/products | GET | List all products |
| /api/products/{product_id} | GET | Get specific product |

Notes:
- The `TwoPhaseCommitTransaction` is currently used when creating an order. Only stores can make orders to suppliers, so this condition is checked by accessing the `user-service`.
- DELETE requests were also implemented, but do not currently work as expected due to an unresolved ScalarDB mutation exception.

### For internal use (`TwoPhaseCommitTransaction`)

| URI | HTTP |
| --- | ---------- |
| /scalardb/join/{transactionId} | GET |
| /scalardb/prepare/{transactionId} | GET |
| /scalardb/commit/{transactionId} | GET |
| /scalardb/rollback/{transactionId} | GET |

# Execution

## Set up ScalarDB and Databases

For Postgres:
- Creating a user `scalar` with password `scalar`
- Create a `orderservice` database

For MySql:
- Creating a user `scalar` with password `scalar`
- Create a `userservice` database

For ScalarDB:
- Download the schema-loader for version 3.6.0
- In `/user-service` folder, run the following (replace schema-loader path):
    - `java -jar /path/to/schema-loader.jar --config src/main/resources/scalardb.properties --schema-file src/main/resources/schema.scalardb.json --coordinator`
- In `/order-service` folder, run the following (replace schema-loader path):
    - `java -jar /path/to/schema-loader.jar --config src/main/resources/scalardb.properties --schema-file order-service-schema.json --coordinator`

## Building and Running

This phase of the project was developed using IntelIJ, and is best built and run in two different instances of the IDE: one for each microservice.
If not using an IDE, the manual instructions are as follows:

For each microservice, run:
- `./gradlew build`
- `./gradlew bootRun`

## Making Requests

Each microservice is launched as a different application locally. The `order-service` uses port `8081` while the `user-service` uses port `8080`.

### For `user-service`

In the case of the `user-service`, OpenAPI was also used to generate the boilerplate code for Spring (see generator [here](https://github.com/rickerp/keio-acds-project/blob/4f0d9b7cd9b00a327aa95462d4c6ab2c8b389d66/user-service/src/main/resources/openapi.yaml)). This means that by accessing the localhost while the `user-service` is running will present an interface to make the requests:

![alt text](https://github.com/rickerp/keio-acds-project/blob/4f0d9b7cd9b00a327aa95462d4c6ab2c8b389d66/images/Interface.png)

### For `order-service`

For the `order-service`, there is no prepared interface, but requests can be made using `curl` or a prepared `.http` file [here](https://github.com/rickerp/keio-acds-project/blob/4f0d9b7cd9b00a327aa95462d4c6ab2c8b389d66/order-service/src/main/resources/requests.http)

Note that the system checks the validaty of some ids, so the ones in the .http example need to be replaced with what is return form POST and GET requests.
