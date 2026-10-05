-- Add the role column to databases created before user roles were introduced.
-- Existing accounts become customers; new registrations use the same default.
ALTER TABLE app_user
    ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER'
    CHECK (role IN ('CUSTOMER', 'ADMIN', 'WAREHOUSE'));
