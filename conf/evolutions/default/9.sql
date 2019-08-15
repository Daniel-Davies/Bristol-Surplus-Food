# FoodAvailabilities schema NOT USED ANYMORE

# --- !Ups

CREATE TABLE FoodAvailabilities (
    id bigserial primary key,
    supplierId bigserial NOT NULL REFERENCES Users,
    availableTimeStart timestamp without time zone  NOT NULL,
    availableTimeEnd timestamp without time zone NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS FoodAvailabilities;