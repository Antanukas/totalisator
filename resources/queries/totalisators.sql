-- name: get-totalisators
SELECT * from "totalisators"

-- name: get-totalisator-by-id
SELECT * from "totalisators" where id = :id

-- name: save-totalisator<!
INSERT INTO "totalisators"(created_by,name,description)
VALUES(:created_by,:name,:description)

-- name: update-totalisator<!
UPDATE "totalisators" t
SET name = :name, description = :description
WHERE id = :id AND t.created_by = :current_user_id