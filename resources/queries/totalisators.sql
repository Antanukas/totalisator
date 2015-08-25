-- name: get-totalisators
SELECT * from "totalisators"

-- name: get-totalisator-by-id
SELECT * from "totalisators" where id = :id

-- name: save-totalisator<!
INSERT INTO "totalisators"(created_by,name,description,winner_count)
VALUES(:current_user_id,:name,:description,:winner_count)

-- name: update-totalisator<!
UPDATE "totalisators" t
SET name = :name, description = :description, winner_count = :winner_count
WHERE id = :id AND t.created_by = :current_user_id