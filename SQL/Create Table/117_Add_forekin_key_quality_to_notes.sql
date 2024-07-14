ALTER TABLE notes
ADD quality_id BIGINT
REFERENCES qualities;
COMMENT ON COLUMN notes.quality_id IS 'Klucz Obcy';
UPDATE notes
   SET quality_id = quality.id
  FROM (SELECT * FROM qualities ) AS quality
 WHERE quality.quality = notes.quality;
 ALTER TABLE notes ALTER COLUMN quality_id SET NOT NULL;