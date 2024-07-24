ALTER TABLE notes
ADD making_id BIGINT
REFERENCES makings;
COMMENT ON COLUMN notes.making_id IS 'Klucz Obcy';
UPDATE notes
   SET making_id = making.id
  FROM (SELECT * FROM makings ) AS making
 WHERE making.making = notes.making;
 ALTER TABLE notes ALTER COLUMN making_id SET NOT NULL;