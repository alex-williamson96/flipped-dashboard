# Flipped dashboard

Pre-class analytics dashboard prototype for flipped classroom instructors. Teachers can view
per-lesson completion rates, quiz scores, and individual student activity before class.

## Prerequisites

- Node.js 18+
- Java 17+
- Docker (for the database)

## Setup

### 1. Start the database

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container on port 5432 with:
- Database: `flipped_dashboard`
- Username: `postgres`
- Password: `postgres`

### 2. Configure the frontend

Create `frontend/.env`:

```
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

### 3. Install frontend dependencies

```bash
cd frontend
npm install
```

## Running the app

Start both servers, each in its own terminal.

**Backend** (from `backend/`):

```bash
./mvnw spring-boot:run
```

Serves at `http://localhost:8080`. Flyway runs migrations and loads seed data automatically on startup.

**Frontend** (from `frontend/`):

```bash
npm run dev
```

Serves at `http://localhost:5173`.

## Usage

Open `http://localhost:5173` in a browser. Use the role switcher in the top-right corner to switch
between teacher and student views.

- **Teacher view** - `/teacher/courses` to manage courses; `/teacher/dashboard` for the analytics dashboard
- **Student view** - select a student from the switcher to browse lessons and submit activity

## Running tests

**Backend** (from `backend/`):

```bash
./mvnw test
```

**Frontend** (from `frontend/`):

```bash
npm test
```
