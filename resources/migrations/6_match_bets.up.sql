CREATE TABLE "match_bets" (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	user_id INT NOT NULL,
	match_id INT NOT NULL,
	home_score INT NOT NULL,
	away_score INT NOT NULL,
    amount DECIMAL NOT NULL,

	CONSTRAINT MATCHES_UNQ UNIQUE (user_id, match_id),
	CONSTRAINT match_bets_user_fk FOREIGN KEY(user_id) REFERENCES "users"(id) ON DELETE RESTRICT,
	CONSTRAINT match_bets_teams_fk FOREIGN KEY(match_id) REFERENCES "team_matches"(id) ON DELETE RESTRICT,
);