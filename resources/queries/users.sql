-- name: get-users
SELECT * FROM "users"

-- name: save-user<!
INSERT INTO "users"(name) VALUES(:name)

-- name: update-user<!
UPDATE "users" SET name = :name WHERE id = :id