CREATE SEQUENCE SQ_ADDRESS START WITH 1
CREATE TABLE ADDRESS("ID" INTEGER not null primary key, "STREET" VARCHAR(50), "NUMBER" VARCHAR(20), "ZIPCODE" VARCHAR(10), "CITY" VARCHAR(50), "STATE" VARCHAR(50), "NEIGHBORHOOD" VARCHAR(50), "COMPLEMENT" VARCHAR(500))
