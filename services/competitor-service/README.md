# Competitor Service - Hexagonal Architecture

## Overview
This is a Spring Boot 3.4 microservice built with **Hexagonal Architecture (Ports and Adapters)** pattern.

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
competitor-service/
├── domain/                          # DOMAIN LAYER (Core Business Logic)
│   ├── model/
│   │   ├── Competitor.java         # Pure Java - No framework dependencies
│   │   └── ClinicalTrial.java      # Pure Java - No framework dependencies
│   └── ports/
│       ├── in/
│       │   └── ManageCompetitorUseCase.java    # Input Port (Interface)
│       └── out/
│           └── CompetitorRepositoryPort.java   # Output Port (Interface)
│
├── application/                     # APPLICATION LAYER (Use Cases)
│   └── service/
│       └── CompetitorService.java  # Implements Input Port
│
└── infrastructure/                  # INFRASTRUCTURE LAYER (Adapters)
    ├── config/
    │   └── OpenApiConfig.java       # Swagger/OpenAPI Configuration
    │
    ├── persistence/                 # Persistence Adapter
    │   ├── entity/
    │   │   ├── CompetitorEntity.java         # JPA Annotations
    │   │   └── ClinicalTrialEntity.java      # JPA Annotations
    │   ├── repository/
    │   │   └── JpaCompetitorRepository.java  # Spring Data JPA
    │   ├── adapter/
    │   │   └── CompetitorPersistenceAdapter.java  # Implements Output Port
    │   └── mapper/
    │       └── PersistenceMapper.java        # MapStruct: Entity <-> Domain
    │
    └── web/                         # Web Adapter
        ├── controller/
        │   └── CompetitorController.java     # REST Controller
        ├── dto/
        │   ├── CreateCompetitorRequest.java
        │   ├── CompetitorResponse.java
        │   └── AddTrialRequest.java
        ├── exception/
        │   ├── GlobalExceptionHandler.java   # @RestControllerAdvice
        │   ├── ErrorResponse.java            # Standard Error DTO
        │   ├── ResourceNotFoundException.java
        │   └── DuplicateResourceException.java
        └── mapper/
            └── WebMapper.java               # MapStruct: DTO <-> Domain
```

## Key Principles

### 1. **Domain Layer (Pure Java)**
- No Spring, JPA, or Jackson annotations
- Contains business logic (e.g., `Competitor.addClinicalTrial()`)
- Defines interfaces (Ports) that outer layers implement

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

### Competitor Operations

#### Create Competitor
```
POST /api/competitors
Content-Type: application/json

{
  "name": "Pfizer Inc.",
  "therapeuticAreas": ["Oncology", "Immunology", "Cardiology"],
  "headquarters": "New York, USA"
}
```

#### Get All Competitors
```
GET /api/competitors
```

#### Get Competitor by ID
```
GET /api/competitors/{id}
```

#### Add Clinical Trial to Competitor
```
POST /api/competitors/{id}/trials
Content-Type: application/json

{
  "trialId": "NCT12345678",
  "name": "Phase III Lung Cancer Study",
  "phase": "Phase III",
  "status": "Recruiting",
  "indication": "Non-Small Cell Lung Cancer"
}
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
- Application: http://localhost:8081
- **Swagger UI: http://localhost:8081/swagger-ui.html** 📚
- API Docs (JSON): http://localhost:8081/v3/api-docs
- H2 Console: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:competitordb`
  - Username: `sa`
  - Password: (empty)
- Health: http://localhost:8081/actuator/health

## Testing Examples

### Create a Competitor
```bash
curl -X POST http://localhost:8081/api/competitors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Novartis AG",
    "therapeuticAreas": ["Oncology", "Ophthalmology", "Neuroscience"],
    "headquarters": "Basel, Switzerland"
  }'
```

### Get All Competitors
```bash
curl http://localhost:8081/api/competitors
```

### Add Clinical Trial
```bash
curl -X POST http://localhost:8081/api/competitors/{id}/trials \
  -H "Content-Type: application/json" \
  -d '{
    "trialId": "NCT98765432",
    "name": "Melanoma Treatment Study",
    "phase": "Phase II",
    "status": "Active",
    "indication": "Melanoma"
  }'
```

## Error Handling

The service uses a `GlobalExceptionHandler` for consistent error responses:

### Error Response Format
```json
{
  "timestamp": "2026-02-08T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "{name=Name is required}"
}
```

### Handled Exceptions
| Exception | HTTP Status | Description |
|-----------|-------------|-------------|
| `MethodArgumentNotValidException` | 400 | Validation errors |
| `ResourceNotFoundException` | 404 | Resource not found |
| `DuplicateResourceException` | 409 | Duplicate resource |
| `Exception` | 500 | Internal server error |

## Design Decisions

1. **Domain Purity**: Domain models are pure Java POJOs with no framework dependencies
2. **Dependency Inversion**: Domain defines ports; infrastructure implements them
3. **MapStruct**: Clean, type-safe mapping between layers
4. **Bidirectional JPA Relations**: Handled in persistence layer only
5. **Transaction Management**: Applied at application service layer
6. **Global Exception Handling**: Consistent error responses with `@RestControllerAdvice`
7. **API Documentation**: SpringDoc OpenAPI 3 with comprehensive Swagger annotations
8. **Validation**: Bean validation on request DTOs (`@Valid`, `@NotBlank`, etc.)

## Future Enhancements

- Integration tests for full flow
- Docker support
- Metrics and monitoring
- Pagination for list endpoints
- Search and filtering capabilities
