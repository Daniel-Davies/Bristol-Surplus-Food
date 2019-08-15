# FoodItems schema

# --- !Ups

ALTER TABLE Users ADD CONSTRAINT unique_emails UNIQUE (email);
ALTER TABLE Users ADD COLUMN linkedid varchar(255);
ALTER TABLE Users ADD COLUMN serializedprofile varchar(10000);
ALTER TABLE Users ADD COLUMN address varchar(255);

# --- !Downs

ALTER TABLE Users DROP CONSTRAINT unique_emails;
ALTER TABLE Users DROP COLUMN linkedid;
ALTER TABLE Users DROP COLUMN serializedprofile;
ALTER TABLE Users DROP COLUMN address;

