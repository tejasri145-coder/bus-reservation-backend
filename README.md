# Bus Reservation System - Backend

A clean, production-ready Spring Boot backend for a Bus Reservation System built with Java 21, Spring Boot 3, Spring Data JPA, Lombok, and MySQL.

---

## 🚀 Tech Stack

- **Java**: 21 (LTS)
- **Framework**: Spring Boot 3.3.4
- **Build Tool**: Maven
- **Modules & Starters**:
  - `spring-boot-starter-web`: RESTful Web APIs
  - `spring-boot-starter-data-jpa`: Object-Relational Mapping & Spring Data JPA
  - `spring-boot-starter-validation`: Request validation with Jakarta Bean Validation
  - `mysql-connector-j`: MySQL JDBC Driver
  - `lombok`: Boilerplate reduction (Getters, Setters, Builders, Constructors)

---

## 🏛️ Layered Architecture

The project follows a clean layered architecture designed for separation of concerns and maintainability:

```
src/main/java/com/busreservation/backend/
├── config/                  # CORS, Database seeding (DataInitializer)
├── controller/              # REST Controllers handling HTTP requests
├── dto/                     # Request and Response transfer objects
├── entity/                  # JPA Entities (User, Bus, Route, Trip, Booking)
├── exception/               # Custom Exceptions & GlobalExceptionHandler
├── repository/              # Spring Data JPA Repositories
├── service/                 # Business logic interfaces
│   └── impl/                # Service implementations
└── BusReservationApplication.java # Spring Boot entry point
```

---

## 🗄️ Database Setup (MySQL)

Ensure MySQL server is running (e.g., via MySQL Workbench, XAMPP, Docker, or native service).

The application is configured in `src/main/resources/application.properties` to automatically connect to MySQL:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/bus_reservation?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> **Note:** Update `spring.datasource.username` and `spring.datasource.password` or configure `DB_USERNAME` and `DB_PASSWORD` environment variables to match your local MySQL credentials.

---

## 🏁 Running the Application

### Using Maven:
```bash
mvn spring-boot:run
```

### Or compile and run jar:
```bash
mvn clean package -DskipTests
java -jar target/bus-reservation-backend-0.0.1-SNAPSHOT.jar
```

The application will start on: `http://localhost:8080`

### Health Check Endpoints
- `GET http://localhost:8080/` -> `"Bus Reservation Backend is Running Successfully!"`
- `GET http://localhost:8080/health` or `/api/health` -> `{"status":"UP","service":"bus-reservation-backend","database":"MySQL"}`

---

## 📡 REST API Documentation

### 1. Bus Endpoints (`/api/bus`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/bus` | Get list of all buses (JSON array for frontend consumption) |
| `POST` | `/api/bus` | Add new bus (Admin) |
| `GET` | `/api/bus/{id}` | Get bus details by ID |
| `PUT` | `/api/bus/{id}` | Update bus details |
| `DELETE` | `/api/bus/{id}` | Remove a bus |

#### Sample Bus Body:
```json
{
  "busNumber": "EXPR-882",
  "operatorName": "Royal Express",
  "busType": "AC_SLEEPER",
  "totalSeats": 36,
  "active": true
}
```
*Supported `busType` values: `AC_SLEEPER`, `NON_AC_SLEEPER`, `AC_SEATER`, `NON_AC_SEATER`*

---

### 3. Route Endpoints (`/api/routes`)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/routes` | Create new travel route |
| `GET` | `/api/routes` | Get all travel routes |
| `GET` | `/api/routes/{id}` | Get route by ID |
| `PUT` | `/api/routes/{id}` | Update route |
| `DELETE` | `/api/routes/{id}` | Delete route |

#### Sample Route Body:
```json
{
  "sourceCity": "New York",
  "destinationCity": "Washington DC",
  "distanceKm": 360.5,
  "estimatedDuration": "4h 45m"
}
```

---

### 4. Trip Endpoints (`/api/trips`)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/trips` | Schedule a new trip |
| `GET` | `/api/trips` | Get all trips |
| `GET` | `/api/trips/{id}` | Get trip details by ID |
| `GET` | `/api/trips/search?source=New York&destination=Boston&date=2026-10-15` | Search trips & calculate available seats |
| `PUT` | `/api/trips/{id}` | Update trip schedule or fare |
| `DELETE` | `/api/trips/{id}` | Cancel/delete trip |

#### Sample Trip Body:
```json
{
  "busId": 1,
  "routeId": 1,
  "departureTime": "09:00:00",
  "arrivalTime": "13:30:00",
  "fare": 45.00,
  "active": true
}
```

---

### 5. Booking Endpoints (`/api/bookings`)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/bookings` | Book seats for a trip on a specific date |
| `GET` | `/api/bookings/{id}` | Get booking by ID |
| `GET` | `/api/bookings/number/{bookingNumber}` | Lookup booking by reference code |
| `GET` | `/api/bookings/user/{userId}` | Get booking history for a user |
| `GET` | `/api/bookings` | Get all system bookings (Admin) |
| `PUT` | `/api/bookings/{id}/cancel` | Cancel an existing reservation |
| `GET` | `/api/bookings/booked-seats?tripId=1&journeyDate=2026-10-15` | Get booked seat list for frontend seat layout |

#### Sample Booking Request:
```json
{
  "userId": 2,
  "tripId": 1,
  "journeyDate": "2026-10-15",
  "seatNumbers": ["12", "13"]
}
```

#### Booking Features:
- **Conflict Prevention**: Validates if any requested seat is already booked for that trip on that specific journey date.
- **Dynamic Fare**: Automatically calculates `totalFare = trip.fare * seatNumbers.size()`.
- **Unique Booking Reference**: Generates structured booking numbers like `BR-20261015-8A3F12`.
- **Seat Availability Endpoint**: Facilitates interactive seat picker UI on the frontend.

---

## 🌐 Frontend Integration & CORS

CORS is globally enabled in `CorsConfig.java` to support requests from frontend dev servers (React, Vite, Next.js, Vue, etc.) without cross-origin blocking.
