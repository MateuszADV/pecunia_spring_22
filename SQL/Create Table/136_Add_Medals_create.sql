CREATE SEQUENCE medals_sequence;
CREATE TABLE medals(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('medals_sequence'),
	currency_id BIGINT NOT NULL REFERENCES currencies(id),
    denomination DOUBLE PRECISION,
    date_buy DATE,
    name_currency VARCHAR(255),
    series VARCHAR(255),
    item_date VARCHAR(255),
    quantity INTEGER NOT NULL,
    unit_quantity VARCHAR(255) NOT NULL,
    price_buy DOUBLE PRECISION  DEFAULT 0.00::numeric NOT NULL,
	price_sell DOUBLE PRECISION  DEFAULT 0.00::numeric NOT NULL,
	composition VARCHAR(255),
	diameter DOUBLE PRECISION DEFAULT 0.00::numeric,
	thickness DOUBLE PRECISION  DEFAULT 0.00::numeric,
	weight DOUBLE PRECISION DEFAULT 0.00::numeric,
    status_sell VARCHAR(255),
    visible BOOLEAN NOT NULL,
    unit_currency VARCHAR(255),
    avers_path VARCHAR(4092) NOT NULL,
	reverse_path VARCHAR(4092) NOT NULL,	
    description VARCHAR(4092),
	bought_id BIGINT NOT NULL REFERENCES boughts(id),
    active_id BIGINT NOT NULL REFERENCES actives(id),
    quality_id BIGINT NOT NULL REFERENCES qualities(id),
    status_id BIGINT NOT NULL REFERENCES statuses(id),
    image_type_id BIGINT NOT NULL REFERENCES image_types(id),	
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
COMMENT ON TABLE medals IS 'Medal';
COMMENT ON COLUMN medals.id IS 'Klucz główny';
COMMENT ON COLUMN medals.currency_id IS 'Klucz obcy do currencies';
COMMENT ON COLUMN medals.denomination IS 'Nominał';
COMMENT ON COLUMN medals.date_buy IS 'Data zakupu';
COMMENT ON COLUMN medals.name_currency IS 'Nazwa waluty';
COMMENT ON COLUMN medals.series IS 'Seria';
COMMENT ON COLUMN medals.item_date IS 'Data, rocznik';
COMMENT ON COLUMN medals.quantity IS 'Ilość';
COMMENT ON COLUMN medals.unit_quantity IS 'Rodzaj jednostek ilości(szt, set...)';
COMMENT ON COLUMN medals.price_buy IS 'Cena zakupu';
COMMENT ON COLUMN medals.price_sell IS 'Cena sprzedaży';
COMMENT ON COLUMN medals.composition IS 'Skład medalu';
COMMENT ON COLUMN medals.diameter IS 'Średnica medalu';
COMMENT ON COLUMN medals.thickness IS 'Grubość medalu';
COMMENT ON COLUMN medals.weight IS 'Waga medalu';
COMMENT ON COLUMN medals.status_sell IS 'Status sprzedaży (wystawiony gdzie wystawiony)';
COMMENT ON COLUMN medals.visible IS 'Czy ma być widoczne dla wszystkich';
COMMENT ON COLUMN medals.avers_path IS 'Avers medalu';
COMMENT ON COLUMN medals.reverse_path IS 'Revers medalu';
COMMENT ON COLUMN medals.unit_currency IS 'Jednostka waluty "Synbol"';
COMMENT ON COLUMN medals.description IS 'Dodatkowy opis:';
COMMENT ON COLUMN medals.bought_id IS 'Klucz Obcy Miejsce Zakupu';
COMMENT ON COLUMN medals.active_id IS 'Klucz Obcy actibe';
COMMENT ON COLUMN medals.quality_id IS 'Klucz Obcy Stan Medalu';
COMMENT ON COLUMN medals.status_id IS 'Klucz Obcy ';
COMMENT ON COLUMN medals.image_type_id IS 'Klucz Obcy Typ Obrazka';
COMMENT ON COLUMN medals.created_at IS 'Data dodania';
COMMENT ON COLUMN medals.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION medals_biu_trigfunc()
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
CREATE TRIGGER medals_biu
	BEFORE INSERT OR UPDATE ON medals
	FOR EACH ROW
EXECUTE PROCEDURE medals_biu_trigfunc();