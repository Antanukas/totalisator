-- name: get-teams
SELECT * from "teams" WHERE totalisator_id = :totalisator_id

--name: get-team
SELECT * from "teams" WHERE totalisator_id = :totalisator_id AND id = :team_id

-- name: save-team<!
INSERT INTO "teams"(name, totalisator_id, created_by) VALUES(:name,:totalisator_id,:created_by)

-- name: update-team!
UPDATE "teams" tm
SET name = :name, totalisator_id = :totalisator_id
WHERE tm.id = :id

-- name: get-teams-with-bets-raw
SELECT tm.id as team_id, tm.*, wb.id as bet_id, wb.*, u.username
from "teams" tm LEFT JOIN "winner_bets" wb ON tm.id = wb.totalisator_team_id LEFT JOIN "users" u ON u.id = wb.betor_id
where totalisator_id = :totalisator_id
