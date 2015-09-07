CREATE TABLE "teams" (
	id BIGSERIAL PRIMARY KEY NOT NULL ,
	name VARCHAR(256) NOT NULL,
	totalisator_id INT NOT NULL,
	created_by INT NOT NULL,

    CONSTRAINT TEAM_UNQ UNIQUE (name, totalisator_id),
	CONSTRAINT totalisator_teams_fk FOREIGN KEY(totalisator_id) REFERENCES "totalisators"(id) ON DELETE RESTRICT,
	CONSTRAINT user_teams_fk FOREIGN KEY(created_by) REFERENCES "users"(id) ON DELETE RESTRICT,
);

INSERT INTO "teams" VALUES(1, 'Latvia', 1, 1);
INSERT INTO "teams" VALUES(2, 'Estonia', 1, 1);
INSERT INTO "teams" VALUES(3, 'Lithuania', 1, 1);
