
ALTER TABLE suppliers
  ALTER COLUMN trade_type TYPE VARCHAR(100),
  ALTER COLUMN delivery_type TYPE VARCHAR(100);

ALTER TABLE suppliers
  DROP CONSTRAINT IF EXISTS suppliers_core_system_code_key,
  ALTER COLUMN core_system_code TYPE VARCHAR(50),
  ALTER COLUMN representative_code TYPE VARCHAR(50);

ALTER TABLE contract_document_types
  ALTER COLUMN code TYPE VARCHAR(50);

ALTER TABLE pb_orders
  ALTER COLUMN supplier_id DROP NOT NULL,
  ALTER COLUMN pb_category DROP NOT NULL;

ALTER TABLE supplier_patterns
  DROP CONSTRAINT IF EXISTS supplier_patterns_supplier_id_pattern_code_key;
