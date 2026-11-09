# HPE Software Engineering Job Simulation (Forage)

My solutions to the [Hewlett Packard Enterprise Software Engineering job simulation](https://www.theforage.com/simulations/hewlett-packard-enterprise/software-engineering-pcij): a RESTful employee-management web service built with **Java 17 and Spring Boot 3**.

| Task | What I built |
|---|---|
| 1. Proposal for a RESTful web service | [`docs/Task1_Proposal.md`](docs/Task1_Proposal.md) |
| 2. Build the web service | `Employee`, `Employees`, `EmployeeManager` and the `GET /employees` endpoint |
| 3. Add support for uploading data | `POST /employees`, with validation, `201 Created` + `Location`, `409` on duplicate id |
| 4. Unit tests | JUnit 5 + Mockito unit tests, MockMvc web-layer tests, GitHub Actions CI |

## API
| Method | Path | Success | Errors |
|---|---|---|---|
| GET | `/employees` | `200` with `{"Employees": [...]}` | none |
| POST | `/employees` | `201` + `Location: /employees/{id}` | `400` invalid body, `409` duplicate `employee_id` |

```bash
curl http://localhost:8080/employees

curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{"employee_id":"5","first_name":"Ali","last_name":"Khan","email":"ali.khan@example.com","title":"QA Engineer"}'
```

## Project structure
```
src/main/java/com/hpe/employees/
├── EmployeesApplication.java   # Spring Boot entry point
├── Employee.java               # Model (snake_case JSON, bean validation)
├── Employees.java              # {"Employees": [...]} wrapper
├── EmployeeManager.java        # In-memory store with seed data
└── EmployeeController.java     # GET and POST /employees
src/test/java/com/hpe/employees/
├── EmployeeManagerTest.java        # Store behaviour
├── EmployeeControllerTest.java     # Controller with a Mockito mock
└── EmployeeControllerWebTest.java  # MockMvc: routing, JSON, validation, status codes
```

## Run it
```bash
mvn spring-boot:run   # http://localhost:8080/employees
mvn test
```
