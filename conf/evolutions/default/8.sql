

# --- !Ups

ALTER TABLE FoodRequests ADD COLUMN availableTimeStart timestamp without time zone NOT NULL;
ALTER TABLE FoodRequests ADD COLUMN availableTimeEnd timestamp without time zone NOT NULL;

# --- !Downs

ALTER TABLE FoodRequests DROP COLUMN availableTimeStart;
ALTER TABLE FoodRequests DROP COLUMN availableTimeEnd;
