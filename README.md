# DriveEase — Your Journey. Your Drive.

## What is included
- `frontend/index.html` — landing page with the raw WebGL2 halftone engine (open directly in a browser).
- `frontend/admin.html` — admin dashboard (sample data, KPI cards, revenue chart, utilization donut, searchable bookings table).
- `backend/` — Spring Boot 3 / Java 21 core: BigDecimal pricing with tier discounts + 18% GST, separate refundable deposit,
  availability check, `POST /api/v1/bookings/quote`, Flyway schema with a PostgreSQL **exclusion constraint** that blocks
  double bookings on `[pickup, return)` even under concurrent requests, seeded tiers (Standard/Silver/Gold/Corporate) and 6 vehicles.
- `docker-compose.yml` (PostgreSQL), `.env.example`.

## Run
1. `docker compose up -d`
2. `cd backend && mvn spring-boot:run` (tests: `mvn test` — pricing tests need no database)
3. Open `frontend/index.html`.

## Not built yet (honest status)
Authentication/JWT, booking creation + status transitions, admin CRUD APIs, payments, reports/CSV, React/Vite port,
and wiring the pages to the API. This package was assembled without network access, so the Maven build has **not been run** — 
if `mvn test` reports an error, fix it before building further.
