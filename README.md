# keio-acds-project
Advanced Course in Database Systems project integrating ScalarDB with microservices

# Prerequisites
- OpenJDK 8
- the JDBC instance: MySQL **AND** PostgreSQL
- Docker

# First start 
Clone the repot in your local environment. As example, you can use the command `git clone` with the SSH key. 
Then you connect to MySQL and PastgreSQL. 

For MySQL:
  You create the user `myusr` with the password `Password1!`, and you can grant him the rights on the already existing databases. 
```
  CREATE USER 'myusr'@'localhost' IDENTIFIED BY 'Password1!';
  GRANT ALL PRIVILEGES ON *.* TO 'myusr'@'localhost';
  FLUSH PRIVILEGES;
```
  Next, you log in with your new account and you create the database `customer` with it.
```
  mysql -u myusr -p
  CREATE DATABASE customer;
```
For PostgreSQL:
  You realized the same operation that mention above. You create the user `myusr` with the password `Password1!`. You need to grant the login right and create database privileges.
  ```
    CREATE USER myusr WITH ENCRYPTED PASSWORD 'Password1' CREATEDB LOGIN;
  ```
  Then you create the database `orders` with `myuser` as owner. Then, you assign all priviledges to `myusr` on this database.
  ```
  CREATE DATABASE orders OWNER myusr;
  GRANT all privileges ON DATABASE orders TO myusr;
  ```
 It's importante to notify that you don't need to create the tables in each database the file `schema.json` and `docker-compose.yml` will do it for you. 
 
 After all databases and users created you have to load the schema.
 
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
 
 The command to run the project is `./gradlew run --args="NameOfTheCommand"`. `NameOfTheCommand`, is one of the following command that can be use to realized some operation and transaction with the database. Until now we have only the following command:
 - `LoadInitialData` used to add some data to the database at the begining of the project.
 - `GetOrdersInfo` used to display the first 100 datas of each table in the database orders.
 - `GetCustomersInfo` used to display the first 100 datas of each table in the database customer.
 
 Other command are in preparation and the `README.md` will be updated with each addition. 

