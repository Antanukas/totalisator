CREATE TABLE "winner_bets" (
	id BIGSERIAL PRIMARY KEY NOT NULL ,
	betor_id INT NOT NULL ,
	totalisator_team_id INT NOT NULL ,
    amount DECIMAL NOT NULL ,

	CONSTRAINT WINNER_UNQ UNIQUE (betor_id, totalisator_team_id),
	CONSTRAINT win_bets_user_fk FOREIGN KEY(betor_id) REFERENCES "users"(id) ON DELETE RESTRICT,
	CONSTRAINT win_bets_teams_fk FOREIGN KEY(totalisator_team_id) REFERENCES "teams"(id) ON DELETE RESTRICT,
);