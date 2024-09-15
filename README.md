

```md
# Raven - Customer Management API

## Project Description

**Raven** is a microservice-based application for customer management, built using **Spring Boot**. It provides CRUD (Create, Read, Update, Delete) functionality for managing customers via a REST API, supports database migrations using **Liquibase**, and implements soft deletion of records.

The application is containerized using **Docker** and **Docker Compose** for easy deployment and testing.

## Key Technologies

- **Java 17**
- **Spring Boot 3.3.3**
- **Maven 3.8.3**
- **Liquibase** for database migrations
- **Hibernate (JPA)** for database interaction
- **MySQL** as the primary database
- **MapStruct** for DTO mapping
- **Lombok** to reduce boilerplate code
- **Docker and Docker Compose** for containerizing the application and database

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/username/raven.git
cd raven
```

### 2. Configure the Database

Make sure you have Docker installed for containerizing the database. This project uses MySQL.

Configure your environment variables for the database in the `.env` file (create it at the root of the project):

```
MYSQL_ROOT_USER=root
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=ravenDb
```

### 3. Build and Run the Application

The project can be built and run using **Docker Compose**:

```bash
docker-compose up --build
```

This will launch two containers:
- A container for MySQL.
- A container for the **Raven** application.

The application will be accessible at: `http://localhost:8080/api/customers`.

### 4. Database Migrations

The application uses **Liquibase** for managing database migrations. Migrations are automatically applied when the application starts.

### 5. API Testing

You can test the API using **Postman** or **cURL**. Below are examples of how to use the API.

## API Endpoints

### 1. Create a Customer

**POST** `/api/customers`

```json
{
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890"
}
```

Response:

```json
{
  "id": 1,
  "created": 1694788931,
  "updated": 1694788931,
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "isActive": true
}
```

### 2. Get All Active Customers

**GET** `/api/customers`

Response:

```json
[
  {
    "id": 1,
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "isActive": true
  }
]
```

### 3. Get Customer by ID

**GET** `/api/customers/{id}`

Response:

```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1234567890",
  "isActive": true
}
```

### 4. Update a Customer

**PUT** `/api/customers/{id}`

```json
{
  "fullName": "John Doe Updated",
  "phone": "+9876543210"
}
```

Response:

```json
{
  "id": 1,
  "fullName": "John Doe Updated",
  "email": "john.doe@example.com",
  "phone": "+9876543210",
  "isActive": true
}
```

### 5. Soft Delete a Customer

**DELETE** `/api/customers/{id}`

This will mark the customer as inactive without removing them from the database.

## Dockerfile

To build and package the application, **Docker multi-stage build** is used. Here is the content of the `Dockerfile`:

```dockerfile
# Stage 1: Build Stage
FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Final Stage
FROM openjdk:17-jdk
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/raven-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

## Docker Compose File

Here is the `docker-compose.yml` file, which is used to start both the application and the database:

```yaml
version: '3.8'

services:
  myapp:
    build: .
    ports:
      - "8080:8080"
      - "8983:8983"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/ravenDb?serverTimeZone=UTC
      - SPRING_DATASOURCE_USERNAME=${MYSQL_ROOT_USER}
      - SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
      - SPRING_JPA_HIBERNATE_DDL_AUTO=validate
      - SPRING_JPA_SHOW_SQL=true
      - SPRING_JPA_OPEN_IN_VIEW=false
      - SPRING_JACKSON_DESERIALIZATION_FAIL_ON_UNKNOWN_PROPERTIES=true
      - SERVER_SERVLET_CONTEXT_PATH=/api
    depends_on:
      - db

  db:
    image: mysql:8.0
    container_name: raven-container
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_ROOT_USER: ${MYSQL_ROOT_USER}
      MYSQL_DATABASE: ${MYSQL_DATABASE}
    ports:
      - "3307:3306"
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```

## Conclusion

Now your application is ready to run! Use Docker Compose to start the MySQL database and the application. You can test it using the provided API examples with Postman or cURL.

Don't forget to configure the environment variables in the `.env` file to ensure proper database connection.
 

