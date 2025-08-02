DROP TABLE IF EXISTS game_tester;
DROP TABLE IF EXISTS tester;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS publisher;

CREATE TABLE publisher (
	publisher_id int NOT NULL AUTO_INCREMENT,
	publisher_name varchar(50) NOT NULL,
	publisher_email varchar(256) NOT NULL,
	publisher_phone varchar(30) NOT NULL,
	location varchar(40),
	rating decimal(2, 1),
	PRIMARY KEY (publisher_id)
);

CREATE TABLE game (
	game_id int NOT NULL AUTO_INCREMENT,
	publisher_id int NOT NULL,
	game_name varchar(60) NOT NULL,
	genre varchar(60),
	platforms varchar(128),
	PRIMARY KEY(game_id),
	FOREIGN KEY (publisher_id) REFERENCES publisher (publisher_id) ON DELETE CASCADE
);

CREATE TABLE tester (
	tester_id int NOT NULL AUTO_INCREMENT,
	tester_name varchar(128),
	tester_email varchar(256),
	tester_phone varchar(30),
	PRIMARY KEY (tester_id)
);

CREATE TABLE game_tester (
	game_id int NOT NULL,
	tester_id int NOT NULL,
	FOREIGN KEY (game_id) REFERENCES game (game_id) ON DELETE CASCADE,
	FOREIGN KEY (tester_id) REFERENCES tester (tester_id) ON DELETE CASCADE
);