# Notification Service - Hexagonal Architecture

## Overview
This is a Spring Boot 3.4 microservice built with **Hexagonal Architecture (Ports and Adapters)** pattern for managing subscriptions and sending notifications.

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
notification-service/
├── domain/                          # DOMAIN LAYER (Core Business Logic)
│   ├── model/
│   │   ├── Subscription.java       # Pure Java - No framework dependencies
│   │   ├── NotificationLog.java    # Pure Java - No framework dependencies
│   │   └── NotificationStatus.java # Enum (SENT)
│   └── ports/
│       ├── in/
│       │   ├── ManageSubscriptionUseCase.java  # Input Port (Interface)
│       │   └── SendNotificationUseCase.java    # Input Port (Interface)
│       └── out/
│           ├── SubscriptionRepositoryPort.java     # Output Port (Interface)
│           ├── NotificationLogRepositoryPort.java  # Output Port (Interface)
│           └── NotificationSenderPort.java         # Output Port (Interface)
│
├── application/                     # APPLICATION LAYER (Use Cases)
│   └── service/
│       └── NotificationService.java  # Implements Input Ports
│
└── infrastructure/                  # INFRASTRUCTURE LAYER (Adapters)
    ├── config/
    │   └── OpenApiConfig.java       # Swagger/OpenAPI Configuration
    │
    ├── persistence/                 # Persistence Adapter
    │   ├── entity/
    │   │   ├── SubscriptionEntity.java       # JPA Annotations
    │   │   ├── NotificationLogEntity.java    # JPA Annotations
    │   │   └── NotificationStatusEntity.java # JPA Enum
    │   ├── repository/
    │   │   ├── JpaSubscriptionRepository.java     # Spring Data JPA
    │   │   └── JpaNotificationLogRepository.java  # Spring Data JPA
    │   ├── adapter/
    │   │   ├── SubscriptionPersistenceAdapter.java     # Implements Output Port
    │   │   └── NotificationLogPersistenceAdapter.java  # Implements Output Port
    │   └── mapper/
    │       └── PersistenceMapper.java        # MapStruct: Entity <-> Domain
    │
    ├── notification/                # Notification Adapter
    │   └── adapter/
    │       └── ConsoleNotificationAdapter.java  # Implements NotificationSenderPort
    │
    └── web/                         # Web Adapter
        ├── controller/
        │   └── NotificationController.java   # REST Controller
        ├── dto/
        │   ├── CreateSubscriptionRequest.java
        │   ├── SendNotificationRequest.java
        │   ├── SubscriptionResponse.java
        │   └── NotificationLogResponse.java
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
- Contains business logic (e.g., `Subscription.create()`, `NotificationLog.create()`)
- Defines interfaces (Ports) that outer layers implement

### 2. **Application Layer**
- Implements Use Cases (Input Ports)
- Orchestrates domain logic
- Calls Output Ports for persistence and notification sending

### 3. **Infrastructure Layer**
- **Persistence Adapter**: Implements domain repository interfaces, uses JPA
- **Notification Adapter**: Implements notification sender interface, logs to console
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

### Subscription Operations

#### Create Subscription
```
POST /api/subscriptions
Content-Type: application/json

{
  "userEmail": "user@example.com",
  "notificationPreferences": ["Oncology", "Cardiology", "Immunology"]
}
```

#### Get All Subscriptions
```
GET /api/subscriptions
```

### Notification Operations

#### Send Notification
```
POST /api/notifications/send
Content-Type: application/json

{
  "recipientEmail": "user@example.com",
  "messageContent": "New insight available in Oncology therapeutic area"
}
```

#### Get Notification History
```
GET /api/notifications
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
- Application: http://localhost:8083
- **Swagger UI: http://localhost:8083/swagger-ui.html** 📚
- API Docs (JSON): http://localhost:8083/v3/api-docs
- H2 Console: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:notificationdb`
  - Username: `sa`
  - Password: (empty)
- Health: http://localhost:8081/actuator/health

## Testing Examples

### Create a Subscription
```bash
curl -X POST http://localhost:8083/api/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "userEmail": "analyst@pharma.com",
    "notificationPreferences": ["Oncology", "Neuroscience", "Rare Diseases"]
  }'
```

### Get All Subscriptions
```bash
curl http://localhost:8083/api/subscriptions
```

### Send a Notification
```bash
curl -X POST http://localhost:8083/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipientEmail": "analyst@pharma.com",
    "messageContent": "New competitor insight: Pfizer announces Phase III trial results"
  }'
```

### Get Notification History
```bash
curl http://localhost:8083/api/notifications
```

## Error Handling

The service uses a `GlobalExceptionHandler` for consistent error responses:

### Error Response Format
```json
{
  "timestamp": "2026-02-08T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "{userEmail=Invalid email format}"
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
3. **Separate Persistence Adapters**: Split adapters to avoid method signature clashes
4. **Console Notification Adapter**: Mocks email sending by logging to console
5. **MapStruct**: Clean, type-safe mapping between layers
6. **Transaction Management**: Applied at application service layer
7. **Global Exception Handling**: Consistent error responses with `@RestControllerAdvice`
8. **API Documentation**: SpringDoc OpenAPI 3 with comprehensive Swagger annotations
9. **Validation**: Bean validation on request DTOs (`@Valid`, `@NotBlank`, `@Email`)

## Models

### Subscription
| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| userEmail | String | User's email address |
| notificationPreferences | List<String> | Topics/therapeutic areas of interest |

### NotificationLog
| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier |
| recipientEmail | String | Recipient's email address |
| messageContent | String | Notification message |
| timestamp | LocalDateTime | When the notification was sent |
| status | NotificationStatus | SENT |

## Future Enhancements

- Email integration (replace console adapter with SMTP adapter)
- Kafka integration for async notifications
- Subscription management (update/delete)
- Notification templates
- Rate limiting
- Integration tests for full flow
- Docker support
- Metrics and monitoring

