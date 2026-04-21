# IssueTrack

A backend-focused personal issue tracking system that helps users define workflows, manage issues, and open dilemmas for discussion when collaboration is needed.

## Overview

IssueTrack is a backend portfolio project that implements a personal issue management system, allowing users to define their own workflows, manage issues accordingly, and turn issues that require collaboration into public dilemmas for discussion.

## Key Features

- User authentication and authorization with JWT
- Create and manage user-defined processes and process-specific steps
- Issue creation, update, search, and detail retrieval
- Mark issues that require collaboration as dilemmas and discuss them with other users
- Hierarchical category structure
- Validation and global exception handling
- Local development environment with Docker Compose
- Swagger / OpenAPI documentation
- Admin Functions
  - Categories & Attributes management
  - Statistics queries implemented with jOOQ

## Tech Stack
- Java 21
- Spring Boot 4
- Spring Security
- JPA / Hibernate
- QueryDSL
- jOOQ
- PostgreSQL
- Gradle
- Docker / Docker Compose

## Architecture Highlights
This project follows a use-case oriented application structure.
- **Domain layer**: contains business rules and core models
- **Application layer**: orchestrates use cases
- **Infrastructure layer**: persistence, security, and external integrations
- **Query strategy split**:
  - QueryDSL for general search/detail queries
  - jOOQ for reporting, aggregation, and recursive/statistical queries

This separation was intentional so that complex reporting queries can remain explicit and SQL-friendly, while standard application flows stay aligned with Spring/JPA conventions.

## Getting Started

### Prequistes
- Java 21
- Docker
- Docker Compose

### Run locally
```Bash
git clone https://github.com/kevin-dev2604/issuetrack.git
cd issuetrack
docker compose up -d
./gradlew bootRun
```

### Build
```Bash
./gradlew clean build
```

### API Documentation
If Swagger is enabled, open:
```
http://localhost:8080/swagger-ui/index.html
```

### Testing
This project includes tests for key business flows and API behavior.

Examples:
- use-case tests
- API tests
- validation / exception response tests
- query-related verification for search and retrieval flows

Run tests with:
```Bash
./gradlew test
```

## Technical Decisions

### Why QueryDSL and jOOQ together?
I used different tools for different query responsibilities.
- QueryDSL is used for application-facing search and retrieval queries
- jOOQ is used for complex SQL, aggregation, and recursive statistics queries
  This approach keeps the codebase practical:
- simpler application queries remain easy to maintain
- advanced reporting queries stay expressive and close to SQL

### Why Docker Compose?
I wanted the local environment to be reproducible and easy to run, especially for database-dependent backend development.

## What this project demonstrates
This repository is intended to demonstrate that I can:
- design a backend system around use cases and domain rules
- implement secure APIs with Spring Boot
- write maintainable query code with the right tool for each job
- structure a project for readability and extensibility
- set up a reproducible local environment with Docker
- document technical decisions clearly

### Limitations / Future Improvements
- broader integration test coverage
- CI pipeline setup
- deployment configuration
- improved observability and logging strategy
- additional production-oriented hardening

## My Role
This is a solo backend portfolio project.

I was responsible for:
- domain design
- database schema design
- API implementation
- authentication / authorization
- query implementation
- exception handling
- test code
- Docker-based local environment setup
- project documentation

## Project Structure

```text
src/main/java/com/kevinj/portfolio/issuetrack
 ┗ com
  ┗ kevinj
   ┗ portfolio
    ┗ issuetrack
     ┣ admin
     ┣ user
     ┣ auth
     ┣ process
     ┣ issue
     ┣ dilemma
     ┗ global
 ```
> The exact package structure may differ depending on the current branch/version.

## Contact
If you would like to discuss backend freelance work or remote opportunities, feel free to reach out.
- GitHub: <https://github.com/kevin-dev2604/issuetrack>
- Email: <kevin.j.dev.2604@gmail.com>