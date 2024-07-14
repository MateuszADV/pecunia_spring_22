CREATE SEQUENCE securities_sequence;
CREATE TABLE securities(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('securities_sequence'),
	currency_id BIGINT NOT NULL REFERENCES currencies(id),
    denomination NUMERIC(20, 2) NOT NULL,
    date_buy TIMESTAMP,
    name_currency VARCHAR,
    series VARCHAR,
    item_date VARCHAR,
    quantity INTEGER NOT NULL,
    unit_quantity VARCHAR NOT NULL,
    price_buy NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
	price_sell NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
	width INTEGER,
    height INTEGER,
    status_sell VARCHAR,
    visible BOOLEAN NOT NULL,
    unit_currency VARCHAR,
    avers_path VARCHAR(512) NOT NULL,
	reverse_path VARCHAR(512) NOT NULL,	
    description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
    bought_id BIGINT NOT NULL REFERENCES boughts(id),
    active_id BIGINT NOT NULL REFERENCES actives(id),
	making_id BIGINT NOT NULL REFERENCES makings(id),
    quality_id BIGINT NOT NULL REFERENCES qualities(id),
    status_id BIGINT NOT NULL REFERENCES statuses(id),
    image_type_id BIGINT NOT NULL REFERENCES image_types(id)

);
COMMENT ON TABLE securities IS 'Securitie';
COMMENT ON COLUMN securities.id IS 'Klucz główny';
COMMENT ON COLUMN securities.currency_id IS 'Klucz obcy do currencies';
COMMENT ON COLUMN securities.denomination IS 'Nominał';
COMMENT ON COLUMN securities.date_buy IS 'Data zakupu';
COMMENT ON COLUMN securities.name_currency IS 'Nazwa waluty';
COMMENT ON COLUMN securities.series IS 'Seria';
COMMENT ON COLUMN securities.item_date IS 'Data, rocznik';
COMMENT ON COLUMN securities.quantity IS 'Ilość';
COMMENT ON COLUMN securities.unit_quantity IS 'Rodzaj jednostek ilości(szt, set...)';
COMMENT ON COLUMN securities.price_buy IS 'Cena zakupu';
COMMENT ON COLUMN securities.price_sell IS 'Cena sprzedaży';
COMMENT ON COLUMN securities.width IS 'Szerokość';
COMMENT ON COLUMN securities.height IS 'wysokość';
COMMENT ON COLUMN securities.status_sell IS 'Status sprzedaży (wystawiony gdzie wystawiony)';
COMMENT ON COLUMN securities.visible IS 'Czy ma być widoczne dla wszystkich';
COMMENT ON COLUMN securities.avers_path IS 'Avers banknotu';
COMMENT ON COLUMN securities.reverse_path IS 'Revers banknotu';
COMMENT ON COLUMN securities.unit_currency IS 'Jednostka waluty "Synbol"';
COMMENT ON COLUMN securities.description IS 'Dodatkowy opis:';
COMMENT ON COLUMN securities.created_at IS 'Data dodania';
COMMENT ON COLUMN securities.updated_at IS 'Data modyfikacji';
COMMENT ON COLUMN securities.bought_id IS 'Klucz Obcy Miejsce Zakupu';
COMMENT ON COLUMN securities.active_id IS 'Klucz Obcy actibe';
COMMENT ON COLUMN securities.making_id IS 'Klucz Obcy rodzaj materiału wykonania elementu';
COMMENT ON COLUMN securities.quality_id IS 'Klucz Obcy Stan Monety';
COMMENT ON COLUMN securities.status_id IS 'Klucz Obcy ';
COMMENT ON COLUMN securities.image_type_id IS 'Klucz Obcy Typ Obrazka';

CREATE OR REPLACE FUNCTION securities_biu_trigfunc()
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
CREATE TRIGGER securities_biu
	BEFORE INSERT OR UPDATE ON securities
	FOR EACH ROW
EXECUTE PROCEDURE securities_biu_trigfunc();