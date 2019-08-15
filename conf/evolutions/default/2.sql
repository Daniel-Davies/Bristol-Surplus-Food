# FoodCategories schema

# --- !Ups

CREATE TABLE FoodCategories (
    id bigserial primary key,
    name varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE FoodCategories;