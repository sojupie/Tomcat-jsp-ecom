-- Add account activation state to databases created before user administration.
-- Existing accounts remain active to match the original account behavior.
ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;
