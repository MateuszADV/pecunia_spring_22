CREATE SEQUENCE currencies_sequence;
CREATE TABLE currencies(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('currencies_sequence'),
	country_id BIGINT NOT NULL REFERENCES countries(id),
	cod VARCHAR,
    pattern VARCHAR NOT NULL,
	currency VARCHAR NOT NULL,
	change VARCHAR,
	active INTEGER NOT NULL,
	data_exchange VARCHAR,
    currency_series VARCHAR,
	currency_from VARCHAR,
	converter VARCHAR,
	description TEXT,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
    
    
);
COMMENT ON TABLE currencies IS 'Waluty';
COMMENT ON COLUMN currencies.country_id IS 'Klucz główny';
COMMENT ON COLUMN currencies.country_id IS 'Klucz obcy';
COMMENT ON COLUMN currencies.pattern IS 'Rodzaj kolekcji banknoty, monety, obligacje...';
COMMENT ON COLUMN currencies.currency IS 'Nazwa waluty';
COMMENT ON COLUMN currencies.change IS 'Drobne wality';
COMMENT ON COLUMN currencies.active IS 'Czy dana waluta jest w obiegu, wymmienna, niewymmienna...';
COMMENT ON COLUMN currencies.data_exchange IS 'Data wymany waluty';
COMMENT ON COLUMN currencies.currency_series IS 'Seria walut danego kraju (Okres występowania danej waluty serii banknotów)';
COMMENT ON COLUMN currencies.currency_from IS 'Poprzednia waluta';
COMMENT ON COLUMN currencies.converter IS 'przelicznik waluty względej poprzedniej waluty';
COMMENT ON COLUMN currencies.description IS 'Dodatkowy opis waluty';
COMMENT ON COLUMN currencies.created_at IS 'Data dodania';
COMMENT ON COLUMN currencies.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION currencies_biu_trigfunc()
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
CREATE TRIGGER currencies_biu
	BEFORE INSERT OR UPDATE ON currencies
	FOR EACH ROW
EXECUTE PROCEDURE currencies_biu_trigfunc();