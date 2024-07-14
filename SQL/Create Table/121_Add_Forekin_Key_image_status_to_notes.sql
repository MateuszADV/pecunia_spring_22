ALTER TABLE notes
ADD image_type_id BIGINT
REFERENCES image_types;
COMMENT ON COLUMN notes.image_type_id IS 'Klucz Obcy';
UPDATE notes
   SET image_type_id = image_type.id
  FROM (SELECT * FROM image_types ) AS image_type
 WHERE image_type.type = notes.img_type;
 ALTER TABLE notes ALTER COLUMN image_type_id SET NOT NULL;