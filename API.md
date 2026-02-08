# Connected Insights Platform — API Documentation

> **Version:** 1.0.0  
> **Last Updated:** February 2026  
> **Status:** MVP  
> **Author:** Enterprise AI Team, AstraZeneca

---

## Table of Contents

1. [Overview](#overview)
2. [Common Information](#common-information)
3. [Competitor Service API](#competitor-service-api)
   - [Health Check](#1-health-check)
   - [Create Competitor](#2-create-competitor)
   - [List All Competitors](#3-list-all-competitors)
   - [Get Competitor by ID](#4-get-competitor-by-id)
   - [Add Clinical Trial to Competitor](#5-add-clinical-trial-to-competitor)
4. [Insights Service API](#insights-service-api)
   - [Health Check](#1-health-check-1)
   - [Create Insight](#2-create-insight)
   - [List Insights](#3-list-insights)
   - [Get Insight by ID](#4-get-insight-by-id)
   - [Delete Insight](#5-delete-insight)
5. [Notification Service API](#notification-service-api)
   - [Health Check](#1-health-check-2)
   - [Create Subscription](#2-create-subscription)
   - [List Subscriptions](#3-list-subscriptions)
   - [Send Notification](#4-send-notification)
   - [List Notification History](#5-list-notification-history)
6. [Quick Test Commands](#quick-test-commands)

---

## Overview

This document defines the REST API contracts for the **Connected Insights** platform, a microservices-based system designed for AstraZeneca's Enterprise AI team. The platform aggregates competitive intelligence data and provides notifications to leadership stakeholders.

### Service Endpoints

| Service | Base URL | Description |
|---------|----------|-------------|
| **Competitor Service** | `http://localhost:8081` | Manages competitors and clinical trials |
| **Insights Service** | `http://localhost:8082` | Handles market insights with relevance scoring |
| **Notification Service** | `http://localhost:8083` | Manages subscriptions and alert delivery |

---

## Common Information

### Content Type

All endpoints accept and return JSON:

```
Content-Type: application/json
Accept: application/json
```

### Standard HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Request succeeded |
| `201 Created` | Resource successfully created |
| `204 No Content` | Request succeeded with no response body |
| `400 Bad Request` | Invalid request payload |
| `404 Not Found` | Resource not found |
| `500 Internal Server Error` | Server-side error |

### Error Response Format

```json
{
  "timestamp": "2026-02-08T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Competitor not found with id: comp-999",
  "path": "/api/competitors/comp-999"
}
```

---

## Competitor Service API

**Base URL:** `http://localhost:8081`

The Competitor Service manages competitor profiles and their associated clinical trials.

---

### 1. Health Check

Verify the service is running and healthy.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/actuator/health` |
| **Authentication** | None |

#### Response

**Status:** `200 OK`

```json
{
  "status": "UP"
}
```

---

### 2. Create Competitor

Create a new competitor profile in the system.

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/competitors` |
| **Authentication** | None |

#### Request Body

```json
{
  "id": "comp-001",
  "name": "CompanyX Pharma",
  "therapeuticAreas": ["Oncology", "Cardiovascular"],
  "headquarters": "Basel, Switzerland",
  "activeDrugs": 12,
  "pipelineDrugs": 45
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | String | Yes | Unique identifier for the competitor |
| `name` | String | Yes | Company name |
| `therapeuticAreas` | Array[String] | Yes | List of therapeutic focus areas |
| `headquarters` | String | No | Location of company headquarters |
| `activeDrugs` | Integer | No | Number of drugs currently on market |
| `pipelineDrugs` | Integer | No | Number of drugs in development pipeline |

#### Response

**Status:** `201 Created`

```json
{
  "id": "comp-001",
  "name": "CompanyX Pharma",
  "therapeuticAreas": ["Oncology", "Cardiovascular"],
  "headquarters": "Basel, Switzerland",
  "activeDrugs": 12,
  "pipelineDrugs": 45,
  "createdAt": "2026-02-08T10:30:00Z",
  "updatedAt": "2026-02-08T10:30:00Z"
}
```

#### Error Responses

| Status | Description |
|--------|-------------|
| `400 Bad Request` | Invalid request body or missing required fields |
| `409 Conflict` | Competitor with the same ID already exists |

---

### 3. List All Competitors

Retrieve a list of all competitor profiles.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/competitors` |
| **Authentication** | None |

#### Response

**Status:** `200 OK`

```json
[
  {
    "id": "comp-001",
    "name": "CompanyX Pharma",
    "therapeuticAreas": ["Oncology", "Cardiovascular"],
    "headquarters": "Basel, Switzerland",
    "activeDrugs": 12,
    "pipelineDrugs": 45,
    "createdAt": "2026-02-08T10:30:00Z",
    "updatedAt": "2026-02-08T10:30:00Z"
  },
  {
    "id": "comp-002",
    "name": "BioGen Labs",
    "therapeuticAreas": ["Neurology", "Immunology"],
    "headquarters": "Cambridge, USA",
    "activeDrugs": 8,
    "pipelineDrugs": 32,
    "createdAt": "2026-02-07T14:20:00Z",
    "updatedAt": "2026-02-07T14:20:00Z"
  }
]
```

---

### 4. Get Competitor by ID

Retrieve details of a specific competitor by their unique identifier.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/competitors/{id}` |
| **Authentication** | None |

#### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | Unique competitor identifier |

#### Response

**Status:** `200 OK`

```json
{
  "id": "comp-001",
  "name": "CompanyX Pharma",
  "therapeuticAreas": ["Oncology", "Cardiovascular"],
  "headquarters": "Basel, Switzerland",
  "activeDrugs": 12,
  "pipelineDrugs": 45,
  "createdAt": "2026-02-08T10:30:00Z",
  "updatedAt": "2026-02-08T10:30:00Z"
}
```

#### Error Responses

| Status | Description |
|--------|-------------|
| `404 Not Found` | Competitor with the specified ID does not exist |

---

### 5. Add Clinical Trial to Competitor

Associate a new clinical trial with an existing competitor.

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/competitors/{id}/trials` |
| **Authentication** | None |

#### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | Unique competitor identifier |

#### Request Body

```json
{
  "trialId": "NCT12345678",
  "drugName": "DrugX-200",
  "phase": "Phase 3",
  "indication": "Non-small cell lung cancer",
  "status": "Active, recruiting",
  "startDate": "2024-01-15",
  "estimatedCompletion": "2026-12-31",
  "enrollmentTarget": 450
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `trialId` | String | Yes | ClinicalTrials.gov identifier (NCT number) |
| `drugName` | String | Yes | Name of the drug being tested |
| `phase` | String | Yes | Clinical trial phase (Phase 1, 2, 3, 4) |
| `indication` | String | Yes | Medical condition being treated |
| `status` | String | Yes | Current trial status |
| `startDate` | String (Date) | Yes | Trial start date (YYYY-MM-DD) |
| `estimatedCompletion` | String (Date) | No | Estimated completion date |
| `enrollmentTarget` | Integer | No | Target number of participants |

#### Response

**Status:** `201 Created`

```json
{
  "trialId": "NCT12345678",
  "competitorId": "comp-001",
  "drugName": "DrugX-200",
  "phase": "Phase 3",
  "indication": "Non-small cell lung cancer",
  "status": "Active, recruiting",
  "startDate": "2024-01-15",
  "estimatedCompletion": "2026-12-31",
  "enrollmentTarget": 450,
  "createdAt": "2026-02-08T10:35:00Z"
}
```

#### Error Responses

| Status | Description |
|--------|-------------|
| `400 Bad Request` | Invalid request body or missing required fields |
| `404 Not Found` | Competitor with the specified ID does not exist |

---

## Insights Service API

**Base URL:** `http://localhost:8082`

The Insights Service manages market insights with automated relevance scoring.

---

### 1. Health Check

Verify the service is running and healthy.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/actuator/health` |
| **Authentication** | None |

#### Response

**Status:** `200 OK`

```json
{
  "status": "UP"
}
```

---

### 2. Create Insight

Create a new market insight in the system.

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/insights` |
| **Authentication** | None |

#### Request Body

```json
{
  "title": "CompanyX advances to Phase 3 in NSCLC",
  "description": "CompanyX Pharma initiated Phase 3 trial for DrugX-200, targeting non-small cell lung cancer patients who have failed first-line therapy.",
  "category": "Clinical Trial Update",
  "therapeuticArea": "Oncology",
  "competitorId": "comp-001",
  "relevanceScore": 8.5,
  "source": "ClinicalTrials.gov",
  "publishedDate": "2024-01-15",
  "impactLevel": "HIGH"
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `title` | String | Yes | Brief title of the insight |
| `description` | String | Yes | Detailed description of the insight |
| `category` | String | Yes | Category (e.g., "Clinical Trial Update", "Regulatory", "Market") |
| `therapeuticArea` | String | Yes | Therapeutic area (e.g., "Oncology", "Cardiovascular") |
| `competitorId` | String | No | Reference to associated competitor |
| `relevanceScore` | Decimal | No | Relevance score (0.0 - 10.0) |
| `source` | String | Yes | Source of the information |
| `publishedDate` | String (Date) | Yes | Date the insight was published |
| `impactLevel` | String (Enum) | Yes | Impact level: `HIGH`, `MEDIUM`, or `LOW` |

#### Response

**Status:** `201 Created`

```json
{
  "id": "ins-001",
  "title": "CompanyX advances to Phase 3 in NSCLC",
  "description": "CompanyX Pharma initiated Phase 3 trial for DrugX-200, targeting non-small cell lung cancer patients who have failed first-line therapy.",
  "category": "Clinical Trial Update",
  "therapeuticArea": "Oncology",
  "competitorId": "comp-001",
  "relevanceScore": 9,
  "source": "ClinicalTrials.gov",
  "publishedDate": "2024-01-15",
  "impactLevel": "HIGH",
  "createdAt": "2026-02-08T10:40:00Z"
}
```

> **Note:** The `relevanceScore` is automatically calculated based on the `impactLevel` if not provided (HIGH=9, MEDIUM=6, LOW=3).

#### Error Responses

| Status | Description |
|--------|-------------|
| `400 Bad Request` | Invalid request body or missing required fields |

---

### 3. List Insights

Retrieve a list of all market insights with optional filtering.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/insights` |
| **Authentication** | None |

#### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `therapeuticArea` | String | No | Filter by therapeutic area |
| `impactLevel` | String | No | Filter by impact level (HIGH, MEDIUM, LOW) |
| `competitorId` | String | No | Filter by associated competitor |

#### Example Request

```
GET /api/insights?therapeuticArea=Oncology&impactLevel=HIGH
```

#### Response

**Status:** `200 OK`

```json
[
  {
    "id": "ins-001",
    "title": "CompanyX advances to Phase 3 in NSCLC",
    "description": "CompanyX Pharma initiated Phase 3 trial for DrugX-200...",
    "category": "Clinical Trial Update",
    "therapeuticArea": "Oncology",
    "competitorId": "comp-001",
    "relevanceScore": 9,
    "source": "ClinicalTrials.gov",
    "publishedDate": "2024-01-15",
    "impactLevel": "HIGH",
    "createdAt": "2026-02-08T10:40:00Z"
  },
  {
    "id": "ins-002",
    "title": "BioGen receives FDA breakthrough designation",
    "description": "BioGen Labs received FDA breakthrough therapy designation...",
    "category": "Regulatory",
    "therapeuticArea": "Oncology",
    "competitorId": "comp-002",
    "relevanceScore": 9,
    "source": "FDA Press Release",
    "publishedDate": "2024-02-01",
    "impactLevel": "HIGH",
    "createdAt": "2026-02-08T11:00:00Z"
  }
]
```

---

### 4. Get Insight by ID

Retrieve details of a specific insight by its unique identifier.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/insights/{id}` |
| **Authentication** | None |

#### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | Unique insight identifier |

#### Response

**Status:** `200 OK`

```json
{
  "id": "ins-001",
  "title": "CompanyX advances to Phase 3 in NSCLC",
  "description": "CompanyX Pharma initiated Phase 3 trial for DrugX-200, targeting non-small cell lung cancer patients who have failed first-line therapy.",
  "category": "Clinical Trial Update",
  "therapeuticArea": "Oncology",
  "competitorId": "comp-001",
  "relevanceScore": 9,
  "source": "ClinicalTrials.gov",
  "publishedDate": "2024-01-15",
  "impactLevel": "HIGH",
  "createdAt": "2026-02-08T10:40:00Z"
}
```

#### Error Responses

| Status | Description |
|--------|-------------|
| `404 Not Found` | Insight with the specified ID does not exist |

---

### 5. Delete Insight

Remove an insight from the system.

| | |
|---|---|
| **Method** | `DELETE` |
| **URL** | `/api/insights/{id}` |
| **Authentication** | None |

#### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | String | Unique insight identifier |

#### Response

**Status:** `204 No Content`

*(No response body)*

#### Error Responses

| Status | Description |
|--------|-------------|
| `404 Not Found` | Insight with the specified ID does not exist |

---

## Notification Service API

**Base URL:** `http://localhost:8083`

The Notification Service manages user subscriptions and delivers alerts for high-priority insights.

---

### 1. Health Check

Verify the service is running and healthy.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/actuator/health` |
| **Authentication** | None |

#### Response

**Status:** `200 OK`

```json
{
  "status": "UP"
}
```

---

### 2. Create Subscription

Subscribe a user to receive notifications for specific therapeutic areas.

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/subscriptions` |
| **Authentication** | None |

#### Request Body

```json
{
  "userId": "user-123",
  "email": "dr.smith@astrazeneca.com",
  "therapeuticAreas": ["Oncology", "Respiratory"],
  "notifyOnHighImpact": true
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `userId` | String | Yes | Unique user identifier |
| `email` | String | Yes | User's email address for notifications |
| `therapeuticAreas` | Array[String] | Yes | List of therapeutic areas to subscribe to |
| `notifyOnHighImpact` | Boolean | No | Only notify for HIGH impact insights (default: false) |

#### Response

**Status:** `201 Created`

```json
{
  "id": "sub-001",
  "userId": "user-123",
  "email": "dr.smith@astrazeneca.com",
  "therapeuticAreas": ["Oncology", "Respiratory"],
  "notifyOnHighImpact": true,
  "active": true,
  "createdAt": "2026-02-08T10:45:00Z"
}
```

#### Error Responses

| Status | Description |
|--------|-------------|
| `400 Bad Request` | Invalid request body or missing required fields |
| `409 Conflict` | User already has an active subscription |

---

### 3. List Subscriptions

Retrieve a list of all active subscriptions.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/subscriptions` |
| **Authentication** | None |

#### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `userId` | String | No | Filter by user ID |
| `therapeuticArea` | String | No | Filter by therapeutic area |

#### Response

**Status:** `200 OK`

```json
[
  {
    "id": "sub-001",
    "userId": "user-123",
    "email": "dr.smith@astrazeneca.com",
    "therapeuticAreas": ["Oncology", "Respiratory"],
    "notifyOnHighImpact": true,
    "active": true,
    "createdAt": "2026-02-08T10:45:00Z"
  },
  {
    "id": "sub-002",
    "userId": "user-456",
    "email": "dr.jones@astrazeneca.com",
    "therapeuticAreas": ["Cardiovascular"],
    "notifyOnHighImpact": false,
    "active": true,
    "createdAt": "2026-02-07T09:30:00Z"
  }
]
```

---

### 4. Send Notification

Manually trigger a notification (logs to console for MVP).

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/api/notifications/send` |
| **Authentication** | None |

#### Request Body

```json
{
  "recipient": "dr.smith@astrazeneca.com",
  "subject": "New Insight: CompanyX Phase 3",
  "message": "A new high-impact insight was added regarding Oncology."
}
```

#### Request Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `recipient` | String | Yes | Email address of the recipient |
| `subject` | String | Yes | Notification subject line |
| `message` | String | Yes | Notification message body |

#### Response

**Status:** `200 OK`

```json
{
  "id": "notif-001",
  "recipient": "dr.smith@astrazeneca.com",
  "subject": "New Insight: CompanyX Phase 3",
  "message": "A new high-impact insight was added regarding Oncology.",
  "status": "SENT",
  "sentAt": "2026-02-08T10:50:00Z"
}
```

> **Note (MVP):** Notifications are currently logged to the console. Email delivery will be implemented in a future release.

#### Error Responses

| Status | Description |
|--------|-------------|
| `400 Bad Request` | Invalid request body or missing required fields |

---

### 5. List Notification History

Retrieve the history of all sent notifications.

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/api/notifications` |
| **Authentication** | None |

#### Query Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `recipient` | String | No | Filter by recipient email |
| `since` | String (DateTime) | No | Filter notifications sent after this date |

#### Response

**Status:** `200 OK`

```json
[
  {
    "id": "notif-001",
    "recipient": "dr.smith@astrazeneca.com",
    "subject": "New Insight: CompanyX Phase 3",
    "message": "A new high-impact insight was added regarding Oncology.",
    "status": "SENT",
    "sentAt": "2026-02-08T10:50:00Z"
  },
  {
    "id": "notif-002",
    "recipient": "dr.jones@astrazeneca.com",
    "subject": "Weekly Cardiovascular Update",
    "message": "3 new insights were added to your subscribed areas.",
    "status": "SENT",
    "sentAt": "2026-02-07T08:00:00Z"
  }
]
```

---

## Quick Test Commands

Use these `curl` commands to quickly test the "Happy Path" workflow.

### Prerequisites

Ensure all services are running:

```bash
docker-compose up --build
```

---

### Step 1: Check Health of All Services

```bash
# Competitor Service
curl -X GET http://localhost:8081/actuator/health

# Insights Service
curl -X GET http://localhost:8082/actuator/health

# Notification Service
curl -X GET http://localhost:8083/actuator/health
```

**Expected Response (all services):**

```json
{"status":"UP"}
```

---

### Step 2: Create a Competitor

```bash
curl -X POST http://localhost:8081/api/competitors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "comp-001",
    "name": "CompanyX Pharma",
    "therapeuticAreas": ["Oncology", "Cardiovascular"],
    "headquarters": "Basel, Switzerland",
    "activeDrugs": 12,
    "pipelineDrugs": 45
  }'
```

**Expected Response:** `201 Created`

---

### Step 3: Add a Clinical Trial to Competitor

```bash
curl -X POST http://localhost:8081/api/competitors/comp-001/trials \
  -H "Content-Type: application/json" \
  -d '{
    "trialId": "NCT12345678",
    "drugName": "DrugX-200",
    "phase": "Phase 3",
    "indication": "Non-small cell lung cancer",
    "status": "Active, recruiting",
    "startDate": "2024-01-15",
    "estimatedCompletion": "2026-12-31",
    "enrollmentTarget": 450
  }'
```

**Expected Response:** `201 Created`

---

### Step 4: Create an Insight Linked to Competitor

```bash
curl -X POST http://localhost:8082/api/insights \
  -H "Content-Type: application/json" \
  -d '{
    "title": "CompanyX advances to Phase 3 in NSCLC",
    "description": "CompanyX Pharma initiated Phase 3 trial for DrugX-200.",
    "category": "Clinical Trial Update",
    "therapeuticArea": "Oncology",
    "competitorId": "comp-001",
    "source": "ClinicalTrials.gov",
    "publishedDate": "2024-01-15",
    "impactLevel": "HIGH"
  }'
```

**Expected Response:** `201 Created`

---

### Step 5: Create a Subscription

```bash
curl -X POST http://localhost:8083/api/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "email": "dr.smith@astrazeneca.com",
    "therapeuticAreas": ["Oncology", "Respiratory"],
    "notifyOnHighImpact": true
  }'
```

**Expected Response:** `201 Created`

---

### Step 6: Send a Test Notification

```bash
curl -X POST http://localhost:8083/api/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "dr.smith@astrazeneca.com",
    "subject": "New Insight: CompanyX Phase 3",
    "message": "A new high-impact insight was added regarding Oncology."
  }'
```

**Expected Response:** `200 OK`

---

### Step 7: Verify Data

```bash
# List all competitors
curl -X GET http://localhost:8081/api/competitors

# List all insights (filtered by Oncology)
curl -X GET "http://localhost:8082/api/insights?therapeuticArea=Oncology"

# List all subscriptions
curl -X GET http://localhost:8083/api/subscriptions

# List notification history
curl -X GET http://localhost:8083/api/notifications
```

---

## References

- [Architecture Documentation](./ARCHITECTURE.md)
- [Project README](./README.md)
- [Docker Compose Configuration](./docker-compose.yml)


