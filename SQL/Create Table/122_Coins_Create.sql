CREATE SEQUENCE coins_sequence;
CREATE TABLE coins(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('coins_sequence'),
	currency_id BIGINT NOT NULL REFERENCES currencies(id),
    denomination NUMERIC(20, 2) NOT NULL,
    bought VARCHAR NOT NULL,
    date_buy TIMESTAMP,
    name_currency VARCHAR,
    series VARCHAR,
    item_date VARCHAR,
    quantity INTEGER NOT NULL,
    unit_quantity VARCHAR NOT NULL,
    signature_code Integer NOT NULL,
    price_buy NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
	price_sell NUMERIC(8, 2) DEFAULT 0.00::numeric NOT NULL,
    quality VARCHAR NOT NULL,
	composition TEXT,
	diameter NUMERIC(8, 2) DEFAULT 0.00::numeric,
	thickness NUMERIC(8, 2) DEFAULT 0.00::numeric,
	weight NUMERIC(8, 2) DEFAULT 0.00::numeric,
    status VARCHAR NOT NULL,
    img_type VARCHAR NOT NULL,
    status_sell VARCHAR,
    visible BOOLEAN NOT NULL,
    unit_currency VARCHAR,
    avers_path VARCHAR(512) NOT NULL,
	reverse_path VARCHAR(512) NOT NULL,	
    description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
COMMENT ON TABLE coins IS 'Coin';
COMMENT ON COLUMN coins.id IS 'Klucz główny';
COMMENT ON COLUMN coins.currency_id IS 'Klucz obcy do currencies';
COMMENT ON COLUMN coins.denomination IS 'Nominał';
COMMENT ON COLUMN coins.bought IS 'Miejsce zakupu';
COMMENT ON COLUMN coins.date_buy IS 'Data zakupu';
COMMENT ON COLUMN coins.name_currency IS 'Nazwa waluty';
COMMENT ON COLUMN coins.series IS 'Seria';
COMMENT ON COLUMN coins.item_date IS 'Data, rocznik';
COMMENT ON COLUMN coins.quantity IS 'Ilość';
COMMENT ON COLUMN coins.unit_quantity IS 'Rodzaj jednostek ilości(szt, set...)';
COMMENT ON COLUMN coins.signature_code IS 'Kod sygnatury (obiegowy, wymienne, nie wymienne itp)';
COMMENT ON COLUMN coins.price_buy IS 'Cena zakupu';
COMMENT ON COLUMN coins.price_sell IS 'Cena sprzedaży';
COMMENT ON COLUMN coins.quality IS 'Stan monety';
COMMENT ON COLUMN coins.composition IS 'Skład monety';
COMMENT ON COLUMN coins.diameter IS 'Średnica monety';
COMMENT ON COLUMN coins.thickness IS 'Grubość monety';
COMMENT ON COLUMN coins.weight IS 'Waga monety';
COMMENT ON COLUMN coins.status IS 'Status banknotu(Kolekcja, FOR SELL, SOLD itp.)';
COMMENT ON COLUMN coins.img_type IS 'Źródło obrazka (Skan, Foto, WWW itp.)';
COMMENT ON COLUMN coins.status_sell IS 'Status sprzedaży (wystawiony gdzie wystawiony)';
COMMENT ON COLUMN coins.visible IS 'Czy ma być widoczne dla wszystkich';
COMMENT ON COLUMN coins.avers_path IS 'Avers banknotu';
COMMENT ON COLUMN coins.reverse_path IS 'Revers banknotu';
COMMENT ON COLUMN coins.unit_currency IS 'Jednostka waluty "Synbol"';
COMMENT ON COLUMN coins.description IS 'Dodatkowy opis:';
COMMENT ON COLUMN coins.created_at IS 'Data dodania';
COMMENT ON COLUMN coins.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION coins_biu_trigfunc()
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
CREATE TRIGGER coins_biu
	BEFORE INSERT OR UPDATE ON coins
	FOR EACH ROW
EXECUTE PROCEDURE coins_biu_trigfunc();