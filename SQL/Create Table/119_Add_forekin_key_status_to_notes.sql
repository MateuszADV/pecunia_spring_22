ALTER TABLE notes
ADD status_id BIGINT
REFERENCES statuses;
COMMENT ON COLUMN notes.status_id IS 'Klucz Obcy';
UPDATE notes
   SET status_id = status.id
  FROM (SELECT * FROM statuses ) AS status
 WHERE status.status = notes.status;
 ALTER TABLE notes ALTER COLUMN status_id SET NOT NULL;