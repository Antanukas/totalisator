-- name: users
SELECT * FROM "users"

-- name: users-by-name
SELECT * FROM "users" where name = :name

-- name: save-user<!
INSERT INTO "users"(name) VALUES(:name)

-- name: update-user<!
UPDATE "users" SET name = :name WHERE id = :id