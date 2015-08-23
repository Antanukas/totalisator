CREATE TABLE "totalisators" (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	user_id INT NOT NULL,
	name VARCHAR(256) NOT NULL,
	description VARCHAR(256),
	winner_count INT NOT NULL,

	CONSTRAINT TOTALISATOR_UNQ UNIQUE (name),
	CONSTRAINT totalisator_user_fk FOREIGN KEY(user_id) REFERENCES "users"(id) ON DELETE RESTRICT
);

INSERT INTO "totalisators"
VALUES(1, 1, 'Tieto Men Eurobasket 2015 totalisator', 'Men Basketbal tournament', 3)