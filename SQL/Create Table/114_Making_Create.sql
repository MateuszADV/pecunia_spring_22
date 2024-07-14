CREATE SEQUENCE makings_sequence;
CREATE TABLE makings(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('makings_sequence'),
    making VARCHAR NOT NULL,
    making_pl VARCHAR NOT NULL,
	description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE makings IS 'Materiał';
COMMENT ON COLUMN makings.id IS 'Klucz główny';
COMMENT ON COLUMN makings.making IS 'Materiał wykonania (EN)';
COMMENT ON COLUMN makings.making_pl IS 'MAteriał wykonania (PL)';
COMMENT ON COLUMN makings.description IS 'Dodatkowy opis';
COMMENT ON COLUMN makings.created_at IS 'Data dodania';
COMMENT ON COLUMN makings.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION makings_biu_trigfunc()
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
CREATE TRIGGER makings_biu
	BEFORE INSERT OR UPDATE ON makings
	FOR EACH ROW
EXECUTE PROCEDURE makings_biu_trigfunc();