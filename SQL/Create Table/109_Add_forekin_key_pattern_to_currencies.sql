ALTER TABLE currencies
ADD pattern_id BIGINT
REFERENCES patterns;
COMMENT ON COLUMN currencies.pattern_id IS 'Klucz Obcy';
UPDATE currencies
   SET pattern_id = pattern.id
  FROM (SELECT * FROM patterns ) AS pattern
 WHERE pattern.pattern = currencies.pattern;