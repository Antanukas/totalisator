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
