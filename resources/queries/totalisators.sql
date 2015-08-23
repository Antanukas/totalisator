-- name: totalisators
SELECT * from "totalisators"

-- name: totalisator-by-id
SELECT * from "totalisators" where id = :id

-- name: save-totalisator<!
INSERT INTO "totalisators"(user_id,name,description,winner_count)
VALUES(:user_id,:name,:description,:winner_count)

-- name: update-totalisator<!
UPDATE "totalisators"
SET user_id = :user_id,name = :name, description = :description, winner_count = :winner_count
WHERE id = :id