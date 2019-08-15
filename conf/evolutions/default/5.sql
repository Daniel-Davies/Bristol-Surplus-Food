# FoodRequests schema NOT USED ANYMORE

# --- !Ups

CREATE TABLE FoodRequests (
    id bigserial PRIMARY KEY,
    beneficiaryId bigserial NOT NULL REFERENCES Users,
    comingDeliveryId bigserial REFERENCES Deliveries
);

# --- !Downs

DROP TABLE FoodRequests;
