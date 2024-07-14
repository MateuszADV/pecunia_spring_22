CREATE SEQUENCE countries_sequence;
CREATE TABLE countries(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('countries_sequence'),
	continent VARCHAR NOT NULL,
	country_en VARCHAR NOT NULL,
	country_pl VARCHAR NOT NULL,
	capital_city VARCHAR NOT NULL,
	alfa_2 VARCHAR,
	alfa_3 VARCHAR,
	numeric_code VARCHAR,
	iso_code VARCHAR,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	exists BOOLEAN,
	description TEXT
);
COMMENT ON TABLE countries IS 'Państwa swiata';
COMMENT ON COLUMN countries.id IS 'Klucz główny';
COMMENT ON COLUMN countries.country_en IS 'Nazwa państwa po angielsku';
COMMENT ON COLUMN countries.country_pl IS 'Nazwa państwa po polsku';
COMMENT ON COLUMN countries.continent IS 'Kontynent na kórym lezy państwo';
COMMENT ON COLUMN countries.capital_city IS 'Stolica państwa' ;
COMMENT ON COLUMN countries.alfa_2 IS 'część standardu ISO 3166-1, zawiera dwuliterowe kody państw oraz terytoriów';
COMMENT ON COLUMN countries.alfa_3 IS'część standardu ISO 3166-1, zawiera trzyliterowe kody państw, które są wizualnie łatwiejsze w rozpoznawaniu krajów';
COMMENT ON COLUMN countries.numeric_code IS 'trzycyfrowe kody państw' ;
COMMENT ON COLUMN countries.iso_code IS ' kodowanie nazw państw, terytoriów zależnych oraz jednostek ich podziałów administracyjnych' ;
COMMENT ON COLUMN countries.created_at IS 'Data dodania';
COMMENT ON COLUMN countries.updated_at IS 'Data modyfikacji';
COMMENT ON COLUMN countries.exists IS 'Czy dane państwo jescze istnieje';
COMMENT ON COLUMN countries.description IS 'Dodatkowy opis';

CREATE OR REPLACE FUNCTION countries_biu_trigfunc()
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
CREATE TRIGGER countries_biu
	BEFORE INSERT OR UPDATE ON countries
	FOR EACH ROW
EXECUTE PROCEDURE countries_biu_trigfunc();