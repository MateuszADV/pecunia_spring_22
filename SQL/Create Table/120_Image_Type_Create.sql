CREATE SEQUENCE image_types_sequence;
CREATE TABLE image_types(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('image_types_sequence'),
    type VARCHAR NOT NULL,
    type_pl VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE image_types IS 'Status Itemu';
COMMENT ON COLUMN image_types.id IS 'Klucz główny';
COMMENT ON COLUMN image_types.type IS 'Rodzej obrazka (EN)';
COMMENT ON COLUMN image_types.type_pl IS 'Rodzaj obrazka (PL)';
COMMENT ON COLUMN image_types.description IS 'Dodatkowy opis';
COMMENT ON COLUMN image_types.created_at IS 'Data dodania';
COMMENT ON COLUMN image_types.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION image_types_biu_trigfunc()
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
CREATE TRIGGER image_types_biu
    BEFORE INSERT OR UPDATE ON image_types
    FOR EACH ROW
EXECUTE PROCEDURE image_types_biu_trigfunc();