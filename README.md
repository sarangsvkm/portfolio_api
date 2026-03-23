# 🚀 Portfolio API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.12-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A robust, enterprise-ready Spring Boot backend designed as the centralized data layer for professional portfolio websites. This API provides secure, encrypted storage for your professional identity, experiences, skills, projects, and education.

---

## 🌟 Key Features

- **🎯 Domain-Driven Architecture**: Structured entities for Profile, Projects, Education, Experience, and Skills.
- **📄 Atomic Resume API**: Powerful `POST /api/resume` endpoint to sync your entire professional profile in one request.
- **🔗 Dynamic Social Media**: Manage a flexible list of social media links (LinkedIn, GitHub, etc.) with automatic encryption.
- **🛡️ Multi-Layer Security**:
  - **Authenticated Mutators**: All POST, PUT, and DELETE operations are protected by credential verification.
  - **AES Encryption**: Sensitive fields are encrypted at rest (AES/ECB/PKCS5Padding).
  - **Secure Config**: Database-driven system configuration for mail and CORS settings.
- **📱 OTP-Gated Contact Reveal**: Protects personal contact details behind a secure One-Time Password verification flow.
- **📧 Automated Mail Services**: Integrated dispatcher for contact requests and security alerts.

---

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.12
- **Language**: Java 17
- **Database**: PostgreSQL (Optimized for Neon.tech)
- **Security**: Spring Security 6+
- **Utilities**: Lombok, Spring Mail, AES Encryption

---

## 🚀 Getting Started

### Prerequisites
- **JDK 17+**
- **Maven 3.6+**
- **PostgreSQL Instance**

### Installation
1. **Clone & Navigate**
   ```bash
   git clone https://github.com/sarangsvkm/portfolio_api.git
   cd portfolio_api
   ```

2. **Configure Environment**
   The API supports environment variables for production security. Update `src/main/resources/application.properties` or set the following vars:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `PORT` (Defaults to 8080)

3. **Run Application**
   ```bash
   mvnw spring-boot:run
   ```

---

## 🔐 API Documentation (v2.0)

The API is served at the root level (`/`). All mutation requests require `username` and `password` in the request body.

### Endpoints Overview

| Category | Endpoint | Method(s) | Auth |
| :--- | :--- | :--- | :--- |
| **Auth** | `/api/auth/register`, `/api/auth/login` | POST | No |
| **Profile** | `/api/profile` | GET, POST, PUT | Required (POST/PUT) |
| **Social Links** | `/api/profile/social/{id}` | DELETE | Required |
| **Resume** | `/api/resume` | GET, POST | Required (POST) |
| **Standalone** | `/api/experience`, `/api/education`, `/api/skills`, `/api/projects` | GET, POST, PUT, DELETE | Required (POST/PUT/DELETE) |
| **Contact** | `/api/contact/request-otp`, `/api/contact/verify-otp` | POST | No |
| **System** | `/api/config` | GET, POST, DELETE | Required (Headers) |

---

## 🧪 Testing with Postman
A pre-configured Postman collection is included: `portfolio-api-postman-collection.json`.
1. Import the file into Postman.
2. Update the `baseUrl` variable to match your server (e.g., `http://localhost:8080`).
3. Use the included samples to test Create, Update, and Secure Delete operations.

---

## 📄 License
Licensed under the [MIT License](LICENSE.md).

## 👨‍💻 Author
**Sarang** - [GitHub](https://github.com/sarangsvkm)
