CREATE SEQUENCE order_items_sequence;
CREATE TABLE order_items(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('order_items_sequence'),
	order_id BIGINT NOT NULL REFERENCES orders(id),
    pattern VARCHAR NOT NULL,
	quantity INTEGER NOT NULL,
	unit_quantity VARCHAR NOT NULL,
	final_price NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
	note_id BIGINT REFERENCES notes(id),
	coin_id BIGINT REFERENCES coins(id),
	security_id BIGINT REFERENCES securities(id),
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE order_items IS 'Rodzaje przesyłki';
COMMENT ON COLUMN order_items.id IS 'Klucz główny';
COMMENT ON COLUMN order_items.pattern IS 'Określa rosdzaj elementu, NOTE, COIN...';
COMMENT ON COLUMN order_items.quantity IS 'Ilość sprzedawanych elementów';
COMMENT ON COLUMN order_items.unit_quantity IS 'Szt Set';
COMMENT ON COLUMN order_items.final_price IS 'Finalna cena sprzedaży';
COMMENT ON COLUMN order_items.note_id IS 'Klucz Obcy Note';
COMMENT ON COLUMN order_items.coin_id IS 'Klucz Obcy Coin';
COMMENT ON COLUMN order_items.security_id IS 'Klucz Obcy Security';
COMMENT ON COLUMN order_items.created_at IS 'Data dodania';
COMMENT ON COLUMN order_items.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION order_items_biu_trigfunc()
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
CREATE TRIGGER order_items_biu
    BEFORE INSERT OR UPDATE ON order_items
    FOR EACH ROW
EXECUTE PROCEDURE order_items_biu_trigfunc();