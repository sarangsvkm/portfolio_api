# Portfolio API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.12-6DB33F)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00)](https://www.oracle.com/java/technologies/javase/17-archive-downloads.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Spring Boot backend for a portfolio site. It exposes CRUD APIs for profile, resume, projects, education, experience, skills, uploads, contact requests, and system configuration.

The app is served under the context path `/portfolioApi`, so a local endpoint looks like `http://localhost:8082/portfolioApi/api/profile`.

## Features

- Portfolio domain APIs for profile, skills, projects, experience, and education
- Aggregated resume sync and read endpoints
- Admin-protected write operations via request headers
- OTP-based contact verification flow
- File upload endpoints for stored files and profile images
- PostgreSQL persistence with Spring Data JPA
- Global `413 Payload Too Large` handling for oversized multipart uploads

## Stack

- Java 17
- Spring Boot 3.5.12
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Spring Mail
- Lombok

## Configuration

Set these environment variables before running the service:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO` optional, defaults to `validate`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`
- `CORS_ALLOWED_ORIGINS`
- `PORT` optional, defaults to `8082`

Multipart limits are configured as:

- `spring.servlet.multipart.max-file-size=10MB`
- `spring.servlet.multipart.max-request-size=20MB`

## Run Locally

```bash
git clone https://github.com/sarangsvkm/portfolio_api.git
cd portfolio_api
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Base URL:

```text
http://localhost:8082/portfolioApi
```

## Authentication Model

Public endpoints can be called directly.

Protected endpoints require these headers:

- `X-Admin-Username`
- `X-Admin-Password`

The filter protects:

- all non-GET `/api/**` requests except `/api/auth/**`, `/api/contact/request-otp`, and `/api/contact/verify-otp`
- all `/api/config/**` requests except `/api/config/version`
- `GET /api/contact/report`

## Main Endpoints

All paths below are relative to `/portfolioApi`.

| Area | Method | Path | Protection |
| --- | --- | --- | --- |
| Auth | `POST` | `/api/auth/register` | Public |
| Auth | `POST` | `/api/auth/login` | Public |
| Profile | `GET` | `/api/profile` | Public |
| Profile | `POST` | `/api/profile` | Admin |
| Profile | `PUT` | `/api/profile/{id}` | Admin |
| Profile | `DELETE` | `/api/profile/{id}` | Admin |
| Social media | `DELETE` | `/api/profile/social/{id}` | Admin |
| Profile image | `POST` | `/api/profile/image/{id}` | Admin |
| Profile image | `GET` | `/api/profile/image/{id}` | Public |
| Profile image | `GET` | `/api/profile/image/name/{name}` | Public |
| Profile image | `DELETE` | `/api/profile/image/{id}` | Admin |
| Resume | `POST` | `/api/resume` | Admin |
| Resume | `GET` | `/api/resume` | Public |
| Resume child delete | `DELETE` | `/api/resume/experience/{id}` | Admin |
| Resume child delete | `DELETE` | `/api/resume/education/{id}` | Admin |
| Resume child delete | `DELETE` | `/api/resume/skill/{id}` | Admin |
| Resume child delete | `DELETE` | `/api/resume/project/{id}` | Admin |
| Experience | `GET` | `/api/experience` | Public |
| Experience | `POST` | `/api/experience` | Admin |
| Experience | `PUT` | `/api/experience/{id}` | Admin |
| Experience | `DELETE` | `/api/experience/{id}` | Admin |
| Education | `GET` | `/api/education` | Public |
| Education | `POST` | `/api/education` | Admin |
| Education | `PUT` | `/api/education/{id}` | Admin |
| Education | `DELETE` | `/api/education/{id}` | Admin |
| Skills | `GET` | `/api/skills` | Public |
| Skills | `POST` | `/api/skills` | Admin |
| Skills | `PUT` | `/api/skills/{id}` | Admin |
| Skills | `DELETE` | `/api/skills/{id}` | Admin |
| Projects | `GET` | `/api/projects` | Public |
| Projects | `POST` | `/api/projects` | Admin |
| Projects | `PUT` | `/api/projects/{id}` | Admin |
| Projects | `DELETE` | `/api/projects/{id}` | Admin |
| Contact | `POST` | `/api/contact/request-otp` | Public |
| Contact | `POST` | `/api/contact/verify-otp` | Public |
| Contact report | `GET` | `/api/contact/report` | Admin |
| Contact | `DELETE` | `/api/contact/{id}` | Admin |
| Config | `GET` | `/api/config/version` | Public |
| Config | `GET` | `/api/config` | Admin |
| Config | `POST` | `/api/config` | Admin |
| Config | `DELETE` | `/api/config/{id}` | Admin |
| Upload | `POST` | `/api/upload` | Admin |
| Upload | `GET` | `/api/upload/view/{filename}` | Public |

## Example Protected Request

```bash
curl -X POST "http://localhost:8082/portfolioApi/api/projects" \
  -H "Content-Type: application/json" \
  -H "X-Admin-Username: admin" \
  -H "X-Admin-Password: admin-password" \
  -d "{\"title\":\"Portfolio API\",\"description\":\"Backend service\"}"
```

## Upload Error Response

If an upload exceeds the configured multipart limit, the API returns:

```json
{
  "timestamp": "2026-04-04T16:30:00Z",
  "status": 413,
  "error": "Payload Too Large",
  "message": "Uploaded file exceeds the configured size limit.",
  "path": "/portfolioApi/api/profile/image/1"
}
```

## Testing

Run tests with:

```bash
./mvnw test
```

The repository also includes a Postman collection at `portfolio-api-postman-collection.json`.

## License

Licensed under the [MIT License](LICENSE.md).
