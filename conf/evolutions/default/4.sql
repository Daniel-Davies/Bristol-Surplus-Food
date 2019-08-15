# FoodCollections schema

# --- !Ups

CREATE TABLE FoodCollections (
    id bigserial primary key,
    userId bigserial NOT NULL REFERENCES Users,
    availableTimeStart timestamp without time zone  NOT NULL,
    availableTimeEnd timestamp without time zone NOT NULL,
    comingDeliveryId INT NULL REFERENCES Deliveries,
    type varchar(255) NOT NULL
);

# --- !Downs

DROP TABLE FoodCollections;