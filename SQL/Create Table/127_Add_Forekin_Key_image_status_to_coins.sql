ALTER TABLE coins
    ADD image_type_id BIGINT
        REFERENCES image_types;
COMMENT ON COLUMN coins.image_type_id IS 'Klucz Obcy';
UPDATE coins
SET image_type_id = image_type.id
FROM (SELECT * FROM image_types ) AS image_type
WHERE image_type.type = coins.img_type;
ALTER TABLE coins ALTER COLUMN image_type_id SET NOT NULL;