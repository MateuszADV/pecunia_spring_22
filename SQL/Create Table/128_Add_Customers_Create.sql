CREATE SEQUENCE customers_sequence;
CREATE TABLE customers(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('customers_sequence'),
	unique_id VARCHAR NOT NULL,
	active BOOLEAN NOT NULL,
	name VARCHAR,
	last_name VARCHAR,
	city VARCHAR,
	zip_code VARCHAR,
	street VARCHAR,
	number VARCHAR,
	email VARCHAR,
	nick VARCHAR,
	phone VARCHAR,
	description VARCHAR(1024),
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
COMMENT ON TABLE customers is 'Dane klientów';
COMMENT ON COLUMN customers.id is 'Klucz główny';
COMMENT ON COLUMN customers.unique_id is 'UUID Unikalne id klienta';
COMMENT ON COLUMN customers.active is 'Klient dostepny/niedostępny';
COMMENT ON COLUMN customers.name is 'Imię klienta';
COMMENT ON COLUMN customers.last_name is 'Nazwisko klienta';
COMMENT ON COLUMN customers.city is 'Miejscowość klienta';
COMMENT ON COLUMN customers.zip_code is 'Kod pocztowy klienta';
COMMENT ON COLUMN customers.street is 'ulica';
COMMENT ON COLUMN customers.number is 'Numer mieszkania/lokalu';
COMMENT ON COLUMN customers.email is 'Email klienta';
COMMENT ON COLUMN customers.nick is 'Nick klienta';
COMMENT ON COLUMN customers.phone is 'Numer telefonu klienta';
COMMENT ON COLUMN customers.description is 'Notatka dotycząca klienta';
COMMENT ON COLUMN customers.created_at IS 'Data dodania';
COMMENT ON COLUMN customers.created_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION customers_biu_trigfunc()
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
CREATE TRIGGER customers_biu
	BEFORE INSERT OR UPDATE ON customers
	FOR EACH ROW
EXECUTE PROCEDURE customers_biu_trigfunc();