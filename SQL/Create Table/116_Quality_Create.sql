CREATE SEQUENCE qualities_sequence;
CREATE TABLE qualities(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('qualities_sequence'),
    quality VARCHAR NOT NULL,
    quality_pl VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE qualities IS 'Stan Itemów';
COMMENT ON COLUMN qualities.id IS 'Klucz główny';
COMMENT ON COLUMN qualities.quality IS 'Stan danego itemu (EN)';
COMMENT ON COLUMN qualities.quality_pl IS 'Stan danego itemu (PL)';
COMMENT ON COLUMN qualities.description IS 'Dodatkowy opis';
COMMENT ON COLUMN qualities.created_at IS 'Data dodania';
COMMENT ON COLUMN qualities.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION qualities_biu_trigfunc()
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
CREATE TRIGGER qualities_biu
    BEFORE INSERT OR UPDATE ON qualities
    FOR EACH ROW
EXECUTE PROCEDURE qualities_biu_trigfunc();