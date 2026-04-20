-- Align DB with JPA: ProductMaster + PbOrder.product

CREATE TABLE product_masters (
  id BIGSERIAL PRIMARY KEY,
  ms_code VARCHAR(20) NOT NULL UNIQUE,
  product_name VARCHAR(300) NOT NULL,
  brand_type VARCHAR(10),
  specification VARCHAR(200),
  large_category_code VARCHAR(10),
  large_category_name VARCHAR(100),
  status VARCHAR(20) DEFAULT 'active',
  notes TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Seed masters from existing order lines (one row per distinct ms_code)
INSERT INTO product_masters (ms_code, product_name, brand_type, specification, large_category_code, large_category_name, status)
SELECT DISTINCT ON (b.ms_norm)
  b.ms_norm,
  COALESCE(NULLIF(TRIM(b.product_name), ''), '(unknown)'),
  b.brand_type,
  b.specification,
  b.large_category_code,
  b.large_category_name,
  'active'
FROM (
  SELECT
    TRIM(ms_code) AS ms_norm,
    product_name,
    brand_type,
    specification,
    large_category_code,
    large_category_name
  FROM pb_orders
  WHERE ms_code IS NOT NULL AND LENGTH(TRIM(ms_code)) > 0
) b
ORDER BY b.ms_norm;

ALTER TABLE pb_orders
  ADD COLUMN product_id BIGINT REFERENCES product_masters(id);

UPDATE pb_orders o
SET product_id = p.id
FROM product_masters p
WHERE o.ms_code IS NOT NULL
  AND LENGTH(TRIM(o.ms_code)) > 0
  AND p.ms_code = TRIM(o.ms_code);

CREATE INDEX idx_pb_orders_product ON pb_orders(product_id);
