# 🚀 Portfolio API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.12-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A robust, enterprise-ready Spring Boot backend designed to serve as the centralized data layer for professional portfolio websites. This API provides structured endpoints to manage a user's professional identity, experiences, skills, projects, and education with built-in security and data integrity.

---

## 🌟 Key Features

- **🎯 Domain-Driven Design**: Clearly defined entities for Profile, Projects, Education, Experience, and Skills.
- **📄 Unified Resume API**: A powerful `POST /api/resume` endpoint to save or update an entire professional profile, including all sub-entities, in a single atomic request.
- **🔄 Full CRUD Lifecycle**: Support for creating, reading, and updating professional records for all domain entities.
- **🛡️ Secure By Design**:
  - **Header-Based Authentication**: Custom security layer requiring `username` and `password` headers for all mutation (POST/PUT/DELETE) operations.
  - **AES Data Encryption**: All sensitive fields across all entities are encrypted at rest using `EncryptionUtils` (AES/ECB/PKCS5Padding) and transparently decrypted for authorized retrieval.
- **📱 OTP-Gated Contact Reveal**: A unique feature that protects personal contact information (phone number) behind a One-Time Password verification flow for visitors.

- **📧 Integrated Mail Services**: Automated email dispatch for contact requests and OTP verification.
- **🗄️ Relational Persistence**: Fully integrated with PostgreSQL via Spring Data JPA for reliable data storage.
- **⚡ Developer Optimized**: Utilizes Lombok to minimize boilerplate and Spring DevTools for rapid iteration.

---

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.5.12
- **Language**: Java 17
- **Data Access**: Spring Data JPA / Hibernate
- **Database**: PostgreSQL (Optimized for Neon)
- **Security**: Spring Security (Custom Filter Chain)
- **Utilities**: Lombok, Spring Mail, Jasypt-derived Encryption

---

## 📁 Project Structure

```text
src/main/java/com/sarangsvkm/portfolio_api/
├── controller/      # REST Endpoints
├── service/         # Business Logic & Encryption logic
├── repository/      # Data Access Layer
├── entity/          # JPA Entities
├── config/          # Security & Application Configuration
├── encryptionUtils/ # AES Encryption Utilities
└── apiuser/         # Authentication & User Management
```

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.6+**
- **PostgreSQL Database** (or any compatible RDBMS)

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/sarangsvkm/portfolio_api.git
   cd portfolio_api
   ```

2. **Configure Environment**
   Update `src/main/resources/application.properties` with your database and mail server credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-db-host:5432/your-db
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```

3. **Build and Run**
   ```bash
   # Windows
   mvnw.cmd spring-boot:run
   
   # Linux/macOS
   ./mvnw spring-boot:run
   ```

---

## 🔐 Security & API Authentication

All non-GET requests (POST, PUT) require the following headers for authentication:

| Header | Description |
| :--- | :--- |
| `username` | Your administrative username |
| `password` | Your administrative password |

*Note: In production environments, it is highly recommended to use HTTPS to protect these headers.*

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](file:///e:/SRG/portfolio-api/LICENSE.md) file for details.

---

## 👨‍💻 Author

**Sarang** - *Lead Developer* - [GitHub](https://github.com/sarangsvkm)
