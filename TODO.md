# Roadrunner TODO

This is the learning roadmap for taking the current Spring Boot API from local development toward deployment, observability, and async communication practice.

## 1. Docker and local deployment

- [ ] Add a `Dockerfile` for the Spring Boot API.
- [ ] Add `docker-compose.yml` with:
  - [ ] API container
  - [ ] MySQL container
  - [ ] Redis container
  - [ ] RabbitMQ container with management UI
- [ ] Load environment variables from `.env`.
- [ ] Add container health checks for MySQL, Redis, RabbitMQ, and API.
- [ ] Make API wait for dependent services through health checks or retry-safe startup.
- [ ] Verify Flyway migrations run correctly on fresh containers.

## 2. Environment and production profiles

- [ ] Keep real secrets out of git.
- [ ] Use `.env.example` as the committed template.
- [ ] Add `application-prod.yaml` for production-specific config.
- [ ] Disable SQL logging in production.
- [ ] Set `spring.jpa.open-in-view=false` unless there is a reason to keep it.
- [ ] Decide whether Scalar/OpenAPI docs stay public, protected, or disabled in production.
- [ ] Use strict production CORS origins.
- [ ] Confirm JWT secret is provided only through environment variables in deployed environments.

## 3. Health checks and actuator

- [x] Add actuator dependency.
- [x] Expose health/info endpoints.
- [x] Permit public access to health endpoints.
- [ ] Verify these endpoints locally:
  - `GET /actuator/health`
  - `GET /actuator/health/liveness`
  - `GET /actuator/health/readiness`
- [ ] Decide whether detailed health output should be hidden, public, or authorized-only.
- [ ] Add deployment health probes using actuator endpoints.

## 4. Logging

- [ ] Add structured JSON logging for deployment environments.
- [ ] Add request logging with:
  - method
  - path
  - status
  - duration
  - request id
- [ ] Add correlation/request id support.
- [ ] Make log levels configurable through environment variables.
- [ ] Avoid logging passwords, JWTs, refresh tokens, or other secrets.
- [ ] Decide which package logs should be DEBUG locally and INFO in production.

## 5. Monitoring and metrics

- [ ] Add `micrometer-registry-prometheus`.
- [ ] Expose `/actuator/prometheus`.
- [ ] Add Prometheus to Docker Compose.
- [ ] Add Grafana to Docker Compose.
- [ ] Create basic dashboard panels for:
  - request count
  - request latency
  - error rate
  - JVM memory
  - DB health
  - Redis health
  - RabbitMQ health
- [ ] Add alerting later after the metrics are understood.

## 6. CI

- [ ] Add GitHub Actions workflow.
- [ ] Run `./mvnw test` on push and pull requests.
- [ ] Cache Maven dependencies.
- [ ] Add Docker image build later.
- [ ] Optionally run Testcontainers-based migration tests only when Docker is available.

## 7. RabbitMQ learning plan

Goal: learn async messaging without overcomplicating the app.

Recommended model:

```text
API instance
  receives HTTP request
  saves DB change
  publishes RabbitMQ event

Worker instance
  listens to RabbitMQ queue
  processes event
  logs result or updates DB
```

Even if both processes use the same codebase, treat them as different roles:

```env
APP_ROLE=api
```

```env
APP_ROLE=worker
```

- [ ] Add RabbitMQ to Docker Compose.
- [ ] Add Spring AMQP dependency.
- [ ] Add RabbitMQ config for:
  - exchange
  - queue
  - routing key
  - dead-letter exchange
  - dead-letter queue
- [ ] Create a simple event DTO, for example `CarCreatedEvent`.
- [ ] Publish `CarCreatedEvent` after a car is created.
- [ ] Add a worker consumer that logs the event.
- [ ] Run two app instances:
  - API role: exposes HTTP controllers and publishes messages
  - Worker role: consumes messages and performs async work
- [ ] Add retry behavior.
- [ ] Add dead-letter queue handling.
- [ ] Add idempotency guard so duplicate messages do not cause duplicate side effects.

Important rule: use RabbitMQ for side effects first, not synchronous request/response.

Good first use case:

```text
POST /api/cars -> save car -> publish CarCreatedEvent -> return 201
```

Avoid this at the beginning:

```text
HTTP request -> RabbitMQ -> wait for another service -> return response
```

That is RPC over RabbitMQ and is harder to reason about early.

## 8. Deployment target

Pick one path first:

- [ ] Docker Compose on a VPS: best for learning real deployment fundamentals.
- [ ] Railway/Render/Fly.io: faster managed deployment experience.
- [ ] AWS/GCP/Azure: useful later, but heavier for the first deployment pass.

Recommended first path: Docker Compose locally, then Docker Compose on a small VPS.
