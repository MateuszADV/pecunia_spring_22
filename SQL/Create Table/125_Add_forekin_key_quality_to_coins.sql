ALTER TABLE coins
    ADD quality_id BIGINT
        REFERENCES qualities;
COMMENT ON COLUMN coins.quality_id IS 'Klucz Obcy';
UPDATE coins
SET quality_id = quality.id
FROM (SELECT * FROM qualities ) AS quality
WHERE quality.quality = coins.quality;
ALTER TABLE coins ALTER COLUMN quality_id SET NOT NULL;