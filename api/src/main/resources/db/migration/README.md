# Database migrations

Flyway selects the `mysql` or `h2` directory through the `{vendor}` placeholder
configured in `application.yaml`.

- `V1__create_initial_schema.sql` creates the original `cars` and `users`
  tables. Existing pre-Flyway databases are baselined at this version.
- `V2__add_car_rental_attributes.sql` adds the rental-search fields to `cars`.
  Defaults keep legacy rows valid until their real QA or production values are
  supplied.
- `V3__create_bookings.sql` creates bookings, lifecycle statuses, foreign keys,
  data-integrity checks, and indexes for overlap and user lookups.
- `V4__drop_bookings.sql` removes the discontinued bookings feature table.
- `V5__recreate_bookings.sql` recreates bookings after the feature was restored.

Applied migration files are immutable because changing even a comment changes
their Flyway checksum. Add a new versioned migration for every later schema
change.
