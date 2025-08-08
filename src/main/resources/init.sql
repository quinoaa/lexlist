PRAGMA foreign_keys=ON;

CREATE TABLE IF NOT EXISTS user_data (
  userid INTEGER PRIMARY KEY AUTOINCREMENT,
  username VARCHAR UNIQUE,
  password VARCHAR
);

CREATE TABLE IF NOT EXISTS user_session (
  token VARCHAR PRIMARY KEY,
  userid INTEGER,
  expire BIGINT,
  FOREIGN KEY (userid)
    REFERENCES user_data (userid)
    ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS dictionary (
  dictid INTEGER PRIMARY KEY AUTOINCREMENT,
  ownerid INTEGER,
  name VARCHAR,
  FOREIGN KEY (ownerid)
    REFERENCES user_data (userid)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS entry (
  name VARCHAR,
  dictid INTEGER,
  data TEXT,
  FOREIGN KEY(dictid)
    REFERENCES dictionary (dictid)
    ON DELETE CASCADE,
  PRIMARY KEY(name, dictid)
);






