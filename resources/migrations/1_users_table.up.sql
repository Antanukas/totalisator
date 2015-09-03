CREATE TABLE "users" (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	username VARCHAR(256) NOT NULL,
	password VARCHAR(50) NOT NULL,

	CONSTRAINT USER_UNQ UNIQUE (username)
);

INSERT INTO "users" VALUES(1, 'Antanas', 'clojure');
INSERT INTO "users" VALUES(2, 'Test1', 'test');
INSERT INTO "users" VALUES(3, 'Test2', 'test');
INSERT INTO "users" VALUES(4, 'Test3', 'test');
INSERT INTO "users" VALUES(5, 'Test4', 'test');