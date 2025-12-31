# Formula 1 Management System

**Student Name:** Daniel Nedyalkov
**Course:** Programming 3

---

## Domain Description

This is a **Formula 1 Management System** managing four core entities with relationships:

### Entities

1. **Driver** - F1 racing drivers
   - Properties: name, dateOfBirth, nationality, worldChampionships, imageUrl
   - Relations: belongs to one Team, participates in many Races

2. **Team** - F1 racing teams
   - Properties: name, foundedYear, league (enum), teamLogoUrl, budgetInMillions
   - Relations: has many Drivers

3. **Race** - F1 racing events
   - Properties: name, date, hasEnded
   - Relations: held at one Track, has one winner (Driver), has many participating Drivers

4. **Track** - Racing circuits with inheritance
   - Base properties: name, location, lengthKm, openedYear
   - **StreetCircuit** subclass: cityName, daysToSetup, annualRentalCost, hasTemporaryBarriers
   - **PermanentCircuit** subclass: hasMuseum, testDaysPerYear, facilities

### Entity Relationships

```
Team (1) ----< (many) Driver
Driver (many) >----< (many) Race  (many-to-many)
Track (1) ----< (many) Race
Race (many) >---- (1) Driver (as winner)

Track (inheritance - Single Table)
  ├── StreetCircuit
  └── PermanentCircuit
```

---

## Application Profiles

The application supports **4 repository implementations** switchable via profiles:

### Repository Profiles

1. **`collection`** - In-memory ArrayList (Week 2-4)
2. **`jdbc`** - Spring JDBC with JdbcClient (Week 6)
3. **`jpa`** - JPA with EntityManager (Week 9)
4. **`spring-data`** - Spring Data JPA repositories (Week 10)

### Database Profiles

1. **`dev`** - H2 in-memory database (automatic)
2. **`prod`** - PostgreSQL

### Switch Profiles

Edit `src/main/resources/application.properties`:
```properties
spring.profiles.active=spring-data,dev

spring.profiles.active=jpa,dev

spring.profiles.active=jdbc,prod

spring.profiles.active=collection
```

---

## Database Configuration

### H2 (Development)
- **Console:** http://localhost:8080/h2-console
- No manual setup required - auto-configured

### PostgreSQL (Production)
```properties
spring.datasource.url=jdbc:postgresql:pro3_db
spring.datasource.username=postgres
spring.datasource.password=Student_1234
```

## Running the Application

### Steps

1. Clone/navigate to project:
   ```bash
   cd /path/to/Programming_3
   ```

2. Build:
   ```bash
   ./gradlew build
   ```

3. Run:
   ```bash
   ./gradlew bootRun
   ```

4. Access application:
   - **Start URL:** http://localhost:8080
   - **H2 Console:** http://localhost:8080/h2-console (dev profile)

### Default Settings
- Profile: `spring-data,dev`
- Port: 8080
- Language: English (switch via navbar)

---

## Completed Features

### ✅ All Assignment Requirements
- [x] 3-tier layered architecture
- [x] 4 entities with all relationships
- [x] Full CRUD for all entities
- [x] Track inheritance (StreetCircuit, PermanentCircuit)
- [x] 4 repository implementations (collection, jdbc, jpa, spring-data)
- [x] Spring Data JPA method queries and @Query
- [x] Bean Validation with custom messages
- [x] Exception handling (custom + global)
- [x] Thymeleaf views with Bootstrap 5
- [x] Multi-language support (EN/BG)
- [x] Session history tracking
- [x] H2 and PostgreSQL support

### ✅ Extra Features
- [x] Query result pages (champions, long tracks, upcoming races)
- [x] Responsive design with Bootstrap
- [x] Custom CSS styling
- [x] Delete confirmations
- [x] Form validation with error display
- [x] Bootstrap Icons integration

---

## Parts Not Completed

All requirements have been fully implemented.

---

## Unique Implementation

### Technical Excellence

1. **Four Repository Implementations**
   - Shows evolution from simple collections to advanced Spring Data
   - Easy switching via profiles demonstrates abstraction
   - Each implementation handles polymorphism correctly (Track subclasses)

2. **Advanced Polymorphism with JPA**
   - Single Table Inheritance for Track entity
   - Type-specific forms for StreetCircuit vs PermanentCircuit
   - Conditional rendering in Thymeleaf based on track type
   - Manual parameter extraction to preserve subclass types during form binding

### Functional Features

1. **Track Type Management**
   - Type-specific forms with conditional field display
   - Visual badges and icons distinguish track types
   - Full edit support preserving subclass properties

2. **Advanced Queries**
   - Champions page: Method query `findByWorldChampionshipsGreaterThan(0)`
   - Long tracks page: Stream filtering for tracks >5km
   - Upcoming races page: Custom `@Query` for races not ended
   - All accessible from footer Quick Links

3. **Session History**
   - Tracks all page visits with timestamps
   - Session-scoped (per browser tab)
   - Clear history functionality
   - Displays last 20 visits

## Quick Test Guide

1. **Test Track Inheritance:**
   - Navigate to "Tracks" → "Add Street Circuit"
   - Fill form with street-specific fields (city name, rental cost, etc.)
   - Save and verify badge shows "Street Circuit"
   - Repeat for "Permanent Circuit"

2. **Test Queries:**
   - Footer → "Champions" (drivers with ≥1 championship)
   - Footer → "Long Tracks" (tracks >5km)
   - Footer → "Upcoming" (races not ended)

3. **Test Languages:**
   - Click language switcher in navbar (EN ⇄ BG)
   - Verify all text changes

---

**Last Updated:** December 31, 2024
