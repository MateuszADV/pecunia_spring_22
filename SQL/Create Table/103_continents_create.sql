CREATE SEQUENCE continents_sequence;
CREATE TABLE continents(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('continents_sequence'),
	continent_en VARCHAR NOT NULL,
    continent_pl VARCHAR NOT NULL,
    continent_code VARCHAR NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
COMMENT ON TABLE continents IS 'Kontynenty';
COMMENT ON COLUMN continents.id IS 'Klucz główny';
COMMENT ON COLUMN continents.continent_en IS 'Nazwa kontynentu po angielsku';
COMMENT ON COLUMN continents.continent_pl IS 'Nazwa kontynentu po polsku';
COMMENT ON COLUMN continents.continent_code IS 'Kod kontynentu';

CREATE OR REPLACE FUNCTION continents_biu_trigfunc()
	RETURNS TRIGGER
AS $$
BEGIN
	IF (TG_OP = 'INSERT') THEN
		NEW.created_at := CURRENT_TIMESTAMP;
	END IF;
	    NEW.updated_at := CURRENT_TIMESTAMP;

RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';
CREATE TRIGGER continents_biu
	BEFORE INSERT OR UPDATE ON continents
	FOR EACH ROW
EXECUTE PROCEDURE continents_biu_trigfunc();