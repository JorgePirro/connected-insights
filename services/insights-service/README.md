# Insights Service - Hexagonal Architecture

## Overview
This is a Spring Boot 3.4 microservice built with **Hexagonal Architecture (Ports and Adapters)** pattern for managing competitive insights in the pharmaceutical industry.

## Tech Stack
- **Java 21**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **H2 Database** (In-Memory)
- **Lombok** (Boilerplate reduction)
- **MapStruct** (Layer mapping)
- **Spring Boot Actuator** (Health endpoint)
- **SpringDoc OpenAPI 3** (Swagger UI for API documentation)

## Architecture

### Hexagonal Architecture Layers

```
insights-service/
├── domain/                          # DOMAIN LAYER (Core Business Logic)
│   ├── model/
│   │   ├── Insight.java            # Pure Java - No framework dependencies
│   │   └── ImpactLevel.java        # Enum (LOW, MEDIUM, HIGH, CRITICAL)
│   └── ports/
│       ├── in/
│       │   └── ManageInsightUseCase.java     # Input Port (Interface)
│       └── out/
│           └── InsightRepositoryPort.java    # Output Port (Interface)
│
├── application/                     # APPLICATION LAYER (Use Cases)
│   └── service/
│       └── InsightService.java     # Implements Input Port
│
└── infrastructure/                  # INFRASTRUCTURE LAYER (Adapters)
    ├── persistence/                 # Persistence Adapter
    │   ├── entity/
    │   │   └── InsightEntity.java           # JPA Annotations
    │   ├── repository/
    │   │   └── JpaInsightRepository.java    # Spring Data JPA
    │   ├── adapter/
    │   │   └── InsightPersistenceAdapter.java  # Implements Output Port
    │   └── mapper/
    │       └── PersistenceMapper.java       # MapStruct: Entity <-> Domain
    │
    └── web/                         # Web Adapter
        ├── controller/
        │   ├── InsightController.java       # REST Controller
        │   └── GlobalExceptionHandler.java  # @RestControllerAdvice
        ├── dto/
        │   ├── CreateInsightRequest.java
        │   ├── InsightResponse.java
        │   ├── ErrorResponse.java           # Standard Error DTO
        │   └── ValidationErrorResponse.java
        └── mapper/
            └── WebMapper.java               # MapStruct: DTO <-> Domain
```

## Key Principles

### 1. **Domain Layer (Pure Java)**
- No Spring, JPA, or Jackson annotations
- Contains business logic (e.g., `Insight.create()`, `Insight.update()`)
- Defines interfaces (Ports) that outer layers implement
- Calculates relevance score based on impact level

### 2. **Application Layer**
- Implements Use Cases (Input Ports)
- Orchestrates domain logic
- Calls Output Ports for persistence

### 3. **Infrastructure Layer**
- **Persistence Adapter**: Implements domain repository interface, uses JPA
- **Web Adapter**: REST controllers, DTOs, handles HTTP requests
- **MapStruct Mappers**: Clean separation between layers
- **Global Exception Handler**: Consistent error responses

## API Endpoints

### Swagger UI
```
GET /swagger-ui.html
```
Interactive API documentation with try-it-out functionality

### OpenAPI Specification
```
GET /v3/api-docs
```
JSON format of the OpenAPI specification

### Health Check
```
GET /actuator/health
```

### Insight Operations

#### Create Insight
```
POST /api/insights
Content-Type: application/json

{
  "description": "Competitor launched new oncology drug targeting EGFR mutations",
  "therapeuticArea": "Oncology",
  "competitorId": "550e8400-e29b-41d4-a716-446655440000",
  "impactLevel": "HIGH"
}
```

#### Get All Insights
```
GET /api/insights
GET /api/insights?therapeuticArea=Oncology
```

#### Get Insight by ID
```
GET /api/insights/{id}
```

#### Update Insight
```
PUT /api/insights/{id}
Content-Type: application/json

{
  "description": "Updated insight description",
  "therapeuticArea": "Oncology",
  "competitorId": "550e8400-e29b-41d4-a716-446655440000",
  "impactLevel": "CRITICAL"
}
```

#### Delete Insight
```
DELETE /api/insights/{id}
```

## Running the Application

### Build
```bash
mvnw clean install
```

### Run
```bash
mvnw spring-boot:run
```

### Access
- Application: http://localhost:8082
- **Swagger UI: http://localhost:8082/swagger-ui.html** 📚
- API Docs (JSON): http://localhost:8082/v3/api-docs
- H2 Console: http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:insightsdb`
  - Username: `sa`
  - Password: (empty)
- Health: http://localhost:8082/actuator/health

## Testing Examples

### Create an Insight
```bash
curl -X POST http://localhost:8082/api/insights \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Pfizer announces Phase III results for lung cancer treatment",
    "therapeuticArea": "Oncology",
    "competitorId": "550e8400-e29b-41d4-a716-446655440000",
    "impactLevel": "HIGH"
  }'
```

### Get All Insights
```bash
curl http://localhost:8082/api/insights
```

### Get Insights by Therapeutic Area
```bash
curl "http://localhost:8082/api/insights?therapeuticArea=Oncology"
```

### Get Insight by ID
```bash
curl http://localhost:8082/api/insights/{id}
```

### Update an Insight
```bash
curl -X PUT http://localhost:8082/api/insights/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Updated: Pfizer Phase III shows 40% improvement",
    "therapeuticArea": "Oncology",
    "competitorId": "550e8400-e29b-41d4-a716-446655440000",
    "impactLevel": "CRITICAL"
  }'
```

### Delete an Insight
```bash
curl -X DELETE http://localhost:8082/api/insights/{id}
```

## Error Handling

The service uses a `GlobalExceptionHandler` for consistent error responses:

### Error Response Format
```json
{
  "timestamp": "2026-02-08T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "{description=Description is required}"
}
```

### Handled Exceptions
| Exception | HTTP Status | Description |
|-----------|-------------|-------------|
| `MethodArgumentNotValidException` | 400 | Validation errors |
| `InsightNotFoundException` | 404 | Insight not found |
| `Exception` | 500 | Internal server error |

## Domain Model

### Insight
| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| description | String | Insight description |
| therapeuticArea | String | Therapeutic area (e.g., Oncology, Cardiology) |
| competitorId | UUID | Reference to competitor |
| impactLevel | ImpactLevel | LOW, MEDIUM, HIGH, CRITICAL |
| relevanceScore | Integer | Calculated based on impact level |

### ImpactLevel (Enum)
| Value | Score |
|-------|-------|
| LOW | 25 |
| MEDIUM | 50 |
| HIGH | 75 |
| CRITICAL | 100 |

## Design Decisions

1. **Domain Purity**: Domain models are pure Java POJOs with no framework dependencies
2. **Dependency Inversion**: Domain defines ports; infrastructure implements them
3. **MapStruct**: Clean, type-safe mapping between layers
4. **Relevance Score Calculation**: Business logic in domain model based on impact level
5. **Transaction Management**: Applied at application service layer
6. **Global Exception Handling**: Consistent error responses with `@RestControllerAdvice`
7. **API Documentation**: SpringDoc OpenAPI 3 with comprehensive Swagger annotations
8. **Validation**: Bean validation on request DTOs (`@Valid`, `@NotBlank`, `@NotNull`)
9. **Filtering**: Support for filtering insights by therapeutic area

## Future Enhancements

- Integration tests for full flow
- Docker support
- Metrics and monitoring
- Pagination for list endpoints
- Full-text search capabilities
- Integration with competitor-service
- Integration with notification-service for alerts
