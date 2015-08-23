CREATE TABLE "teams" (
	id BIGSERIAL PRIMARY KEY NOT NULL ,
	name VARCHAR(256) NOT NULL,
	totalisator_id INT NOT NULL,

    CONSTRAINT TEAM_UNQ UNIQUE (name, totalisator_id),
	CONSTRAINT totalisator_teams_fk FOREIGN KEY(totalisator_id) REFERENCES "totalisators"(id) ON DELETE RESTRICT,
);