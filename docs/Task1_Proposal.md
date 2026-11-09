# Proposal: Employee Management RESTful Web Service

**To:** Stella Yun, Senior Software Engineering Director
**From:** Khawaja Moinuddin Jamal, Software Engineer
**Subject:** Proposal for a RESTful web service to manage employees on HPE GreenLake

## 1. Objective
Build a lightweight RESTful web service that lets internal applications list and add employees through a clean JSON API. It will be written in Java Spring Boot and deployed on the HPE GreenLake cloud platform. The design starts small (GET and POST) and leaves room for update, delete, search and a persistent database.

## 2. Why Spring Boot and GreenLake
- **Spring Boot** auto-configures an embedded Tomcat server, Jackson JSON mapping and validation. Developers can focus on business logic rather than setup. It is the most widely used Java framework for REST services and has first-class testing support (JUnit 5, Mockito, MockMvc).
- **HPE GreenLake** gives a cloud operating experience with pay-per-use consumption, wherever the data lives (on-premise, edge or co-location). The service gets elastic capacity, central monitoring and governance through one console, while employee data can stay in HPE-managed infrastructure for security and compliance.

## 3. Data model
| Field | Type | Rules |
|---|---|---|
| `employee_id` | String | Required, unique |
| `first_name` | String | Required |
| `last_name` | String | Required |
| `email` | String | Required, valid email |
| `title` | String | Required |

`Employees` wraps the list so the JSON response is `{"Employees": [ ... ]}`.

## 4. Architecture
```
Client ──HTTP/JSON──▶ EmployeeController (@RestController)
                              │
                              ▼
                       EmployeeManager (@Repository, in-memory store; later a DB)
                              │
                              ▼
                    Employee / Employees (model)
```
- **Model:** the `Employee` and `Employees` classes.
- **Data access:** `EmployeeManager` holds the list behind a small interface (get all, exists, add). It can later be replaced by Spring Data JPA without touching the controller.
- **Controller:** `EmployeeController` maps HTTP requests to manager calls.

## 5. API endpoints
| Method | Path | Description | Responses |
|---|---|---|---|
| GET | `/employees` | Return all employees | 200 OK with JSON list |
| POST | `/employees` | Add a new employee | 201 Created + `Location` header; 400 invalid body; 409 duplicate id |

Planned next: `GET /employees/{id}`, `PUT /employees/{id}`, `DELETE /employees/{id}`.

## 6. Implementation plan
1. Generate the project with Spring Initializr (Maven, Java 17, Spring Web, Validation).
2. Create the model classes and the `EmployeeManager` with seed data.
3. Implement `GET /employees`, then `POST /employees` with validation and duplicate checks.
4. Write unit tests (JUnit 5 + Mockito) and web-layer tests (MockMvc).
5. Containerize the service and deploy it to GreenLake, with CI running the tests on every push.

## 7. Testing and quality
- Unit tests isolate the controller using Mockito mocks of `EmployeeManager`.
- MockMvc tests check URL mapping, JSON field names, status codes and validation errors.
- CI (GitHub Actions) runs `mvn test` on every push.

## 8. Risks and mitigations
- **In-memory data is lost on restart.** Move to a GreenLake-hosted database as the next increment.
- **Concurrent writes.** The manager methods are synchronized; a database will handle this through transactions.
- **Security.** Add Spring Security with OAuth2/JWT before exposing the service outside the internal network.
