# Deliveries schema

# --- !Ups

CREATE TABLE Deliveries (
    id bigserial primary key,
    foodAvailabilityId bigserial NOT NULL,
    delivererId bigserial NOT NULL
);

# --- !Downs

DROP TABLE Deliveries;