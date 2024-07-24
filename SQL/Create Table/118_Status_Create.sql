CREATE SEQUENCE statuses_sequence;
CREATE TABLE statuses(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('statuses_sequence'),
    status VARCHAR NOT NULL,
    status_pl VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE statuses IS 'Status Itemu';
COMMENT ON COLUMN statuses.id IS 'Klucz główny';
COMMENT ON COLUMN statuses.status IS 'Satus danego itemu (EN)';
COMMENT ON COLUMN statuses.status_pl IS 'Status danego itemu (PL)';
COMMENT ON COLUMN statuses.description IS 'Dodatkowy opis';
COMMENT ON COLUMN statuses.created_at IS 'Data dodania';
COMMENT ON COLUMN statuses.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION statuses_biu_trigfunc()
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
CREATE TRIGGER statuses_biu
    BEFORE INSERT OR UPDATE ON statuses
    FOR EACH ROW
EXECUTE PROCEDURE statuses_biu_trigfunc();