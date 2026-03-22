# Portfolio API

A robust, enterprise-ready Spring Boot backend for managing professional portfolio data. This API serves as the centralized data layer for portfolio websites, providing structured endpoints to manage a user's professional identity, experiences, skills, projects, and education.

## Overview

The `portfolio-api` is designed with a clear domain-driven architecture, implementing various entities to thoroughly capture a professional's resume and portfolio details. 

It also includes built-in User Management and Authentication features to securely manage the data.

## Working Models

The API is built around several core entities mapping directly to the relational database structure (JPA/Hibernate):

### 1. **Profile (`Profile`)**
The central entity for the portfolio owner's general information. 
- **Attributes**: `name`, `title`, `about`, `email`, `phone`, `location`.

### 2. **Projects (`Project`)**
Manages the showcases of professional or personal projects.
- **Attributes**: `name`, `description` (up to 1000 chars), `techStack`.

### 3. **Education (`Education`)**
Tracks academic background and achievements.
- **Attributes**: `degree`, `institution`, `year`.

### 4. **Experience (`Experience`)**
Chronicles work history and professional roles. *(Includes company, role, duration, etc.)*

### 5. **Skills (`Skill`)**
Represents technical or soft skills acquired by the professional.

### 6. **API User (`ApiUser`)**
Handles the authentication and authorization layer, ensuring that portfolio details are securely managed by authorized administrators.
- **Attributes**: `username`, `password` (encrypted), `role`.

## Professional Architecture Features

- **Spring Boot Ecosystem**: Utilizes Spring Web, Spring Data JPA, and Spring Boot Starters for rapid, reliable REST API development.
- **Data Persistence**: Jakarta Persistence API (JPA) for ORM mapping.
- **Lombok Integration**: Reduces boilerplate code by automatically generating Getters, Setters, and Constructors.
- **Security & Encryption**: Custom `EncryptionUtils` to securely hash and verify `ApiUser` passwords (resolving Null-Safety and generic type constraints seamlessly).
- **Service Layer Abstraction**: Clean separation between Controllers, Services (`ApiUserServiceImpl`, `ProfileService`), and Repositories for maintainability.

## Getting Started

### Prerequisites
- Java 17+
- Maven
- A preferred relational database (e.g., MySQL / PostgreSQL / H2)

### Running the Project

1. **Clone the repository:**
   ```bash
   git clone https://github.com/sarangsvkm/portfolio_api.git
   cd portfolio_api
   ```

2. **Configure Database Connection:**
   Update your `src/main/resources/application.properties` with the correct database credentials.

3. **Build and Run:**
   Using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(For Windows use `mvnw.cmd spring-boot:run`)*

The application will start up, automatically sync the schema, and expose the REST endpoints for front-end integration.
