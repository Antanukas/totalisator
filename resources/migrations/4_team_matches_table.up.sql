CREATE TABLE "team_matches" (
	id BIGSERIAL PRIMARY KEY NOT NULL ,
	home_team INT NOT NULL ,
	away_team INT NOT NULL ,
    home_team_score INT,
    away_team_score INT,
    totalisator_id INT NOT NULL,
    match_date TIMESTAMP(6),
    created_by INT NOT NULL,

	CONSTRAINT totalisator_home_teams_fk FOREIGN KEY(home_team) REFERENCES "teams"(id) ON DELETE RESTRICT,
	CONSTRAINT totalisator_away_teams_fk FOREIGN KEY(away_team) REFERENCES "teams"(id) ON DELETE RESTRICT,
	CONSTRAINT totalisator_matches_fk FOREIGN KEY(totalisator_id) REFERENCES "totalisators"(id) ON DELETE RESTRICT,
	CONSTRAINT user_team_matches_fk FOREIGN KEY(created_by) REFERENCES "users"(id) ON DELETE RESTRICT
);