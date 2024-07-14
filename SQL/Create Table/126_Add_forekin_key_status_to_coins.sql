ALTER TABLE coins
    ADD status_id BIGINT
        REFERENCES statuses;
COMMENT ON COLUMN coins.status_id IS 'Klucz Obcy';
UPDATE coins
SET status_id = status.id
FROM (SELECT * FROM statuses ) AS status
WHERE status.status = coins.status;
ALTER TABLE coins ALTER COLUMN status_id SET NOT NULL;