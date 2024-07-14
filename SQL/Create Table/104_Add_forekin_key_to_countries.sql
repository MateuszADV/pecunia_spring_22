ALTER TABLE countries
ADD continent_id BIGINT
REFERENCES continents;
COMMENT ON COLUMN countries.continent_id IS 'Klucz Obcy';
UPDATE countries
   SET continent_id = continent.id
  FROM (SELECT * FROM continents ) AS continent
 WHERE countries.continent = continent.continent_pl;