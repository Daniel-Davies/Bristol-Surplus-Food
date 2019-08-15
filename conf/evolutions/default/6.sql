# FoodItems schema

# --- !Ups

CREATE TABLE FoodItems (
    id bigserial primary key,
    amount int NOT NULL CHECK (amount > 0),
    unit int NOT NULL,
    foodCategoryId bigserial NOT NULL REFERENCES FoodCategories,
    foodCollectionId bigserial NOT NULL,
    foodCollectionType varchar(255) NOT NULL CHECK(foodCollectionType IN ('FoodAvailabilities', 'FoodRequests'))
);

# --- !Downs

DROP TABLE FoodItems;

