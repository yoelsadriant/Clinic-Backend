# IB Clinic — Full-Stack Management System

A complete clinic management system for IB Clinic, ported from the original Unity desktop app to a modern web stack.

## Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.3.5 · Java 21 · Spring Security 6 · JWT |
| Database | PostgreSQL 16 · Flyway migrations |
| Frontend | React 18 · Vite · Tailwind CSS · React Router v6 |
| Infra | Docker Compose · AWS ECS Fargate (prod) · RDS PostgreSQL |
| Tests | JUnit 5 · Mockito · TestContainers |

---

## Quick Start (Local Dev)

### Prerequisites
- Java 21+
- Docker (for the database)
- Node.js 22+ (LTS)

### 1 — Start the database
```bash
cd backend
docker compose up db -d
```

### 2 — Start the backend
```bash
cd backend
./mvnw spring-boot:run -DskipTests
# API available at http://localhost:8080
# Swagger UI at http://localhost:8080/swagger-ui.html
```

### 3 — Start the frontend
```bash
cd frontend
npm install
npm run dev
# App available at http://localhost:3000
```

Default test credentials (seed data via Flyway):
```
username: admin    password: admin123   role: ADMIN
username: doctor1  password: doctor123  role: DOCTOR
```

---

## Project Structure

```
.
├── backend/
│   ├── src/
│   │   ├── main/java/com/ultimindstudio/clinic/
│   │   │   ├── config/          SecurityConfig, JwtConfig, OpenApiConfig, DataSeeder
│   │   │   ├── controller/      REST controllers (all modules)
│   │   │   ├── dto/             Request / Response records
│   │   │   ├── entity/          JPA entities
│   │   │   ├── exception/       BusinessException, ResourceNotFoundException, GlobalExceptionHandler
│   │   │   ├── repository/      Spring Data JPA repositories
│   │   │   └── service/         Service interfaces + impl/
│   │   └── main/resources/
│   │       ├── application.properties         (local dev)
│   │       ├── application-docker.properties  (Docker Compose full-stack)
│   │       ├── application-prod.properties    (AWS ECS + RDS)
│   │       └── db/migration/                  Flyway SQL migrations
│   ├── Dockerfile           (multi-stage, non-root user)
│   ├── ecs-task-definition.json
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── api/        Axios instance with JWT interceptor
│   │   ├── context/    AuthContext (login / logout / role helpers)
│   │   ├── components/ Layout, Modal, reusable UI
│   │   ├── pages/      Login, Dashboard, Schedule, Clients, Staff,
│   │   │               Treatments, Products, Sales, Reports
│   │   └── utils/      formatRupiah, formatDate
│   └── vite.config.js  (proxies /api → backend :8080)
│   └── docker-compose.yml   (db + app containers)
└── ecs-task-definition.json
```

---

## API

All endpoints are prefixed `/api/v1` and secured with JWT Bearer tokens.

| Module | Endpoints |
|---|---|
| Auth | `POST /auth/login` · `POST /auth/register` |
| Dashboard | `GET /dashboard/stats` |
| Clients | CRUD `/clients` |
| Staff | CRUD `/staff` |
| Treatments | CRUD `/treatments` |
| Packages | CRUD `/packages` · `/package-details` |
| Products | CRUD `/products` |
| Sales | CRUD `/sales` |
| Schedule | CRUD `/schedules` |
| Installments | CRUD `/installments` · `/history-installments` |
| Reports | `GET /reports/revenue` · `GET /reports/revenue/total` |

Full interactive docs: **http://localhost:8080/swagger-ui.html**

---

## Role-Based Access

| Role | Access |
|---|---|
| `ADMIN` | All modules + Reports + user management |
| `RECEPTIONIST` | Schedule, Clients, Staff, Treatments, Products, Sales |
| `DOCTOR` | Schedule (own), Clients (read), Revenue view |

---

## Frontend Screens

Ported from the original Unity 5.3 desktop app:

| Screen | Route | Description |
|---|---|---|
| Login | `/login` | JWT authentication |
| Dashboard | `/dashboard` | Stats cards + module nav |
| Schedule | `/schedule` | Appointment management with status |
| Clients | `/clients` | Client profiles + installment tracking |
| Staff | `/staff` | Staff / doctor management |
| Treatments | `/treatments` | Treatments + packages |
| Products | `/products` | Product inventory |
| Sales | `/sales` | POS — products, treatments, packages, installments |
| Reports | `/reports` | Revenue analytics (ADMIN only) |

---

## Running Tests

```bash
cd backend

# Unit tests only (always work, no Docker needed)
./mvnw test -Dtest="*ServiceImpl*"

# All tests (integration tests skip gracefully if Docker API incompatible)
./mvnw test
```

**Test coverage:**
- 16 unit tests (AuthService × 4, ClientService × 8, ScheduleService × 4) — always pass
- Integration tests (Spring context load, AuthController, ClientController) — require Docker with API ≥ 1.41

> Note: TestContainers integration tests are skipped automatically (`disabledWithoutDocker = true`) on Rancher Desktop due to a docker-java 1.32 probe vs. Rancher Desktop minimum API 1.41 incompatibility. They run on standard Docker Engine / CI.

---

## Docker (Full-Stack)

```bash
cd backend

# Start both database + app in containers
docker compose up --build

# App: http://localhost:8080
```

The `docker-compose.yml` builds the Spring Boot jar, waits for Postgres health check, then starts the app with `SPRING_PROFILES_ACTIVE=docker`.

---

## Production (AWS ECS)

See `ecs-task-definition.json` for the full Fargate task definition. Sensitive config is read from **AWS Secrets Manager**:

```
clinic/prod/datasource → DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD
clinic/prod/jwt        → JWT_SECRET
```

Health check: `GET /actuator/health` (public endpoint).

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/ibclinic` | PostgreSQL JDBC URL |
| `DATASOURCE_USERNAME` | `clinic_user` | DB username |
| `DATASOURCE_PASSWORD` | `clinic_pass` | DB password |
| `JWT_SECRET` | *(dev key)* | HS256 signing key (min 32 chars) |
| `JWT_EXPIRATION_MS` | `86400000` | Token TTL in ms (default: 24 h) |

Copy `.env.example` to `.env` for local overrides.
