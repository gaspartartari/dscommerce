# DsCommerce
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/gaspartartari/dscommerce/blob/main/LICENSE)

# About the Project

**Name:** Ds Commerce

**Purpose:** Ds Commerce, an educational back-end web application, employs a layered architecture to proficiently manage users, products, categories, orders, and payments. It features robust security through OAuth2 with JWT token implementation and utilizes JPA/Hibernate for efficient object-relational mapping.

**Key Features:**
1. **Layered Architecture:** Structured for maintainability and scalability, the project follows a layered architecture.

2. **Entity Management:** Handles users, products, categories, orders, and payments seamlessly.

3. **RESTful APIs:** Provides standardized and accessible API endpoints for interacting with entities.

4. **Security Implementation:** Utilizes OAuth2 with JWT tokens to ensure secure authentication and authorization.

5. **JPA/Hibernate:** Efficiently manages object-relational mapping, enhancing database interactions.

6. **Java Spring Boot/Maven:** Developed using Java Spring Boot framework and Maven for streamlined project management.

7. **Database Environment:**
   - **Test:** H2 database for testing purposes.
   - **Development/Production:** PostgreSQL ensures robust data storage for both development and production environments.

**Objective:** Ds Commerce serves as a comprehensive educational tool, exemplifying key concepts such as layered architecture, RESTful API design, security through OAuth2 and JWT, and efficient database management using JPA/Hibernate. Developed with Java Spring Boot and Maven, the project ensures a smooth and organized development process, with H2 and PostgreSQL databases catering to testing, development, and production needs.

## Conceptual model:
![Conceptual model](https://github.com/gaspartartari/assets/blob/main/Captura%20de%20Tela%202024-01-12%20a%CC%80s%2019.14.36.png)

## Layared architecture
![Layered architecture](https://github.com/gaspartartari/assets/blob/main/1635741828065.jpeg)

# Technologies used
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Maven
- OAuth2 with JWT 
- RESTful APIs
- H2 Database (Test Environment)
- PostgreSQL (Development and Production Environments)

## Deployment
- Back end: Heroku
- Database: Postgresql

# How to run the project

## Back end
Pre-requisites: Java 11

```bash
# clone repository
git clone https://github.com/gaspartartari/dscommerce.git

# Enter the dscommerce project folder
cd dscommerce

# run the project
./mvnw spring-boot:run
```
## Postman Environment and Collections

Explore and interact with the Ds Commerce RESTful APIs using Postman. The following resources are provided to assist you:

- **Postman Environment:** [Ds Commerce Postman Environment](https://drive.google.com/file/d/1OklFCrF099JVJ3WP1N32X6KJHAM7YiI6/view?usp=sharing)

- **Postman Collection:** [Ds Commerce API Collection](https://drive.google.com/file/d/1rongL8pgxorUPX9JP2jT9meCjBQsifzX/view?usp=sharing)


### Getting Started
1. Import the Postman environment and collections.
2. Set up the environment variables for easy configuration.
3. Start making requests to the various API endpoints.
   

**Note:** Ensure that you have the necessary authentication tokens when testing protected endpoints. The environment is set with the username maria@gmail.com, but you can also test with the alex@gmail.com for different endpoints access level.

Feel free to experiment with different usernames to observe the varying levels of access and functionality across the API.


Happy exploring!

# Autor

Gaspar Tartari

https://www.linkedin.com/in/gaspartartari
