ALTER TABLE coins
    ADD bought_id BIGINT
        REFERENCES boughts;
COMMENT ON COLUMN coins.bought_id IS 'Klucz Obcy';
UPDATE coins
SET bought_id = bought.id
FROM (SELECT * FROM boughts ) AS bought
WHERE bought.name = coins.bought;
ALTER TABLE coins ALTER COLUMN bought_id SET NOT NULL;