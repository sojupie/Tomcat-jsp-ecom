-- Add a status for orders that warehouse staff have finished packing.
ALTER TABLE customer_order
    DROP CONSTRAINT IF EXISTS customer_order_status_check;

ALTER TABLE customer_order
    ADD CONSTRAINT customer_order_status_check
    CHECK (status IN ('PLACED', 'PACKING', 'PACKED', 'SHIPPED', 'CANCELLED'));
