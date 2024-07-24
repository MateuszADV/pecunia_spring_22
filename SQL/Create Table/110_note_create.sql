CREATE SEQUENCE notes_sequence;
CREATE TABLE notes(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('notes_sequence'),
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
    making VARCHAR NOT NULL,
    quality VARCHAR NOT NULL,
    width INTEGER,
    height INTEGER,
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
COMMENT ON TABLE notes IS 'Note';
COMMENT ON COLUMN notes.id IS 'Klucz główny';
COMMENT ON COLUMN notes.currency_id IS 'Klucz obcy do currencies';
COMMENT ON COLUMN notes.denomination IS 'Nominał';
COMMENT ON COLUMN notes.bought IS 'Miejsce zakupu';
COMMENT ON COLUMN notes.date_buy IS 'Data zakupu';
COMMENT ON COLUMN notes.name_currency IS 'Nazwa waluty';
COMMENT ON COLUMN notes.series IS 'Seria';
COMMENT ON COLUMN notes.item_date IS 'Data, rocznik';
COMMENT ON COLUMN notes.quantity IS 'Ilość';
COMMENT ON COLUMN notes.unit_quantity IS 'Rodzaj jednostek ilości(szt, set...)';
COMMENT ON COLUMN notes.signature_code IS 'Kod sygnatury (obiegowy, wymienne, nie wymienne itp)';
COMMENT ON COLUMN notes.price_buy IS 'Cena zakupu';
COMMENT ON COLUMN notes.price_sell IS 'Cena sprzedaży';
COMMENT ON COLUMN notes.making IS 'Materiał z jakiego został wykonany banknot';
COMMENT ON COLUMN notes.quality IS 'Stan banknotu';
COMMENT ON COLUMN notes.width IS 'Szerokość banknotu';
COMMENT ON COLUMN notes.height IS 'Wysokość banknotu';
COMMENT ON COLUMN notes.status IS 'Status banknotu(Kolekcja, FOR SELL, SOLD itp.)';
COMMENT ON COLUMN notes.img_type IS 'Źródło obrazka (Skan, Foto, WWW itp.)';
COMMENT ON COLUMN notes.status_sell IS 'Status sprzedaży (wystawiony gdzie wystawiony)';
COMMENT ON COLUMN notes.visible IS 'Czy ma być widoczne dla wszystkich';
COMMENT ON COLUMN notes.avers_path IS 'Avers banknotu';
COMMENT ON COLUMN notes.reverse_path IS 'Revers banknotu';
COMMENT ON COLUMN notes.unit_currency IS 'Jednostka waluty "Synbol"';
COMMENT ON COLUMN notes.description IS 'Dodatkowy opis:';
COMMENT ON COLUMN notes.created_at IS 'Data dodania';
COMMENT ON COLUMN notes.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION notes_biu_trigfunc()
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
CREATE TRIGGER notes_biu
	BEFORE INSERT OR UPDATE ON notes
	FOR EACH ROW
EXECUTE PROCEDURE notes_biu_trigfunc();