# Merging foodRequests and FoodAvailabilities into FoodCollections
# We dont really need Deliveries anymore, they only contain the delivererId...


# --- !Ups



ALTER TABLE Deliveries DROP COLUMN IF EXISTS foodAvailabilityId;
ALTER TABLE Deliveries ADD CONSTRAINT deliverer_ref FOREIGN KEY (delivererId) REFERENCES Users;
ALTER TABLE Deliveries DROP COLUMN IF EXISTS id CASCADE;
ALTER TABLE Deliveries ADD COLUMN id INT PRIMARY KEY;

ALTER TABLE FoodItems DROP COLUMN IF EXISTS foodCollectionType;
ALTER TABLE FoodItems ADD CONSTRAINT collection_ref FOREIGN KEY (foodCollectionId) REFERENCES FoodCollections;
DROP TABLE IF EXISTS FoodRequests;
DROP TABLE IF EXISTS FoodAvailabilities;



# --- !Downs

ALTER TABLE Deliveries DROP CONSTRAINT deliverer_ref;
ALTER TABLE Deliveries DROP COLUMN foodCollectionId;
ALTER TABLE FoodItems DROP CONSTRAINT collection_ref;


