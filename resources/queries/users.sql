-- name: users
SELECT * FROM "users"

-- name: users-by-name
SELECT * FROM "users" where name = ?

-- name: save-user<!
INSERT INTO "users"(name) VALUES(?)