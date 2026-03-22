# Portfolio API

A robust, enterprise-ready Spring Boot backend for managing professional portfolio data. This API serves as the centralized data layer for portfolio websites, providing structured endpoints to manage a user's professional identity, experiences, skills, projects, and education.

## Overview

The `portfolio-api` is designed with a clear domain-driven architecture, implementing various entities to thoroughly capture a professional's resume and portfolio details. 

It also includes built-in User Management, Header-based Authentication features, and automatic Data Encryption to securely manage information in the database.

## Core Entities

The API is built around several core entities mapping directly to the relational database structure (JPA/Hibernate):

1. **Profile**: General information (`name`, `title`, `about`, `email`, `phone`, `location`).
2. **Projects**: Showcase of professional or personal projects.
3. **Education**: Academic background and achievements.
4. **Experience**: Work history and professional roles.
5. **Skills**: Technical or soft skills acquired.
6. **API User**: Internal users to handle the authentication layer, ensuring that portfolio details are securely managed.

## Security & Features

- **Header-Based Authentication**: All `POST` endpoints are secured. To create or update records, clients must provide `username` and `password` as HTTP Headers (`@RequestHeader`).
- **Data Encryption**: The service layer seamlessly utilizes `EncryptionUtils` to encrypt sensitive entity features (such as `password`, `degree`, `institution`, project `description`, etc.) upon saving, and then automatically decrypts them during `GET` retrieval.
- **Spring Boot Ecosystem**: Utilizes Spring Web, Spring Data JPA, and Spring Boot Starters for reliable REST API development.
- **Lombok Integration**: Reduces boilerplate code by automatically generating Getters, Setters, and Constructors.
- **Clean Architecture**: Distinct separation between Controllers, Services, and Repositories for maintainability.

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
   Using the Maven wrapper for Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```
   *(For Unix use `./mvnw spring-boot:run`)*

The application will start up, automatically sync the schema, and expose the REST endpoints for front-end integration.
