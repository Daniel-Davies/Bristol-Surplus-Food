# Revert deliveries pkey change

# --- !Ups
ALTER TABLE Deliveries DROP COLUMN IF EXISTS id CASCADE;
ALTER TABLE Deliveries ADD COLUMN id bigserial PRIMARY KEY;
ALTER TABLE Deliveries ADD CONSTRAINT one_delivery_per_deliverer UNIQUE (delivererId);


# --- !Downs

ALTER TABLE Deliveries DROP CONSTRAINT one_delivery_per_deliverer;
