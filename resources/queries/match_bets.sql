-- name:new-match-bet<!
INSERT INTO "match_bets"(betor_id, match_id, home_score, away_score, amount)
VALUES(:current_user_id, :match_id, :home_score, :away_score, :amount)

-- name:get-match-bets-by-user
SELECT * FROM "match_bets" WHERE betor_id = :current_user_id