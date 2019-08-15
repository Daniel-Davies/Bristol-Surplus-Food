# Users schema

# --- !Ups

CREATE TABLE Users (
    id bigserial primary key,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    type varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE Users;