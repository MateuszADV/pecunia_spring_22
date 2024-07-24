CREATE SEQUENCE actives_sequence;
CREATE TABLE actives(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('actives_sequence'),
    active_cod INT NOT NULL,
    name VARCHAR NOT NULL,
	description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE actives IS 'Waluty';
COMMENT ON COLUMN actives.id IS 'Klucz główny';
COMMENT ON COLUMN actives.active_cod IS 'kod nuneryczny statusu waluty';
COMMENT ON COLUMN actives.name IS 'Status waluta, banknotu, monety... na obecny czas';
COMMENT ON COLUMN actives.description IS 'Dodatkowy opis';
COMMENT ON COLUMN actives.created_at IS 'Data dodania';
COMMENT ON COLUMN actives.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION actives_biu_trigfunc()
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
CREATE TRIGGER actives_biu
	BEFORE INSERT OR UPDATE ON actives
	FOR EACH ROW
EXECUTE PROCEDURE actives_biu_trigfunc();