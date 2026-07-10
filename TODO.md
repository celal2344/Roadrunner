# Roadrunner TODO

This is the learning roadmap for taking the current Spring Boot API toward observability, async messaging, and AWS-style deployment practice without using real AWS cloud resources.

## Current baseline

- [x] Spring Boot API lives under `api/`.
- [x] Java 21 + Spring Boot 4.1.0.
- [x] MySQL + Spring Data JPA.
- [x] Flyway migrations.
- [x] Redis dependency/config exists.
- [x] Spring Security + JWT auth exists.
- [x] Redis-backed refresh token flow exists.
- [x] Actuator dependency exists.
- [x] Basic health/info config exists.
- [ ] Full deployment-style local environment is not done yet.
- [ ] Monitoring stack is not done yet.
- [ ] RabbitMQ async messaging is not done yet.
- [ ] LocalStack + Terraform AWS practice is not done yet.

Important rule: do not use real AWS credentials for this learning phase.

---

## 1. Docker local runtime

Goal: make the whole project run like a small production system on your laptop.

Add these files:

```text
Dockerfile
compose.yaml
.env.example
```

Compose services:

- [ ] `api` - Spring Boot app.
- [ ] `mysql` - local MySQL, acts like fake RDS.
- [ ] `redis` - local Redis, acts like fake ElastiCache.
- [ ] `rabbitmq` - local RabbitMQ with management UI.
- [ ] `localstack` - fake AWS API.
- [ ] `prometheus` - metrics collector.
- [ ] `grafana` - metrics dashboard.

Environment work:

- [ ] Keep `.env` ignored.
- [ ] Keep `.env.example` committed.
- [ ] Make API read DB/Redis/RabbitMQ/LocalStack config from env vars.
- [ ] Add health checks for MySQL, Redis, RabbitMQ, LocalStack, and API.
- [ ] Verify fresh startup from zero with `docker compose up --build`.
- [ ] Verify Flyway runs on a fresh MySQL volume.

---

## 2. Observability first

Goal: before learning AWS deployment, know how to see if the app is alive, slow, or broken.

Actuator:

- [x] Expose `health` and `info`.
- [ ] Verify locally:
  - `GET /actuator/health`
  - `GET /actuator/health/liveness`
  - `GET /actuator/health/readiness`
- [ ] Add Prometheus support with `micrometer-registry-prometheus`.
- [ ] Expose `/actuator/prometheus`.
- [ ] Decide which actuator endpoints are public, protected, or disabled.

Logging:

- [ ] Add request logging with method, path, status, duration, and request id.
- [ ] Add correlation/request id support.
- [ ] Make log levels configurable from env vars.
- [ ] Avoid logging passwords, JWTs, refresh tokens, or secrets.
- [ ] Optional later: structured JSON logs for deployment-like runs.

Grafana dashboard:

- [ ] Request count.
- [ ] Request latency.
- [ ] Error rate.
- [ ] JVM memory.
- [ ] DB health.
- [ ] Redis health.
- [ ] RabbitMQ health.

---

## 3. RabbitMQ async messaging

Goal: learn async backend communication separately from AWS.

Use case:

```text
POST /api/cars
  -> save car
  -> publish CarCreatedEvent to RabbitMQ
  -> return 201 immediately

worker instance
  -> consumes CarCreatedEvent
  -> logs event first
  -> later simulates supplier sync / indexing / notification
```

Add dependency:

- [ ] `spring-boot-starter-amqp`

RabbitMQ config:

- [ ] Exchange: `roadrunner.events`.
- [ ] Queue: `car.created.queue`.
- [ ] Routing key: `car.created`.
- [ ] Dead-letter exchange: `roadrunner.dlx`.
- [ ] Dead-letter queue: `car.created.dlq`.
- [ ] Retry behavior.
- [ ] Idempotency guard for duplicate messages.

App role split:

```env
APP_ROLE=api
```

```env
APP_ROLE=worker
```

Tasks:

- [ ] API role exposes controllers and publishes events.
- [ ] Worker role consumes RabbitMQ messages.
- [ ] Run both roles through Docker Compose.
- [ ] Add one happy-path consumer test.
- [ ] Add one failed-message/DLQ test.

Do not use RabbitMQ as request/response RPC at first.

---

## 4. Local AWS practice with LocalStack + Terraform

Goal: practice AWS concepts locally without deploying to real AWS.

Add this structure:

```text
infra/
  localstack/
    providers.tf
    main.tf
    variables.tf
    outputs.tf
```

Tools:

- [ ] Docker Desktop.
- [ ] Terraform.
- [ ] AWS CLI.
- [ ] LocalStack container.
- [ ] Optional: `tflocal` wrapper.

Fake AWS resources to create with Terraform:

- [ ] S3 bucket: `roadrunner-uploads-local`.
- [ ] SQS queue: `roadrunner-booking-events-local` or `roadrunner-car-events-local`.
- [ ] Secrets Manager secret: `roadrunner/local/app`.
- [ ] CloudWatch log group: `/local/roadrunner/api`.
- [ ] ECR repository: `roadrunner-api`.
- [ ] ECS cluster/task/service skeleton for learning only.

Terraform commands to practice:

```bash
cd infra/localstack
terraform init
terraform fmt
terraform validate
terraform plan
terraform apply
terraform destroy
```

If using `tflocal`:

```bash
cd infra/localstack
tflocal init
tflocal apply
```

Rules:

- [ ] Use fake AWS credentials only: `AWS_ACCESS_KEY_ID=test`, `AWS_SECRET_ACCESS_KEY=test`.
- [ ] Point Terraform AWS provider endpoints to `http://localhost:4566`.
- [ ] Keep Terraform state local for now.
- [ ] Do not create a real AWS account resource yet.

---

## 5. App integration with fake AWS

Goal: connect the Spring Boot app to LocalStack resources.

S3 practice:

- [ ] Add AWS SDK S3 client config.
- [ ] Add an internal/dev endpoint to upload a dummy file to S3.
- [ ] Read S3 bucket name from env var.
- [ ] Verify object exists through AWS CLI against LocalStack.

SQS practice:

- [ ] Add AWS SDK SQS client config.
- [ ] Publish a simple `CarCreatedEvent` or `BookingRequestedEvent` to SQS.
- [ ] Add a local worker that polls SQS and logs the event.
- [ ] Compare RabbitMQ vs SQS behavior in notes.

Secrets Manager practice:

- [ ] Create fake app secret with Terraform.
- [ ] Read it locally through AWS CLI first.
- [ ] Later wire Spring Boot to read selected config from Secrets Manager.

CloudWatch practice:

- [ ] Create log group with Terraform.
- [ ] Learn the concept only; keep real app logs in Docker/Grafana for now.

---

## 6. CI/CD basics

Goal: show interview-ready release discipline.

GitHub Actions:

- [ ] Run Maven tests on push/PR.
- [ ] Cache Maven dependencies.
- [ ] Run `terraform fmt -check` for `infra/localstack`.
- [ ] Run `terraform validate` for `infra/localstack`.
- [ ] Build Docker image.
- [ ] Optional later: start MySQL/Redis with services or Testcontainers in CI.

Release flow notes:

- [ ] Write a short `DEPLOYMENT_NOTES.md`.
- [ ] Document rollback idea: redeploy previous image/tag.
- [ ] Document smoke test endpoints after deploy.

---

## 7. Carla interview-focused gaps

Use this project to prepare stories around these topics:

- [ ] REST API design for car search and car management.
- [ ] Auth flow: login, refresh, logout, roles.
- [ ] Redis use: refresh tokens, caching, rate limiting, locking.
- [ ] MySQL use: migrations, indexes, filtering, pagination.
- [ ] Observability: actuator, logs, metrics, dashboards.
- [ ] Async messaging: RabbitMQ events and SQS comparison.
- [ ] AWS basics: S3, SQS, Secrets Manager, ECR, ECS, CloudWatch.
- [ ] CI/CD: tests, Docker image build, Terraform validation.
- [ ] Production support: logs, health checks, debugging, rollback.
- [ ] Performance: indexes, pagination, caching, avoiding N+1 queries.

---

## 8. Recommended order

Do this in order:

1. [ ] Dockerfile + Compose for API, MySQL, Redis.
2. [ ] Add Prometheus endpoint and Grafana/Prometheus compose services.
3. [ ] Add request logging + request id.
4. [ ] Add RabbitMQ and `CarCreatedEvent` async flow.
5. [ ] Add LocalStack service.
6. [ ] Add Terraform for S3 + SQS first.
7. [ ] Add Terraform for Secrets Manager.
8. [ ] Add app integration with S3/SQS.
9. [ ] Add fake ECR/ECS Terraform skeleton.
10. [ ] Add GitHub Actions.

Stop after each step and make sure the app still starts and tests still pass.
