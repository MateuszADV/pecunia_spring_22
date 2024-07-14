CREATE SEQUENCE user_sequence;
CREATE TABLE app_user(
	id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('user_sequence'),
	first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    app_user_role VARCHAR NOT NULL,
    locked BOOLEAN,
    enabled BOOLEAN,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
COMMENT ON TABLE app_user IS 'App User';
COMMENT ON COLUMN app_user.id IS 'Klucz główny';
COMMENT ON COLUMN app_user.first_name IS 'Imię';
COMMENT ON COLUMN app_user.email IS 'Email';
COMMENT ON COLUMN app_user.password IS 'Hasło';
COMMENT ON COLUMN app_user.app_user_role IS 'Rola, Uprawnienia';
COMMENT ON COLUMN app_user.locked IS 'Czy konto jest zablokowane';
COMMENT ON COLUMN app_user.enabled IS 'Czy konto jest aktywne';
COMMENT ON COLUMN app_user.created_at IS 'Data dodania';
COMMENT ON COLUMN app_user.updated_at IS 'Data modyfikacji';

CREATE OR REPLACE FUNCTION app_user_biu_trigfunc()
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
CREATE TRIGGER app_user_biu
	BEFORE INSERT OR UPDATE ON app_user
	FOR EACH ROW
EXECUTE PROCEDURE app_user_biu_trigfunc();