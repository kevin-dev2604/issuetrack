# IssueTrack (이슈 트래커)

사용자 정의 워크플로우를 기반으로 이슈를 관리하고, 협업이 필요한 순간 ‘딜레마’를 개설하여 다른 유저들과 토론할 수 있는 백엔드 중심의 개인 이슈 트래킹 시스템입니다.

A backend-focused personal issue tracking system that helps users define workflows, manage issues, and open dilemmas for discussion when collaboration is needed.

---

## 📌 개요 (Overview)

IssueTrack은 개인이 자신만의 작업 프로세스를 정의하고 이슈를 관리할 수 있도록 돕는 백엔드 포트폴리오 프로젝트입니다. 특히, 혼자 해결하기 어려운 이슈를 공론화(Dilemma)하여 커뮤니티 및 사이드 프로젝트 팀원들과 함께 논의할 수 있는 소셜 기능을 결합했습니다.

IssueTrack is a backend portfolio project that implements a personal issue management system, allowing users to define their own workflows, manage issues accordingly, and turn issues that require collaboration into public dilemmas for discussion and community feedback.

---

## ✨ 핵심 기능 (Key Features)

* **인증 및 인가 (Authentication & Authorization)**
  * JWT 기반의 자체 로그인 및 Spring Security 적용
  * OAuth2 (Google, GitHub) 소셜 로그인 연동 (프론트엔드 로그인 후 백엔드 토큰 검증)
  * *JWT-based authentication with Spring Security*
  * *OAuth2 (Google, GitHub) social login integration (Frontend-initiated auth flow)*
* **사용자 정의 프로세스 (User-Defined Workflows)**
  * 유저가 직접 프로세스와 단계(Steps)를 정의하고 이슈에 적용
  * *Create and manage user-defined processes and process-specific steps*
* **이슈 및 딜레마 관리 (Issue & Dilemma Management)**
  * 이슈 생성, 수정, 상세 조회 및 다차원 조건 검색
  * 협업이나 조언이 필요한 이슈를 '딜레마'로 전환하여 공개 토론 개설
  * *Issue lifecycle management (CRUD, advanced search)*
  * *Convert issues into public dilemmas to discuss with other users*
* **알림 시스템 (Notification System)**
  * Google FCM(Firebase Cloud Messaging)을 활용한 푸시 알림 이벤트 처리
  * *Push notification event handling powered by Google FCM*
* **유연한 데이터 구조 (Flexible Data Structure)**
  * 계층형 카테고리 구조 및 동적 속성 관리
  * 안정적인 유효성 검증(Validation) 및 글로벌 예외 처리
  * *Hierarchical category structure with global exception handling*
* **어드민 및 통계 (Admin & Statistics)**
  * 카테고리 및 속성(Attributes) 관리 시스템
  * jOOQ를 활용한 복잡한 재귀형 통계 및 대시보드 쿼리 구현
  * *Category & Attributes management and statistical queries implemented with jOOQ*

---

## 🛠 기술 스택 (Tech Stack)

* **Language & Framework**: Java 21, Spring Boot 4, Spring Security (OAuth2 Client)
* **Persistence & Query**: JPA / Hibernate, QueryDSL, jOOQ, PostgreSQL
* **Build & Infrastructure**: Gradle, Docker / Docker Compose
* **Notification**: Google FCM (Firebase Cloud Messaging)
* **Documentation**: Swagger / OpenAPI 3.0

---

## 🏗 아키텍처 및 기술적 결정 (Architecture & Technical Decisions)

### 1. 유즈케이스 지향 구조 및 패키지 정리 (Use-Case Oriented Structure & Package Refactoring)
도메인의 핵심 규칙을 보호하고 유지보수성을 극대화하기 위해 유즈케이스 지향 아키텍처를 채택했습니다. 최근 가독성과 확장성을 높이기 위해 전체 패키지 구조를 명확하게 재정리했습니다.
* **Domain Layer**: 비즈니스 룰과 코어 모델 정의
* **Application Layer**: 유즈케이스(Use-cases) 흐름 제어
* **Infrastructure Layer**: 영속성(Persistence), 보안, 외부 연동(FCM 등)

We follow a use-case oriented application structure to isolate core business logic. The package layout has been recently restructured for better readability and extensibility.
* *Domain layer: contains business rules and core models*
* *Application layer: orchestrates use cases*
* *Infrastructure layer: persistence, security, and external integrations (FCM, etc.)*

### 2. QueryDSL과 jOOQ의 하이브리드 운영 (Why QueryDSL and jOOQ together?)
조회 목적에 따라 가장 적합한 도구를 분리하여 사용하는 전략을 취했습니다.
* **QueryDSL**: 애플리케이션의 일반적인 검색, 수정, 상세 조회용 (JPA 연계성 우수)
* **jOOQ**: 대시보드, 통계, 계층형 카테고리를 위한 재귀/집계 SQL 쿼리용

This intentional separation keeps standard application flows aligned with Spring/JPA conventions via QueryDSL, while complex reporting/statistical queries remain explicit, performant, and SQL-friendly through jOOQ.

---

## 📂 프로젝트 구조 (Project Structure)

최근 가독성 향상과 기능 레이어 분리를 위해 패키지 구조를 아래와 같이 정비했습니다.

```text
src/main/java
 ┗ com
  ┗ kevinj
   ┗ portfolio
    ┗ issuetrack
     ┣ auth         # JWT & OAuth2 (Google, GitHub)
     ┣ user         # 유저 도메인 (User Domain)
     ┣ process      # 워크플로우 정의 (Workflow Process)
     ┣ issue        # 이슈 관리 (Issue Management)
     ┣ dilemma      # 토론 및 딜레마 (Dilemma Community)
     ┣ admin        # 통계 및 어드민 (Admin & jOOQ Statistics)
     ┣ storage      # 저장소 및 파일첨부 (Storage & File Uploads)
     ┗ global       # 공통 예외 및 설정 (Global Config & Exception)
```

---

## 🚀 시작하기 (Getting Started)

### 사전 요구사항 (Prerequisites)
* Java 21
* Docker & Docker Compose

### 로컬 실행 (Run locally)

```Bash
git clone https://github.com/kevin-dev2604/issuetrack.git
# 로컬에서 바로 실행할 경우
cd issuetrack
./gradlew bootRun

# 로컬 docker에 올려서 실행할 경우
cd issuetrack
docker-compose up --build -d
```

### 빌드 및 테스트 (Build & Test)

```Bash
# Build
./gradlew clean build

# Test
./gradlew test
```

### API 문서 확인 (API Documentation)
```
http://localhost:8080/swagger-ui/index.html
```

---

## 💡 프로젝트를 통해 증명하고자 하는 것 (What this project demonstrates)
* 실무 중심의 문제 해결: 단순 CRUD를 넘어 OAuth2, FCM 푸시, 통계 전용 쿼리 튜닝(jOOQ) 등 실제 프로덕션 수준의 기술적 고민 반영  
Designing a backend system around strict domain rules and clear use cases.
* 유지보수하기 좋은 코드: 유즈케이스 기반의 레이어드 아키텍처와 깔끔한 패키지 구조 설계 능력  
Implementing secure APIs with Spring Boot, combining social logins and robust validation.
* 효율적인 도구 선택: 표준 비즈니스 로직(JPA/QueryDSL)과 통계 분석(jOOQ)의 역할을 명확히 분리하는 안목  
Choosing the right query tool (QueryDSL vs jOOQ) for the specific performance and maintenance goal.
* 인프라 자동화 구성: Docker Compose를 활용하여 언제 어디서나 즉시 구동 가능한 샌드박스 환경 구축  
Setting up a reproducible and fully documented local development ecosystem.

---

## 📬 연락처 (Contact)
사이드 프로젝트 협업, 백엔드 기술 교류, 또는 오픈소스 기여 관련 논의는 언제나 환영합니다!

Feel free to reach out for side projects, technical discussions, or remote opportunities.
* GitHub: <https://github.com/kevin-dev2604/issuetrack>
* Email: <kevin.j.dev.2604@gmail.com>
