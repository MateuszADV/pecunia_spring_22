CREATE SEQUENCE status_orders_sequence;
CREATE TABLE status_orders(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('status_orders_sequence'),
    status_en VARCHAR NOT NULL,
    status_pl VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE status_orders IS 'Status Zamówienia';
COMMENT ON COLUMN status_orders.id IS 'Klucz główny';
COMMENT ON COLUMN status_orders.status_en IS 'Status zamówienia En';
COMMENT ON COLUMN status_orders.status_pl IS 'Status Zamówienia PL';
COMMENT ON COLUMN status_orders.description IS 'Dodatkowy opis';
COMMENT ON COLUMN status_orders.created_at IS 'Data dodania';
COMMENT ON COLUMN status_orders.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION status_orders_biu_trigfunc()
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
CREATE TRIGGER status_orders_biu
    BEFORE INSERT OR UPDATE ON status_orders
    FOR EACH ROW
EXECUTE PROCEDURE status_orders_biu_trigfunc();