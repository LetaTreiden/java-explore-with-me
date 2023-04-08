CREATE TABLE IF NOT EXISTS stats (
  id int8 GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  app varchar,
  uri varchar,
  ip varchar,
  timestamp timestamp,
  id_hit int8 unique
);