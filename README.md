# Programming 5 - Formula 1 Management System

## Student Information
- **Course:** Programming 5
- **Name:** Daniel Nedyalkov
- **Email:** daniel.nedyalkov@student.kdg.be
- **Student ID:** 0172599-36
- **Academic Year:** 2025-2026
- **Group:** ACS201

---

## Domain Entities

This is a **Formula 1 Management System** with the following entities:

### 1. Driver
- **Properties:** id, name, dateOfBirth, nationality, worldChampionships, imageUrl
- **Relationships:** ManyToOne with Team, ManyToMany with Race

### 2. Team
- **Properties:** id, name, foundedYear, league (enum), teamLogoUrl, budgetInMillions
- **Relationships:** OneToMany with Driver

### 3. Race
- **Properties:** id, name, date, hasEnded
- **Relationships:** ManyToOne with Track, ManyToOne with Driver (winner), ManyToMany with Driver

### 4. Track (Base class with inheritance)
- **Properties:** id, name, location, lengthKm, openedYear
- **Inheritance Strategy:** Single Table
- **Subtypes:**
  - **PermanentCircuit:** hasMuseum, testDaysPerYear, facilities
  - **StreetCircuit:** cityName, daysToSetup, annualRentalCost, hasTemporaryBarriers

### 5. League (Enum)
- **Values:** Formula_1, Formula_2, Formula_3, Formula_4, Formula_E

### Entity Relationships Diagram
```
Team (1) ----< (many) Driver
Driver (many) >----< (many) Race
Track (1) ----< (many) Race
Race (many) >---- (1) Driver (as winner)

Track (Single Table Inheritance)
  ├── PermanentCircuit
  └── StreetCircuit
```

---

## Build and Run Instructions

### Prerequisites
- Java 21 or higher
- Docker and Docker Compose
- Git

### Setup Steps

1. **Clone the repository:**
   ```bash
   git clone https://gitlab.com/kdg-ti/programming-5/projects-25-26/acs201/daniel.nedyalkov/Client.git
   cd Client
   ```

2. **Start the development PostgreSQL database using Docker:**
   ```bash
   docker compose up -d
   ```
   This starts the `f1Management` PostgreSQL database on host port `5050`.
   The default Spring configuration loads `src/main/resources/sql/data.sql`.

3. **Build the application:**
   ```bash
   ./gradlew build
   ```

4. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

5. **Access the application:**
   - Open your browser: `http://localhost:8080`

### Spring Profiles and Testing

- The default profile uses `src/main/resources/application.properties` and loads the normal seed data from `classpath:sql/data.sql`.
- Tests use the `test` profile through `@ActiveProfiles("test")`.
- The `test` profile is configured in `src/test/resources/application-test.properties`.
- Test data seeding is disabled with `spring.sql.init.mode=never`, so tests arrange their own data and do not depend on development seed data.
- Tests use a separate PostgreSQL database, `f1Management-test`, on host port `5435`.

Start the test database:
```bash
docker compose -f docker-compose.test.yml up -d
```

Run the test suite:
```bash
./gradlew test
```

The tests are configured with `@ActiveProfiles("test")`, so the single command above runs the full test suite against the test-specific PostgreSQL database.

Presentation-layer integration test classes:

- MVC integration tests: `DriverControllerTest`, `TeamControllerTest`
- API integration tests: `ApiDriverControllerTest`
- Role verification tests: `ApiDriverControllerTest`

Code coverage screenshot from IntelliJ:

![Code coverage report](coverageReport.png)

Unit tests with mocking:

- Mocking tests: `ApiDriverControllerUnitTest`, `DriverServiceUnitTest`
- `verify` tests: `ApiDriverControllerUnitTest`, `DriverServiceUnitTest`

Continuous integration:

- The GitLab CI pipeline is configured in `.gitlab-ci.yml`.
- The pipeline runs automatically when changes are pushed to GitLab.
- The pipeline has two stages: `build` and `test`.
- The `build` job runs `./gradlew --build-cache assemble`.
- The `test` job runs `./gradlew test`.
- The `test` job starts a PostgreSQL service and sets `CI_DB_HOST_PORT=postgres:5432`, so Spring tests connect to the CI database service.
- Locally, the same tests still use the Docker Compose test database through the fallback `localhost:5435` value in `application-test.properties`.
- The test job publishes a JUnit report from `build/test-results/test/**/TEST-*.xml`.

GitLab test report:

Stop the test database:
```bash
docker compose -f docker-compose.test.yml down
```

### Stopping the Application

- **Stop Spring Boot:** Press `Ctrl+C` in the terminal
- **Stop PostgreSQL:**
  ```bash
  docker compose down
  ```

### Useful Commands

```bash
# Clean and rebuild
./gradlew clean build

# Start the development database
docker compose up -d

# Start the test database
docker compose -f docker-compose.test.yml up -d

# Run tests with the test profile
./gradlew test

# Check Docker containers status
docker compose ps

# View database logs
docker compose logs postgres_boardgames_db

# Access PostgreSQL CLI
docker compose exec postgres_boardgames_db psql -U spring -d f1Management
```

---

## Technology Stack

- **Framework:** Spring Boot 3.5.6
- **Java:** 21
- **Database:** PostgreSQL (Docker)
- **ORM:** Spring Data JPA / Hibernate
- **Template Engine:** Thymeleaf
- **CSS Framework:** Bootstrap 5.3.3
- **Build Tool:** Gradle (Kotlin DSL)

---

## Project Structure

```
src/main/java/application/
├── domain/              # Entity classes (Driver, Team, Race, Track, etc.)
├── repository/JPA/      # Spring Data JPA Repository interfaces
├── service/             # Service layer with business logic
├── controller/          # Spring MVC controllers
└── viewmodel/           # View model classes for data transfer

src/main/resources/
├── templates/           # Thymeleaf HTML templates
├── static/              # Static assets (CSS, JS, images)
├── sql/data.sql         # Development seed data
└── application.properties

src/test/resources/
└── application-test.properties # Test profile datasource configuration

docker-compose.yml       # Development PostgreSQL database configuration
docker-compose.test.yml  # Test PostgreSQL database configuration
```

---

## Week 2

This section contains HTTP requests and responses for the REST API endpoints implemented for drivers.

### Fetching all drivers - OK (200)

**Request:**
```
GET http://localhost:8080/api/drivers
Accept: application/json
```

**Response:**
```
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 5,
    "name": "Nikola Tsolov",
    "dateOfBirth": "2006-12-21",
    "nationality": "Bulgarian",
    "worldChampionships": 0,
    "teamId": 4,
    "teamName": "Levski",
    "imageUrl": "/img/Nikola.png"
  },
  {
    "id": 4,
    "name": "Carlos Sainz",
    "dateOfBirth": "1994-09-01",
    "nationality": "Spanish",
    "worldChampionships": 0,
    "teamId": 2,
    "teamName": "Ferrari",
    "imageUrl": "/img/Carlos.png"
  },
  {
    "id": 3,
    "name": "Charles Leclerc",
    "dateOfBirth": "1997-10-16",
    "nationality": "Monegasque",
    "worldChampionships": 0,
    "teamId": 2,
    "teamName": "Ferrari",
    "imageUrl": "/img/Leclerc.png"
  },
  {
    "id": 2,
    "name": "Max Verstappen",
    "dateOfBirth": "1997-09-30",
    "nationality": "Dutch",
    "worldChampionships": 2,
    "teamId": 3,
    "teamName": "Red Bull Racing",
    "imageUrl": "/img/Verstappen.png"
  },
  {
    "id": 1,
    "name": "Lewis Hamilton",
    "dateOfBirth": "1985-01-07",
    "nationality": "British",
    "worldChampionships": 7,
    "teamId": 1,
    "teamName": "Mercedes",
    "imageUrl": "/img/Hamilton.png"
  }
]
```

---

### Fetching single driver by ID - OK (200)

**Request:**
```
GET http://localhost:8080/api/drivers/1
Accept: application/json
```

**Response:**
```
HTTP/1.1 200
Content-Type: application/json

{
  "id": 1,
  "name": "Lewis Hamilton",
  "dateOfBirth": "1985-01-07",
  "nationality": "British",
  "worldChampionships": 7,
  "teamId": 1,
  "teamName": "Mercedes",
  "imageUrl": "/img/Hamilton.png"
}
```

---

### Fetching single driver by ID - Not Found (404)

**Request:**
```
GET http://localhost:8080/api/drivers/999
Accept: application/json
```

**Response:**
```
HTTP/1.1 404
Content-Length: 0
```

---

### Fetching races for a specific driver - OK (200)

**Request:**
```
GET http://localhost:8080/api/drivers/1/races
Accept: application/json
```

**Response:**
```
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "name": "Monaco Grand Prix",
    "date": "2025-05-25",
    "trackId": 1,
    "trackName": "Monaco GP",
    "winnerId": 1,
    "winnerName": "Lewis Hamilton",
    "hasEnded": true
  },
  {
    "id": 2,
    "name": "British Grand Prix",
    "date": "2025-07-13",
    "trackId": 2,
    "trackName": "Silverstone",
    "winnerId": 2,
    "winnerName": "Max Verstappen",
    "hasEnded": true
  }
]
```

---

### Fetching races for a specific driver - Not Found (404)

**Request:**
```
GET http://localhost:8080/api/drivers/999/races
Accept: application/json
```

**Response:**
```
HTTP/1.1 404
Content-Length: 0
```

---

### Deleting a driver - No Content (204)

**Request:**
```
DELETE http://localhost:8080/api/drivers/5
```

**Response:**
```
HTTP/1.1 204
```

---

### Deleting a driver - Not Found (404)

**Request:**
```
DELETE http://localhost:8080/api/drivers/999
```

**Response:**
```
HTTP/1.1 404
Content-Length: 0
```

---

## Week 3

### Creating a driver - Created (201)

**Request:**
```http
POST http://localhost:8080/api/drivers
Accept: application/json
Content-Type: application/json

{
  "name": "Lando Norris",
  "dateOfBirth": "1999-11-13",
  "nationality": "British",
  "worldChampionships": 0,
  "imageUrl": "/img/Lando.png"
}
```

**Response:**
```http
HTTP/1.1 201
Content-Type: application/json

{
  "id": 6,
  "name": "Lando Norris",
  "dateOfBirth": "1999-11-13",
  "nationality": "British",
  "worldChampionships": 0,
  "imageUrl": "/img/Lando.png"
}
```

### Creating a driver - Bad Request (400)

**Request:**
```http
POST http://localhost:8080/api/drivers
Accept: application/json
Content-Type: application/json

{
  "name": "A",
  "dateOfBirth": "2099-01-01",
  "nationality": "B",
  "worldChampionships": 99,
  "imageUrl": ""
}
```

**Response:**
```http
HTTP/1.1 400
Content-Type: application/json
```

### Updating a driver with PATCH - OK (200)

**Request:**
```http
PATCH http://localhost:8080/api/drivers/5
Accept: application/json
Content-Type: application/json

{
  "worldChampionships": 3
}
```

**Response:**
```http
HTTP/1.1 200
Content-Type: application/json

{
  "id": 5,
  "name": "Nikola Tsolov",
  "dateOfBirth": "2006-12-21",
  "nationality": "Bulgarian",
  "worldChampionships": 3,
  "imageUrl": "/img/Nikola.png"
}
```

### Updating a driver with PATCH - Bad Request (400)

**Request:**
```http
PATCH http://localhost:8080/api/drivers/5
Accept: application/json
Content-Type: application/json

{
  "worldChampionships": 11
}
```

**Response:**
```http
HTTP/1.1 400
Content-Type: application/json
```

### Updating a driver with PATCH - Not Found (404)

**Request:**
```http
PATCH http://localhost:8080/api/drivers/999
Accept: application/json
Content-Type: application/json

{
  "worldChampionships": 1
}
```

**Response:**
```http
HTTP/1.1 404
Content-Length: 0
```

### Deleting a driver - No Content (204)

**Request:**
```http
DELETE http://localhost:8080/api/drivers/1
Accept: application/json
```

**Response:**
```http
HTTP/1.1 204
```

### Deleting a driver - Not Found (404)

**Request:**
```http
DELETE http://localhost:8080/api/drivers/999
Accept: application/json
```

**Response:**
```http
HTTP/1.1 404
Content-Length: 0
```

---

## Week 5

### Seeded users

The application seeds three persisted users in the `app_users` table. Passwords are stored as BCrypt hashes in the database, but the following credentials can be used to log in:

| Username | Password | Role  | Managed team |
|----------|----------|-------|--------------|
| `Daniel` | `Dani`   | `ADMIN` | Mercedes |
| `Ivan`   | `Ivan`   | `USER`  | Ferrari |
| `Viki`   | `Viki`   | `USER`  | Red Bull Racing |

### Roles and access rights

The application distinguishes between three access categories: unauthenticated visitors, signed-in `USER`s, and signed-in `ADMIN`s.

| Category | Can access | Can create | Can update/delete | Notes |
|----------|------------|------------|-------------------|-------|
| Unauthenticated user | Public driver pages such as [All Drivers](http://localhost:8080/drivers) and the home page | No | No | Hidden from add/edit/delete actions and redirected to `/login` for protected pages |
| `USER` | Public pages plus protected pages after login, including [All Teams](http://localhost:8080/teams) | Can create drivers | Can update/delete only drivers associated with the same managed team | Cannot manage teams |
| `ADMIN` | All pages | Can create drivers and teams | Can update/delete any driver and can manage teams | Can also assign or reassign driver teams |

### Hidden UI elements

The UI hides actions that the current visitor cannot perform:

- On [All Drivers](http://localhost:8080/drivers), unauthenticated visitors do not see the inline add-driver form or the edit/delete buttons on driver cards.
- On [All Teams](http://localhost:8080/teams), non-admin users do not see the add-team button or the delete-team action.
- In the navbar, admin-only quick actions such as `Add Team` are hidden from non-admin users.

The backend also verifies these restrictions. Hidden controls are only a convenience layer; controller and service logic still enforce the same access rules.

### User-to-entity relationship

The persisted user entity is `AppUser`. It is associated with `Team` through the `managerInTeam` relation.

- A signed-in user who creates a new driver creates it for their managed team.
- Drivers are therefore associated to users indirectly through the team they belong to.
- A normal `USER` may update or delete only drivers belonging to their own managed team.
- An `ADMIN` may update or delete any driver.
- Users without a managed team cannot manage drivers.

This can be observed on:

- [All Drivers](http://localhost:8080/drivers)
- [Driver Details Example](http://localhost:8080/drivers/1)
- [All Teams](http://localhost:8080/teams)

### Authentication and CSRF

- Custom login page: `/login`
- Logout endpoint/form: `/logout`
- Logged-in username is shown in the navbar with `sec:authentication="name"`.
- CSRF protection is enabled.
- Thymeleaf exposes the CSRF token and header name through meta tags in the page header.
- AJAX requests include the CSRF header when calling the REST API, so the API and Ajax flows keep working with CSRF enabled.

### Verification links

- Public page: [All Drivers](http://localhost:8080/drivers)
- Public driver details example: [Driver 1](http://localhost:8080/drivers/1)
- Authentication required page: [All Teams](http://localhost:8080/teams)
- Login page: [Login](http://localhost:8080/login)
