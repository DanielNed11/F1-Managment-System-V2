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

2. **Start the PostgreSQL database using Docker:**
   ```bash
   docker-compose up -d
   ```
   This will start a PostgreSQL database on port 5432.

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

### Stopping the Application

- **Stop Spring Boot:** Press `Ctrl+C` in the terminal
- **Stop PostgreSQL:**
  ```bash
  docker-compose down
  ```

### Useful Commands

```bash
# Clean and rebuild
./gradlew clean build

# Run tests
./gradlew test

# Check Docker containers status
docker-compose ps

# View database logs
docker-compose logs postgres

# Access PostgreSQL CLI
docker exec -it programming5-postgres psql -U f1user -d f1db
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
└── application.properties

docker-compose.yml       # PostgreSQL database configuration
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

## Week 4

### Seeded users

- `Daniel` / `Dani`
- `Ivan` / `Ivan`
- `Viki` / `Viki`


Passwords are stored as BCrypt hashes in the database (`app_users` table).

### Required links

- Public page: [All Drivers](http://localhost:8080/drivers)
- Authentication required page: [All Teams](http://localhost:8080/teams)

### Login and logout

- Custom login page: `/login`
- Logout endpoint/form: `/logout`
- Logged in username is shown in the navbar with `sec:authentication="name"`.

### Authorization behavior

- Anonymous users can access public pages (for example `/drivers`).
- Authenticated users can access protected pages and perform data-changing actions.
- On `/drivers`, authenticated users see application-specific controls (add form, edit/delete card actions) while anonymous users do not.

### Notes

- CSRF is temporarily disabled as required for this assignment.
- REST API and AJAX flows are kept functional with authentication-aware handling.
