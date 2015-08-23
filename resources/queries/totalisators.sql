-- name: totalisators
SELECT * from "totalisators"

-- name: totalisator-by-id
SELECT * from "totalisators" where id = ?

-- name: save-totalisator<!
INSERT INTO "totalisators"(user_id,name,description,winner_count) VALUES(?,?,?,?)