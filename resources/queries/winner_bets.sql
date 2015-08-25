-- name: new-winner-bet<!
INSERT INTO "winner_bets"(betor_id,totalisator_team_id,amount) VALUES(:current_user_id, :totalisator_team_id, :amount)

-- name: get-winner-bets-by-id
SELECT * FROM "winner_bets" WHERE betor_id = :current_user_id