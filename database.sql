-- POSTGRESQL DATABASE SCHEMA --

CREATE TYPE sex_type AS ENUM('Male', 'Female');

CREATE TYPE account_status_type AS ENUM(
    'PENDING',   -- account created by the system, pending approval by sys admin
    'ACTIVE',    -- account is approved and usable by the user
    'SUSPENDED', -- account is temporarily disabled, for example due to suspicious activity or policy violation
    'ARCHIVED'   -- graduated/terminated students or staff
);

CREATE TABLE users (
    -- database-generated id
    id SERIAL PRIMARY KEY,

    -- primary human-readable identifiers
    school_id VARCHAR(20) NOT NULL UNIQUE, -- school ID is used as identifier in login
    email VARCHAR(255) NOT NULL UNIQUE, -- email is used for password recovery

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
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- account status
    account_status account_status_type NOT NULL DEFAULT 'PENDING',
    last_login TIMESTAMPTZ
);

CREATE TABLE blacklisted_tokens (
    -- table for storing blacklisted JWTs (e.g., after logout or password change)
    -- expired tokens should be periodically cleared by a scheduler

    id SERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    blacklisted_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL -- based on the token's original expiration time, to know when to clear it from the blacklist
);

CREATE TABLE account_recovery (
    -- table for storing password reset tokens
    -- a scheduler must be used to clear expired recovery codes

    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id INTEGER NOT NULL,

    recovery_code VARCHAR(6) NOT NULL, 
    attempts_counted INTEGER NOT NULL DEFAULT 0,
    max_attempts INTEGER NOT NULL DEFAULT 5,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    -- timestamp when code was successfully used (NULL if unused)

    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    UNIQUE (user_id, recovery_code),
    -- ensures a user cannot have duplicate active recovery codes

    CHECK (attempts_counted <= max_attempts)
    -- enforces brute-force protection at database level
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

CREATE TABLE student_records (
    -- STUDENT RECORDS table which links a student user to their student data

    -- ignore for now
);

CREATE TABLE resources (
    -- a resource (image, file, etc) stored in the backend to srv/portal/resources/{uuid}

    id VARCHAR(255) PRIMARY KEY,
    date_uploaded TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_resources (
    -- junction table linking a resource and a user

    user_id INTEGER NOT NULL,
    resource_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, resource_id),

    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    FOREIGN KEY (resource_id)
        REFERENCES resources(id)
        ON DELETE CASCADE
);

-- PERMISSIONS TABLE
CREATE TABLE permissions (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,  -- e.g., accounting.transactions.create
    description TEXT,           -- optional human-readable description
    parent_id INTEGER REFERENCES permissions(id) ON DELETE CASCADE -- for hierarchical relationships
);

-- ROLES TABLE (already defined)
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT            -- optional description for the role
);

-- ROLE_PERMISSIONS: junction table linking roles to permissions
CREATE TABLE role_permissions (
    role_id INTEGER NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INTEGER NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- USER_ROLES: junction table linking users to roles
CREATE TABLE user_roles (
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

