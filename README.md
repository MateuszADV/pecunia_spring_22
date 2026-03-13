![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3-green)
![Database](https://img.shields.io/badge/PostgreSQL-blue)

# Pecunia – Collection Management System

Backend application for managing a private collection of coins, medals, banknotes and securities.

The project focuses on data management, financial analysis and integration with external financial APIs.  
It allows storing collection items, generating statistics, reports and tracking financial values such as exchange rates and precious metal prices.

The application is built using **Spring Boot** and follows a layered backend architecture.

---

## Features

• management of coins, medals, banknotes and securities  
• pageable queries for large collections  
• role-based access (ADMIN / USER)  
• visibility filtering for user access  
• statistics by country and currency  
• financial reports and charts  
• integration with external financial APIs  
• exchange rate integration (NBP API)  
• precious metal price tracking  
• collection analytics

---

## Tech Stack

Backend

Java 21  
Spring Boot  
Spring Data JPA  
Hibernate

Database

PostgreSQL

Testing

JUnit  
Integration Tests

Build Tool

Maven

External APIs

NBP API – exchange rates  
Metal price API – precious metal prices

---

## Architecture

The project follows a classic layered Spring Boot architecture:
Client

↓   
Controller  
↓   
Service     
↓   
Repository  
↓   
Database


Project structure:  
src/main/java   
│   
├── controller  
├── service     
├── repository  
├── model   
├── dto     
├── config  


Responsibilities:

Controller  
Handles REST API endpoints.

Service  
Contains business logic and security rules.

Repository  
Handles database communication using Spring Data JPA.

Model  
Database entities.

DTO  
Data transfer objects for custom queries and reports.

---

## Security

The application uses **role-based access control**.

Roles:

ADMIN  
• full access to the system  
• can edit and manage all records

USER  
• read-only access  
• only visible items can be accessed

Visibility is enforced in the service layer to prevent unauthorized data access.

---

## Example API Endpoints
GET /api/medals     
GET /api/medals/{id}    
POST /api/medals    
PUT /api/medals/{id}    
DELETE /api/medals/{id}


Pagination example:

GET /api/medals?page=0&size=5


Statistics example:

GET /api/statistics/country     
GET /api/statistics/currency


---

## External API Integration

The system integrates with external financial data providers.

NBP API  
Provides exchange rate data used in financial calculations.

Metal Price API  
Provides precious metal prices for investment tracking.

---

## Example Use Cases

• managing a private coin or medal collection  
• analyzing collection statistics by country or currency  
• tracking value changes based on exchange rates  
• monitoring precious metal investment values

---

## Future Improvements

• Docker support  
• JWT authentication  
• caching for external API data  
• extended reporting and analytics  
• frontend application (React / Angular)

---

## Running the Project

Clone repository:
git clone https://github.com/MateuszADV/pecunia_spring_22


Run application:
mvn spring-boot:run


---

## Author

Mateusz  
Java / Spring Boot Backend Developer
