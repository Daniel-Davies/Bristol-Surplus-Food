# a deliverer can have more than one delivery

# --- !Ups
ALTER TABLE Deliveries DROP CONSTRAINT one_delivery_per_deliverer;


# --- !Downs

ALTER TABLE Deliveries ADD CONSTRAINT one_delivery_per_deliverer UNIQUE (delivererId);
