CREATE SEQUENCE orders_sequence;
CREATE TABLE orders(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('orders_sequence'),
	customer_id BIGINT NOT NULL REFERENCES customers(id),
    number_order VARCHAR NOT NULL,
    date_order TIMESTAMP,
	date_send_order TIMESTAMP,
	status_order_id BIGINT NOT NULL REFERENCES status_orders(id),
	tracking_number VARCHAR,
	shipping_type_id BIGINT NOT NULL REFERENCES shipping_types(id),
	shipping_cost NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE orders IS 'Rodzaje przesyłki';
COMMENT ON COLUMN orders.id IS 'Klucz główny';
COMMENT ON COLUMN orders.customer_id IS 'Klucz Obcy ID Klienta';
COMMENT ON COLUMN orders.number_order IS 'Numer zamówienia';
COMMENT ON COLUMN orders.date_order IS 'Data Przyjęcia zamówienia';
COMMENT ON COLUMN orders.date_send_order IS 'Data wysyłki zamówienia';
COMMENT ON COLUMN orders.status_order_id IS 'Klucz Obcy status zamówienia (Open, Close, reservation...)';
COMMENT ON COLUMN orders.tracking_number IS 'Numer przesyłki do śledzenia, jeśli to nie jest odbiór osobisty';
COMMENT ON COLUMN orders.shipping_type_id IS 'Klucz obcy Określa rodzaj przesyłki';
COMMENT ON COLUMN orders.shipping_cost IS 'Cena przesyłki';
COMMENT ON COLUMN orders.description IS 'Dodatkowy opis';
COMMENT ON COLUMN orders.created_at IS 'Data dodania';
COMMENT ON COLUMN orders.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION orders_biu_trigfunc()
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
CREATE TRIGGER orders_biu
    BEFORE INSERT OR UPDATE ON orders
    FOR EACH ROW
EXECUTE PROCEDURE orders_biu_trigfunc();