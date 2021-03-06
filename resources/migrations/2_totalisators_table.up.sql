CREATE TABLE "totalisators" (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	created_by INT NOT NULL,
	name VARCHAR(256) NOT NULL,
	description VARCHAR(256),

	CONSTRAINT TOTALISATOR_UNQ UNIQUE (name),
	CONSTRAINT totalisator_user_fk FOREIGN KEY(created_by) REFERENCES "users"(id) ON DELETE RESTRICT
);

INSERT INTO "totalisators" VALUES(1, 1, 'Baltic Basket 2015', 'Basketball championship for Baltic countries');