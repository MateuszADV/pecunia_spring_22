ALTER TABLE coins
    ADD active_id BIGINT
        REFERENCES actives;
COMMENT ON COLUMN coins.active_id IS 'Klucz Obcy';
UPDATE coins
SET active_id = active.id
FROM (SELECT * FROM actives ) AS active
WHERE active.active_cod = coins.signature_code;
ALTER TABLE coins ALTER COLUMN active_id SET NOT NULL;