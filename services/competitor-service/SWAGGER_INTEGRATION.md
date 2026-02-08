# Swagger UI Integration - Quick Reference

## What Was Added

### 1. Maven Dependency
Added to `pom.xml`:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 2. Configuration Files

#### application.yaml
```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operationsSorter: method
    tagsSorter: alpha
```

#### OpenApiConfig.java
Created configuration class at:
`src/main/java/com/astrazeneca/competitor_service/infrastructure/config/OpenApiConfig.java`

This configures:
- API title and description
- Version information
- Contact details
- License information
- Server URLs

### 3. Controller Annotations

Enhanced `CompetitorController.java` with:
- `@Tag` - Groups endpoints under "Competitor Management"
- `@Operation` - Describes each endpoint
- `@ApiResponses` - Documents response codes and schemas
- `@Parameter` - Documents path variables

### 4. DTO Schema Annotations

Added `@Schema` annotations to:
- `CreateCompetitorRequest.java`
- `AddTrialRequest.java`
- `CompetitorResponse.java`

These provide:
- Field descriptions
- Example values
- Required field indicators

## How to Use

### 1. Build the Project
```bash
cd C:\stuff\code\java\challenges\astra\connected-insights\services\competitor-service
mvnw clean install
```

### 2. Run the Application
```bash
mvnw spring-boot:run
```

### 3. Access Swagger UI
Open your browser and navigate to:
```
http://localhost:8081/swagger-ui.html
```

### 4. Try the APIs

In Swagger UI, you can:
- See all available endpoints
- View request/response schemas
- Try out each API with the "Try it out" button
- See example values for all fields
- Get detailed error responses

## Available Endpoints in Swagger

### Competitor Management
1. **POST /api/competitors** - Create a new competitor
2. **GET /api/competitors** - Get all competitors
3. **GET /api/competitors/{id}** - Get competitor by ID
4. **POST /api/competitors/{id}/trials** - Add clinical trial to competitor

## Example Usage via Swagger UI

### Create a Competitor
1. Click on `POST /api/competitors`
2. Click "Try it out"
3. Use this example JSON:
```json
{
  "name": "Novartis AG",
  "therapeuticAreas": ["Oncology", "Ophthalmology", "Neuroscience"],
  "headquarters": "Basel, Switzerland"
}
```
4. Click "Execute"
5. See the response with the created competitor (including ID)

### Add a Clinical Trial
1. Note the competitor ID from the previous response (e.g., 1)
2. Click on `POST /api/competitors/{id}/trials`
3. Click "Try it out"
4. Enter the competitor ID
5. Use this example JSON:
```json
{
  "trialId": "NCT98765432",
  "name": "Melanoma Treatment Study",
  "phase": "Phase II",
  "status": "Active",
  "indication": "Melanoma"
}
```
6. Click "Execute"

## OpenAPI JSON Specification

Access the raw OpenAPI specification at:
```
http://localhost:8081/api-docs
```

This can be imported into:
- Postman
- Insomnia
- API testing tools
- Code generators

## Troubleshooting

### Swagger UI Not Loading
- Ensure the application is running on port 8081
- Check that you're accessing the correct URL: `/swagger-ui.html`
- Verify no errors in the console logs

### Dependencies Not Resolved
If you see import errors in your IDE:
1. Reload Maven project (Right-click pom.xml → Maven → Reload Project)
2. Run `mvnw clean install` to download dependencies
3. Restart your IDE

### Changes Not Reflected
- Restart the Spring Boot application
- Clear browser cache
- Check that annotations are correctly placed

## Benefits of Swagger Integration

✅ **Interactive Documentation** - Try APIs without external tools
✅ **Auto-generated** - Documentation stays in sync with code
✅ **Schema Validation** - See request/response structures
✅ **Team Collaboration** - Share API contracts easily
✅ **Client Generation** - Generate client SDKs from OpenAPI spec
✅ **Testing** - Quick API testing during development

