CREATE SCHEMA TEST AUTHORIZATION DBA;

CREATE TABLE TEST.t_foo (
    id INTEGER IDENTITY PRIMARY KEY,
    name VARCHAR(32),
    size INTEGER
);

CREATE TABLE TEST.t_bar (
    id INTEGER IDENTITY NOT NULL ,
    name VARCHAR(32) NOT NULL,
    size INTEGER NOT NULL,
    PRIMARY KEY(id)
);