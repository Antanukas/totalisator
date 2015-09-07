CREATE TABLE "winner_bets" (
	id BIGSERIAL PRIMARY KEY NOT NULL ,
	betor_id INT NOT NULL ,
	totalisator_team_id INT NOT NULL ,
    amount DECIMAL NOT NULL ,

	CONSTRAINT win_bets_user_fk FOREIGN KEY(betor_id) REFERENCES "users"(id) ON DELETE RESTRICT,
	CONSTRAINT win_bets_teams_fk FOREIGN KEY(totalisator_team_id) REFERENCES "teams"(id) ON DELETE RESTRICT,
);

INSERT INTO "winner_bets" VALUES(1, 1, 3, 10);
INSERT INTO "winner_bets" VALUES(2, 2, 1, 5.5);
INSERT INTO "winner_bets" VALUES(3, 1, 1, 3);
INSERT INTO "winner_bets" VALUES(4, 3, 2, 7.30);
INSERT INTO "winner_bets" VALUES(5, 4, 2, 2);
INSERT INTO "winner_bets" VALUES(6, 5, 3, 4);