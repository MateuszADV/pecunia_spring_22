ALTER TABLE notes
ADD bought_id BIGINT
REFERENCES boughts;
COMMENT ON COLUMN notes.bought_id IS 'Klucz Obcy';
UPDATE notes
   SET bought_id = bought.id
  FROM (SELECT * FROM boughts ) AS bought
 WHERE bought.name = notes.bought;
 ALTER TABLE notes ALTER COLUMN bought_id SET NOT NULL;