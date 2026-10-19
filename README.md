# Hospital Management System (HMS) API

A robust, production-ready backend REST API for a Hospital Management System built with Spring Boot, Spring Security (JWT), and PostgreSQL. Designed with clean architecture, global exception handling, JPA auditing, and role-based access control (RBAC).
🚀 Tech Stack

    Core: Java, Spring Boot

    Security: Spring Security, JSON Web Tokens (JWT), Stateless Session Management

    Persistence & Database: Spring Data JPA, Hibernate, PostgreSQL
    
    Database Migration: Flyway

    API Documentation: Springdoc OpenAPI (Swagger UI)

    Utilities & Validation: Lombok, Jakarta Validation (@Valid)

    Auditing: JPA Auditing with custom OffsetDateTime provider

## 🏛️ Architecture & Key Design Decisions

    Shared Primary Key Strategy (@MapsId):
    Role-specific tables (Doctor, Admin, Receptionist) share a 1-to-1 primary key relationship with the core Staff entity using @MapsId, ensuring tight database integrity without redundant ID generation.

    Stateless Security Filter Chain:
    Custom JWT authentication filter intercepts requests prior to the Spring Security authorization layer, validating bearer tokens and extracting authorities securely.

    Global Exception Handling:
    Centralized @RestControllerAdvice intercepts domain exceptions (ResourceNotFoundException, UserAlreadyExistsException, database constraint violations) and maps them to clean, uniform JSON error responses with correct HTTP status codes (404 Not Found, 409 Conflict, etc.).

    JPA Auditing:
    Automated tracking of created_at and updated_at fields using PostgreSQL timestamptz columns and a custom DateTimeProvider bean configured for OffsetDateTime.

## ⚙️ Core Features

    Staff & Role Management: Secure registration and authentication for Admins, Doctors, and Receptionists.

    Patient Management: CRUD operations and search capabilities restricted to authorized hospital personnel.

    Appointment Scheduling: Book and manage appointments with strict authorization rules (@PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")).

    Soft Deletes & Error Safety: Built-in safeguards preventing operations on deactivated or deleted records.

## 🔒 Security & Roles

The API implements role-based access control across endpoints using Spring Security method-level and filter-level matchers:

    ADMIN: Full administrative control over staff, roles, and system configuration.

    RECEPTIONIST: Patient registration, appointment booking, and lookup management.

    DOCTOR: Access to patient records and clinical schedules.

## 🛠️ Getting Started
Prerequisites

    Java 17 or higher

    PostgreSQL installed and running locally

    Maven or Gradle

1. Clone the Repository
Bash

git clone https://github.com/your-username/hospital-management-system.git
cd hospital-management-system

2. Configure Database & Environment

Update your src/main/resources/application.properties with your PostgreSQL credentials:
Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Secret & Expiration
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

3. Run the Application
Bash

mvn spring-boot:run

📖 API Documentation (Swagger UI)

This project uses Springdoc OpenAPI for automated, interactive API documentation.

Once the application is running, you can explore, test, and authenticate against all endpoints directly through your browser using Swagger UI:

    Swagger UI URL: http://localhost:8080/swagger-ui/index.html

    OpenAPI JSON Docs: http://localhost:8080/v3/api-docs

(Note: For secured endpoints, use the /api/v1/auth/login route to obtain your JWT token, then click the Authorize button at the top right of the Swagger UI and enter Bearer <your_token> to test protected requests).
