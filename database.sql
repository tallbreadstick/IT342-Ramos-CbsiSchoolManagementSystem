CREATE TABLE users (
    -- database-generated id
    id SERIAL PRIMARY KEY,

    -- primary human-readable identifiers
    school_id VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,

    -- personal information
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    sex sex_type NOT NULL,
    date_of_birth DATE NOT NULL,
    permanent_address TEXT,
    current_address TEXT,

    -- security and metadata
    password_hash VARCHAR(255) NOT NULL,
    password_changed BOOLEAN NOT NULL DEFAULT FALSE,
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- account status
    account_status account_status_type NOT NULL DEFAULT 'PENDING',
    last_login TIMESTAMPTZ
);

CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
   NEW.date_updated = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();