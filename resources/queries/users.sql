-- name: get-users
SELECT id, username FROM "users"

-- name: save-user<!
INSERT INTO "users"(username, password) VALUES(:username, :password)

-- name: update-user<!
UPDATE "users" SET username = :usernamename, password = :password WHERE id = :id

-- name: get-user-by-name
SELECT * from "users" where username = :username
