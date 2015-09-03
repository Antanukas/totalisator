-- name: new-winner-bet<!
INSERT INTO "winner_bets"(betor_id,totalisator_team_id,amount) VALUES(:current_user_id, :totalisator_team_id, :amount)

-- name: get-winner-bets-by-id
SELECT * FROM "winner_bets" WHERE betor_id = :current_user_id

-- name: get-winner-bets-by-totalisator-id
SELECT wb.*, u.username from "winner_bets" wb, "teams" tm, "users" u
where totalisator_team_id = tm.id and tm.totalisator_id = :totalisator_id and u.id = wb.betor_id
