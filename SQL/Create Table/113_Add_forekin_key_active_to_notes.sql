ALTER TABLE notes
ADD active_id BIGINT
REFERENCES actives;
COMMENT ON COLUMN notes.active_id IS 'Klucz Obcy';
UPDATE notes
   SET active_id = active.id
  FROM (SELECT * FROM actives ) AS active
 WHERE active.active_cod = notes.signature_code;
 ALTER TABLE notes ALTER COLUMN active_id SET NOT NULL;