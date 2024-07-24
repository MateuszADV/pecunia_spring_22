CREATE SEQUENCE settings_sequence;
CREATE TABLE settings(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('settings_sequence'),
	name VARCHAR NOT NULL,
    setting VARCHAR NOT NULL,
    parameter VARCHAR,
	description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE settings IS 'Ustawienia aplikacji';
COMMENT ON COLUMN settings.id IS 'Klucz główny';
COMMENT ON COLUMN settings.name IS 'Nazwa ustawień';
COMMENT ON COLUMN settings.setting IS 'ustawienia applikacji';
COMMENT ON COLUMN settings.parameter IS 'Dostepne Parametry dla aplikacji';
COMMENT ON COLUMN settings.description IS 'Dodatkowy opis';
COMMENT ON COLUMN settings.created_at IS 'Data dodania';
COMMENT ON COLUMN settings.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION settings_biu_trigfunc()
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
CREATE TRIGGER settings_biu
	BEFORE INSERT OR UPDATE ON settings
	FOR EACH ROW
EXECUTE PROCEDURE settings_biu_trigfunc();