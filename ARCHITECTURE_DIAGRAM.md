# Connected Insights Platform — Architecture Diagram

> **Version:** 1.0.0
> **Author:** Jorge Falcigno

---

## System Architecture Overview

The following diagram illustrates the high-level architecture of the Connected Insights platform, showing the microservices, their databases, and communication patterns.

```mermaid
graph TD
    %% ========================================
    %% Style Definitions
    %% ========================================
    classDef service fill:#E3F2FD,stroke:#1565C0,stroke-width:2px,color:#000000
    classDef db fill:#E8F5E9,stroke:#2E7D32,stroke-width:2px,color:#000000
    classDef external fill:#ECEFF1,stroke:#546E7A,stroke-width:2px,color:#000000
    classDef output fill:#FFF3E0,stroke:#EF6C00,stroke-width:2px,color:#000000

    %% ========================================
    %% External Actor
    %% ========================================
    SLT([👤 Senior Leadership Team]):::external

    %% ========================================
    %% Docker Compose Network
    %% ========================================
    subgraph DockerNetwork["🐳 Docker Compose Network"]
        
        %% --- Competitor Service ---
        subgraph CompetitorContext["Competitor Bounded Context"]
            CS{{"🏢 Competitor Service<br/>:8081"}}:::service
            CS_DB[("💾 H2 Database<br/>Competitors & Trials")]:::db
        end

        %% --- Insights Service ---
        subgraph InsightsContext["Insights Bounded Context"]
            IS{{"📊 Insights Service<br/>:8082"}}:::service
            IS_DB[("💾 H2 Database<br/>Insights & Scores")]:::db
        end

        %% --- Notification Service ---
        subgraph NotificationContext["Notification Bounded Context"]
            NS{{"🔔 Notification Service<br/>:8083"}}:::service
            NS_DB[("💾 H2 Database<br/>Subscriptions")]:::db
            LOGS["📋 Console Logs<br/>(Simulated Email)"]:::output
        end

    end

    %% ========================================
    %% User to Services Connections
    %% ========================================
    SLT -->|"HTTP / REST :8081"| CS
    SLT -->|"HTTP / REST :8082"| IS
    SLT -->|"HTTP / REST :8083"| NS

    %% ========================================
    %% Service to Database Connections
    %% ========================================
    CS -->|"JPA / JDBC"| CS_DB
    IS -->|"JPA / JDBC"| IS_DB
    NS -->|"JPA / JDBC"| NS_DB
```

---

## Diagram Legend

| Symbol | Meaning |
|--------|---------|
| 🔷 **Blue Hexagon** | Spring Boot Microservice |
| 🟢 **Green Cylinder** | H2 In-Memory Database |
| ⬜ **Gray Stadium** | External Actor (User) |
| 🟠 **Orange Rectangle** | Output / Logs |
| **Solid Arrow (→)** | Synchronous Communication |
| **Dashed Arrow (⇢)** | Optional / Future Integration |

---

## Port Mapping

| Service | Internal Port | External Port | Description |
|---------|---------------|---------------|-------------|
| Competitor Service | 8080 | **8081** | Manages Competitors & Clinical Trials |
| Insights Service | 8080 | **8082** | Handles Market Insights with Scoring |
| Notification Service | 8080 | **8083** | Manages Subscriptions & Alerts |

---

## Communication Patterns

### Current State (MVP)
- **Client → Services:** RESTful HTTP/JSON over exposed ports
- **Services → Databases:** JPA/JDBC (embedded H2)
- **Notification Output:** Console logging (simulated email)

### Future State
- **Event-Driven:** RabbitMQ/Kafka for async notifications
- **Service Mesh:** Istio for mTLS and observability
- **API Gateway:** Centralized authentication and routing

---

## References

- [Full Architecture Documentation](./ARCHITECTURE.md)
- [API Specifications](./API.md)
- [Project README](./README.md)
