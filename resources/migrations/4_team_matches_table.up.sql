CREATE TABLE "team_matches" (
	id BIGSERIAL PRIMARY KEY NOT NULL ,
	home_team INT NOT NULL ,
	away_team INT NOT NULL ,
    home_team_score INT,
    away_team_score INT,
    match_date TIMESTAMP(6),

	CONSTRAINT totalisator_home_teams_fk FOREIGN KEY(home_team) REFERENCES "teams"(id) ON DELETE RESTRICT,
	CONSTRAINT totalisator_away_teams_fk FOREIGN KEY(away_team) REFERENCES "teams"(id) ON DELETE RESTRICT,
);