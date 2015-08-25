-- name: save-match<!
INSERT INTO "team_matches"(home_team, away_team, home_team_score, away_team_score, match_date, totalisator_id, created_by)
VALUES(:home_team,:away_team,:home_team_score,:away_team_score,:match_date,:totalisator_id, :current_user_id)

-- name: update-match!
UPDATE "team_matches"
SET home_team = :home_team, away_team = :away_team, home_team_score = :home_team_score, away_team_score = :away_team_score, match_date = :match_date
WHERE id = :id

-- name: get-matches
SELECT * FROM "team_matches" where totalisator_id = :totalisator_id