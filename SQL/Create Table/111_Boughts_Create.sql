CREATE SEQUENCE boughts_sequence;
CREATE TABLE boughts(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('boughts_sequence'),
    name VARCHAR NOT NULL,
    full_name VARCHAR,
	description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE boughts IS 'Miejsce zakupu';
COMMENT ON COLUMN boughts.id IS 'Klucz główny';
COMMENT ON COLUMN boughts.name IS 'Skrucona nazawa miejsca zakupu';
COMMENT ON COLUMN boughts.full_name IS 'Pełna naza sprzedającgo';
COMMENT ON COLUMN boughts.description IS 'Dodatkowy opis';
COMMENT ON COLUMN boughts.created_at IS 'Data dodania';
COMMENT ON COLUMN boughts.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION boughts_biu_trigfunc()
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
CREATE TRIGGER boughts_biu
	BEFORE INSERT OR UPDATE ON boughts
	FOR EACH ROW
EXECUTE PROCEDURE boughts_biu_trigfunc();