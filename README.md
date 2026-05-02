# GoodLad Forge 🔥

> **A coding + real-world developer simulation platform with the AlgoBid Arena system.**

---

## 1. Product Overview

**GoodLad Forge** is a backend platform that powers a developer upskilling ecosystem. Developers solve algorithmic and real-world coding challenges, compete in time-limited arena contests, and climb a live leaderboard based on performance and speed.

At its core is **AlgoBid Arena** — a competitive coding arena where developers race against the clock, earn scores based on how fast and accurately they solve problems, and compete against peers in structured contests.

---

## 2. Problem Statement

Most coding platforms (LeetCode, HackerRank) are passive: you solve problems alone with no real competitive pressure and no sense of consequence. GoodLad Forge changes that by:

- Making **time a first-class scoring factor** — solve faster, score higher
- Introducing **Arena contests** (AlgoBid) with defined start/end windows
- Building a **live leaderboard** that reflects real performance
- Creating the foundation for **anti-cheat**, **AI evaluation**, and **job integration** in future versions

---

## 3. Features (MVP)

### 👤 User Module
| Feature | Endpoint |
|---|---|
| Register a new user | `POST /api/users/register` |
| Login (session-less) | `POST /api/users/login` |
| Get user by ID | `GET /api/users/{id}` |

### 📚 Problem Module
| Feature | Endpoint |
|---|---|
| Create problem | `POST /api/problems` |
| Get all active problems | `GET /api/problems` |
| Get problem by ID | `GET /api/problems/{id}` |
| Filter by difficulty | `GET /api/problems/difficulty/{EASY\|MEDIUM\|HARD}` |
| Deactivate problem | `DELETE /api/problems/{id}` |

### 📤 Submission Module
| Feature | Endpoint |
|---|---|
| Submit code | `POST /api/submissions` |
| Get submission by ID | `GET /api/submissions/{id}` |
| Get user's submissions | `GET /api/submissions/user/{userId}` |
| Get problem's submissions | `GET /api/submissions/problem/{problemId}` |

### 🏟️ Arena (AlgoBid) Module
| Feature | Endpoint |
|---|---|
| Create contest | `POST /api/arena/contests` |
| List all contests | `GET /api/arena/contests` |
| Get contest by ID | `GET /api/arena/contests/{id}` |
| Get active contests | `GET /api/arena/contests/active` |
| Get upcoming contests | `GET /api/arena/contests/upcoming` |

### 🏆 Leaderboard Module
| Feature | Endpoint |
|---|---|
| Global leaderboard | `GET /api/leaderboard` |
| Top N users | `GET /api/leaderboard/top/{n}` |

---

## 4. Architecture Flow

```
React Frontend (Future)
        │
        ▼
REST API (Spring Boot Controllers)
        │
        ▼
Service Layer (Business Logic + Anti-Cheat)
        │
        ▼
Repository Layer (Spring Data JPA)
        │
        ▼
PostgreSQL Database
```

### Package Structure

```
com.goodlad.forge
├── ForgeApplication.java
├── common/
│   ├── exception/
│   │   ├── ForgeException.java
│   │   └── GlobalExceptionHandler.java
│   └── response/
│       └── ApiResponse.java
├── user/
│   ├── controller/UserController.java
│   ├── service/UserService.java
│   ├── repository/UserRepository.java
│   ├── model/User.java
│   └── dto/RegisterRequest, LoginRequest, UserResponse
├── problem/
│   ├── controller/ProblemController.java
│   ├── service/ProblemService.java
│   ├── repository/ProblemRepository.java
│   ├── model/Problem.java
│   └── dto/ProblemRequest, ProblemResponse
├── submission/
│   ├── controller/SubmissionController.java
│   ├── service/SubmissionService.java
│   ├── repository/SubmissionRepository.java
│   ├── model/Submission.java
│   └── dto/SubmitRequest, SubmissionResponse
├── arena/
│   ├── controller/ContestController.java
│   ├── service/ContestService.java
│   ├── repository/ContestRepository.java
│   ├── model/Contest.java
│   └── dto/ContestRequest, ContestResponse
└── leaderboard/
    ├── controller/LeaderboardController.java
    └── service/LeaderboardService, LeaderboardEntry
```

---

## 5. Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |
| Boilerplate reduction | Lombok |

---

## 6. Database Schema

### `users`
| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL PK | Auto-generated |
| username | VARCHAR(50) UNIQUE | Login handle |
| email | VARCHAR(120) UNIQUE | Contact |
| password | TEXT | Plain for MVP — hash in production |
| role | VARCHAR | USER / ADMIN |
| problems_solved | INT | Auto-updated on accepted submissions |
| total_score | INT | Cumulative score across all submissions |
| created_at | TIMESTAMP | Auto-set |

### `problems`
| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL PK | |
| title | VARCHAR(200) | |
| description | TEXT | Full problem statement |
| difficulty | VARCHAR | EASY / MEDIUM / HARD |
| points | INT | Base score value |
| time_limit_seconds | INT | Used for scoring bonus/penalty |
| active | BOOLEAN | Soft-delete flag |
| created_at | TIMESTAMP | |

### `submissions`
| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL PK | |
| user_id | BIGINT FK → users | |
| problem_id | BIGINT FK → problems | |
| code | TEXT | Full code submitted |
| language | VARCHAR | JAVA, PYTHON, etc. |
| status | VARCHAR | PENDING / ACCEPTED / WRONG_ANSWER / TLE / COMPILE_ERROR |
| time_taken_seconds | BIGINT | Reported solve time |
| score | INT | Calculated on submission |
| duplicate | BOOLEAN | Anti-cheat flag |
| contest_id | BIGINT | Optional arena context |
| submitted_at | TIMESTAMP | |

### `contests`
| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL PK | |
| title | VARCHAR(200) | |
| description | TEXT | |
| start_time | TIMESTAMP | |
| end_time | TIMESTAMP | |
| status | VARCHAR | UPCOMING / ACTIVE / ENDED |
| created_at | TIMESTAMP | |

### `contest_problems` (join table)
| Column | Type |
|---|---|
| contest_id | BIGINT FK → contests |
| problem_id | BIGINT |

---

## 7. Running the Project

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL running locally

### Setup

**1. Create the database**
```sql
CREATE DATABASE goodlad_forge;
```

**2. Configure credentials**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/goodlad_forge
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**3. Run**
```bash
./mvnw spring-boot:run
```

The server starts at **http://localhost:8080**

Tables are auto-created by Hibernate (`ddl-auto=update`).

---

## 8. Scoring Formula

```
Base Score = problem.points

If timeTaken < 50% of timeLimit → Score = base × 1.5  (speed bonus)
If timeTaken > 80% of timeLimit → Score = base × 0.75 (slow penalty)
Otherwise                       → Score = base
```

Leaderboard rank = `totalScore DESC`, tiebreak = `problemsSolved DESC`

---

## 9. Anti-Cheat Strategy (MVP)

Three lightweight checks run on every submission:

| Check | Logic |
|---|---|
| **Duplicate code detection** | If the user submits the exact same code for the same problem again, it's flagged as a duplicate and marked WRONG_ANSWER |
| **Speed sanity check** | If `timeTakenSeconds < 10`, the submission is flagged as suspicious and marked WRONG_ANSWER |
| **Contest time enforcement** | Submissions are linked to a `contestId`; out-of-window submissions can be filtered at the API level in future iterations |

No penalty is logged against the user permanently at MVP — this is intentionally lightweight. The `duplicate` boolean field is stored per submission for auditing.

---

## 10. Sample API Calls

**Register**
```json
POST /api/users/register
{
  "username": "devwarrior",
  "email": "dev@forge.io",
  "password": "secret123"
}
```

**Create Problem**
```json
POST /api/problems
{
  "title": "Two Sum",
  "description": "Given an array of integers, return indices of two numbers that add up to a target.",
  "difficulty": "EASY",
  "points": 20,
  "timeLimitSeconds": 300
}
```

**Submit Code**
```json
POST /api/submissions
{
  "userId": 1,
  "problemId": 1,
  "code": "class Solution { public int[] twoSum(...) { ... } }",
  "language": "JAVA",
  "timeTakenSeconds": 120
}
```

**Create Contest**
```json
POST /api/arena/contests
{
  "title": "AlgoBid Round #1",
  "description": "First competitive coding arena round.",
  "startTime": "2026-05-10T10:00:00",
  "endTime": "2026-05-10T12:00:00",
  "problemIds": [1, 2, 3]
}
```

---

## 11. Future Scope

### 🔬 Forge Labs
A sandboxed code execution environment where submissions are evaluated against real test cases using Docker containers or remote judge integration (Judge0 API).

### 👤 Profile System
User profiles with badges, submission history, heat maps, and skill tags similar to GitHub contribution graphs.

### 🤖 AI Evaluation
Integrate an LLM layer to provide:
- Code quality feedback
- Time/space complexity estimation
- Alternate solution suggestions

### 💼 Job Integration
Verified solve scores can be shared with recruiters. Companies post "forge challenges" — problems customised to their hiring criteria. Think of it as a live resume.

### 🔐 JWT Authentication
Replace the session-less login with full JWT-based stateless authentication with refresh tokens.

### 📊 Analytics Dashboard
Per-user analytics: solve time distribution, accuracy rate, performance per difficulty, ranking trajectory.

### 🌍 Real-World Simulation Tracks
Beyond algorithms — system design challenges, debugging sessions, code review tasks simulating actual dev workflows.
