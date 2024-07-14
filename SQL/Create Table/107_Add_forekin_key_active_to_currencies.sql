ALTER TABLE currencies
ADD active_id BIGINT
REFERENCES actives;
COMMENT ON COLUMN currencies.active_id IS 'Klucz Obcy';
UPDATE currencies
   SET active_id = active.id
  FROM (SELECT * FROM actives ) AS active
 WHERE active.active_cod = currencies.active;