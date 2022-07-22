# ScalarDB in a Monolith Architecture

This branch represents the first phase of the project, where the idea was to use and implement ScalarDB with two JDBC databases based on the tutorials available.

# Prerequisites
- OpenJDK 8
- the JDBC instances: MySQL **AND** PostgreSQL
- Docker & Docker Compose

# First connection 
Clone the repository in your local environment. As example, you can use the command `git clone` with the SSH key. 
Then you connect to MySQL and PostgreSQL. 

*For MySQL*: 

  You create the user `myusr` with the password `Password1!`, and you can grant him the rights on the already existing databases. 
```
  CREATE USER 'myusr'@'localhost' IDENTIFIED BY 'Password1!';
  GRANT ALL PRIVILEGES ON *.* TO 'myusr'@'localhost';
  FLUSH PRIVILEGES;
```
  Next, you login with your new account and you create the database `customer` with it.
```
  mysql -u myusr -p
  CREATE DATABASE customer;
```
*For PostgreSQL*:

  You realize the same operation that mention above. You create the user `myusr` with the password `Password1!`. You need to grant the `LOGIN` right and `CREATEDB` privileges.
  ```
    CREATE USER myusr WITH ENCRYPTED PASSWORD 'Password1' CREATEDB LOGIN;
  ```
  Then you create the database `orders` with `myusr` as owner. Then, you assign all priviledges to `myusr` on this database.
  ```
  CREATE DATABASE orders OWNER myusr;
  GRANT all privileges ON DATABASE orders TO myusr;
  ```
 It's importante to notify that you don't need to create the tables in each database the file `schema.json` and `docker-compose.yml` will do it for you. 
 
 After all databases and users created you need to load the schema.
 
 First, you stop mysql and postresql that will free the 2 ports, respectively, '3306' and '5432' used by mysql and postgresql. 
 ```
 sudo service postgresql stop
 sudo service mysql stop
 ```
 Second, you run the `docker-compose.ylm` file that load the schema. And you restart postgresql and mysql.
 
 ```
 docker-compose up -d
 sudo service postgresql start
 sudo service mysql start
 java -jar scalardb-schema-loader-3.5.2.jar --config scalardb.properties --schema-file schema.json --coordinator
 ```
 Now, you can load the initial data to the databases with the command: 
 ```
 ./gradlew run --args="LoadInitialData"
 ```
 
 # Execut and run the project
 
 The command to run the project is `./gradlew run --args="NameOfTheCommand"`. `NameOfTheCommand`, is one of the following command that can be used to realized some operation and transaction with the database. Until now we have only the following command:
 - `LoadInitialData` used to add specific data to the databases at the begining of the project.
 - `GetOrdersInfo` used to display the first 100 rows of each tables in the database `orders`.
 - `GetCustomersInfo` used to display the first 100 rows of each table in the database `customer`.
 - `PlaceOrder <toId> <itemId>:<count>:<fromId>` used to create an transaction between the customer `fromId` and the customer `toId`. The item send is the `itemId` with the amount `count`. 
 - `ReStock <customerId> <itemId>:<count>:<price>` used to modify the amount of item `itemId` for the customer `customerId`. Generally this command is used to simulate a restocking.  
 - `NewItem <name>` used to add a item in the order.item table. the `name` need to be unique.
 - `UpdateCustomer <customerId> <treasury>:<type>` used to create a new customer or modify the information of an existing customer. If `type==1` the customer is a supermarcket. If `type==2` the customer is a supplier. The default value is `1`.  
 
 Other commands are in preparation and the `README.md` will be updated with each addition. 

