CREATE SEQUENCE shipping_types_sequence;
CREATE TABLE shipping_types(
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('shipping_types_sequence'),
    shipping_type_en VARCHAR NOT NULL,
    shipping_type_pl VARCHAR NOT NULL,
	shipping_cost NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL

);
COMMENT ON TABLE shipping_types IS 'Rodzaje przesyłki';
COMMENT ON COLUMN shipping_types.id IS 'Klucz główny';
COMMENT ON COLUMN shipping_types.shipping_type_en IS 'Rodzaj przesyłki En';
COMMENT ON COLUMN shipping_types.shipping_type_pl IS 'Rodzaj przesyłki PL';
COMMENT ON COLUMN shipping_types.shipping_cost IS 'Cena przesyłki';
COMMENT ON COLUMN shipping_types.description IS 'Dodatkowy opis';
COMMENT ON COLUMN shipping_types.created_at IS 'Data dodania';
COMMENT ON COLUMN shipping_types.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION shipping_types_biu_trigfunc()
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
CREATE TRIGGER shipping_types_biu
    BEFORE INSERT OR UPDATE ON shipping_types
    FOR EACH ROW
EXECUTE PROCEDURE shipping_types_biu_trigfunc();