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

**Last Updated:** February 6, 2026
