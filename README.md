# Connected Insights Platform

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-brightgreen?style=flat-square&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)
![Status](https://img.shields.io/badge/Status-MVP-yellow?style=flat-square)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple?style=flat-square)

---

## Repository

Clone the repository:

```bash
git clone https://github.com/JorgePirro/connected-insights.git
cd connected-insights
```

---

## Overview

**Connected Insights** is a microservices-based Competitive Intelligence Platform developed for AstraZeneca's Enterprise AI team. The platform enables leadership and analysts to:

- 📊 **Track Competitors** — Manage competitor profiles and their clinical trial pipelines
- 🔍 **Aggregate Insights** — Collect and score market insights by therapeutic area and impact level
- 🔔 **Receive Notifications** — Subscribe to alerts for high-priority competitive developments

The system follows **Hexagonal Architecture** principles, ensuring clean separation between business logic and infrastructure concerns.

---

## Quick Start (Docker)

The fastest way to run the entire platform is using Docker Compose.

### Prerequisites

- [Docker](https://www.docker.com/get-started) (v20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) (v2.0+)

### Run All Services

```bash
docker-compose up --build
```

This command will build and start all three microservices simultaneously.

### Health Check URLs

Once the services are running, verify their health status:

| Service | Health Check URL | Port |
|---------|------------------|------|
| **Competitor Service** | [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health) | 8081 |
| **Insights Service** | [http://localhost:8082/actuator/health](http://localhost:8082/actuator/health) | 8082 |
| **Notification Service** | [http://localhost:8083/actuator/health](http://localhost:8083/actuator/health) | 8083 |

### Swagger UI (API Documentation)

Interactive API documentation is available for each service:

| Service | Swagger UI URL |
|---------|----------------|
| **Competitor Service** | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| **Insights Service** | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| **Notification Service** | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |

> 💡 **Tip:** You can also access the OpenAPI JSON specs at `/v3/api-docs` (e.g., `http://localhost:8081/v3/api-docs`)

### Stop Services

```bash
docker-compose down
```

---

## Manual Installation (Fallback)

If Docker is not available, you can run each service individually using Maven.

### Prerequisites

- Java 21 (JDK)
- Maven 3.9+ (or use the included Maven Wrapper)

### Run a Single Service

Navigate to the service directory and execute:

```bash
# Example: Running the Competitor Service
cd services/competitor-service
./mvnw spring-boot:run
```

```bash
# Example: Running the Insights Service
cd services/insights-service
./mvnw spring-boot:run
```

```bash
# Example: Running the Notification Service
cd services/notification-service
./mvnw spring-boot:run
```

> **Note (Windows):** Use `mvnw.cmd` instead of `./mvnw`

### Configure Ports (Optional)

Each service runs on its default port. To override, use:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9081
```

---

## Documentation Links

| Document | Description |
|----------|-------------|
| 📐 [ARCHITECTURE.md](./ARCHITECTURE.md) | System design, C4 diagrams, design patterns, and architectural decisions |
| 📡 [API.md](./API.md) | OpenAPI/Swagger specifications, endpoint details, request/response schemas |

---

## Project Structure

```
connected-insights/
├── README.md                    # This file
├── ARCHITECTURE.md              # Architecture documentation
├── API.md                       # API specifications
├── docker-compose.yml           # Docker orchestration
│
└── services/
    ├── competitor-service/      # Port 8081
    │   ├── Dockerfile
    │   ├── pom.xml
    │   └── src/
    │       ├── main/java/com/astrazeneca/competitor_service/
    │       │   ├── domain/          # Entities, Value Objects
    │       │   ├── application/     # Use Cases, Ports
    │       │   └── infrastructure/  # REST Controllers, JPA Adapters
    │       └── test/
    │
    ├── insights-service/        # Port 8082
    │   ├── Dockerfile
    │   ├── pom.xml
    │   └── src/
    │       ├── main/java/com/astrazeneca/insights_service/
    │       │   ├── domain/          # Insight, ImpactLevel
    │       │   ├── application/     # Use Cases, Ports
    │       │   └── infrastructure/  # REST Controllers, JPA Adapters
    │       └── test/
    │
    └── notification-service/    # Port 8083
        ├── Dockerfile
        ├── pom.xml
        └── src/
            ├── main/java/com/astrazeneca/notification_service/
            │   ├── domain/          # Subscription
            │   ├── application/     # Use Cases, Ports
            │   └── infrastructure/  # REST Controllers, JPA Adapters
            └── test/
```

---

## Testing

### Run Unit Tests

Execute tests for a specific service:

```bash
cd services/competitor-service
./mvnw test
```

### Run All Tests

```bash
# From each service directory
cd services/competitor-service && ./mvnw test && cd ../..
cd services/insights-service && ./mvnw test && cd ../..
cd services/notification-service && ./mvnw test && cd ../..
```

### Test Reports

Test results are generated in each service's `target/surefire-reports/` directory.

---

## Design Decisions

### 1. Hexagonal Architecture (Ports & Adapters)

**Decision:** Each microservice implements Hexagonal Architecture with three distinct layers:
- **Domain Layer** — Pure business logic, no framework dependencies
- **Application Layer** — Use cases and port interfaces
- **Infrastructure Layer** — Spring Boot controllers, JPA repositories, external clients

**Rationale:**
- ✅ **Testability** — Domain logic can be unit-tested without Spring context
- ✅ **Framework Independence** — Business rules are decoupled from Spring Boot and JPA
- ✅ **Maintainability** — Clear boundaries make the codebase easier to navigate and extend

### 2. H2 In-Memory Database

**Decision:** Use H2 embedded database for all services during MVP phase.

**Rationale:**
- ✅ **Development Velocity** — Zero configuration, instant startup
- ✅ **Testing** — Embedded mode enables fast integration tests
- ✅ **MVP Constraint** — Focus on business logic over infrastructure setup

**Future State:** PostgreSQL or MySQL for production persistence.

### 3. Synchronous REST Communication

**Decision:** All inter-service communication uses synchronous HTTP/REST.

**Rationale:**
- ✅ **Simplicity** — Easier debugging and tracing during MVP
- ✅ **Familiarity** — Standard REST patterns for team onboarding
- ✅ **Sufficient for MVP** — Current load requirements don't demand async

**Future State:** Asynchronous event-driven messaging (RabbitMQ/Kafka) for:
- Notification dispatch
- Cross-service event propagation
- Improved resilience and decoupling

---

## API Quick Reference

### Competitor Service (Port 8081)

```
GET    /api/v1/competitors          # List all competitors
POST   /api/v1/competitors          # Create competitor
GET    /api/v1/competitors/{id}     # Get competitor by ID
PUT    /api/v1/competitors/{id}     # Update competitor
DELETE /api/v1/competitors/{id}     # Delete competitor
GET    /api/v1/competitors/{id}/trials  # Get competitor's trials
```

### Insights Service (Port 8082)

```
GET    /api/v1/insights             # List all insights
POST   /api/v1/insights             # Create insight
GET    /api/v1/insights/{id}        # Get insight by ID
PUT    /api/v1/insights/{id}        # Update insight
DELETE /api/v1/insights/{id}        # Delete insight
```

### Notification Service (Port 8083)

```
GET    /api/v1/subscriptions        # List subscriptions
POST   /api/v1/subscriptions        # Create subscription
GET    /api/v1/subscriptions/{id}   # Get subscription by ID
DELETE /api/v1/subscriptions/{id}   # Delete subscription
```

> 📖 See [API.md](./API.md) for complete endpoint specifications.

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_PORT` | 8080 | Override service port |
| `SPRING_PROFILES_ACTIVE` | default | Active Spring profile |
| `LOGGING_LEVEL_ROOT` | INFO | Root logging level |

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is proprietary to AstraZeneca's Enterprise AI Team.

---

## Contact

**Enterprise AI Team** — AstraZeneca

For questions or support, please contact jfalcigno@gmail.com
