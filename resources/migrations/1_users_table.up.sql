CREATE TABLE "users" (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(256) NOT NULL,

	CONSTRAINT USER_UNQ UNIQUE (name)
);