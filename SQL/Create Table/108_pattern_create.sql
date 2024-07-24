CREATE SEQUENCE patterns_sequence;
CREATE TABLE patterns(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('patterns_sequence'),
	pattern VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
COMMENT ON TABLE patterns IS 'Pattern';
COMMENT ON COLUMN patterns.id IS 'Klucz główny';
COMMENT ON COLUMN patterns.pattern IS 'Rodzej';
COMMENT ON COLUMN patterns.name IS 'Nazwa';
COMMENT ON COLUMN patterns.description IS 'Dodatkowy opis:';
COMMENT ON COLUMN patterns.created_at IS 'Data dodania';
COMMENT ON COLUMN patterns.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION patterns_biu_trigfunc()
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
CREATE TRIGGER patterns_biu
	BEFORE INSERT OR UPDATE ON patterns
	FOR EACH ROW
EXECUTE PROCEDURE patterns_biu_trigfunc();