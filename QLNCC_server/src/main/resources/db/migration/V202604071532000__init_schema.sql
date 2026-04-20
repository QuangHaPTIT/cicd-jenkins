CREATE TABLE suppliers (
  id BIGSERIAL PRIMARY KEY,

  management_code VARCHAR(10) NOT NULL UNIQUE,
  core_system_code VARCHAR(50),
  representative_code VARCHAR(50),

  name_official VARCHAR(200) NOT NULL,
  name_display VARCHAR(200),
  name_kana VARCHAR(200),

  trade_type VARCHAR(100),
  delivery_type VARCHAR(100),

  subcontractor_list_no INTEGER,
  capital_amount_million DECIMAL(15,2),
  employee_count INTEGER,
  is_subsidiary BOOLEAN DEFAULT FALSE,

  zip_code VARCHAR(10),
  address TEXT,
  phone VARCHAR(30),

  status VARCHAR(20) NOT NULL DEFAULT 'active',
  deleted_at TIMESTAMPTZ,
  deleted_reason TEXT,
  added_to_list_at DATE,

  subcontractor_duty_type VARCHAR(100),
  delegate_work_content TEXT,

  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(50),
  updated_by VARCHAR(50)
);

CREATE INDEX idx_suppliers_management_code ON suppliers(management_code);
CREATE INDEX idx_suppliers_core_system_code ON suppliers(core_system_code);
CREATE INDEX idx_suppliers_status ON suppliers(status);
CREATE INDEX idx_suppliers_name_official ON suppliers USING gin(to_tsvector('simple', name_official));

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  employee_code VARCHAR(30) NOT NULL UNIQUE,
  email VARCHAR(200) NOT NULL UNIQUE,
  display_name VARCHAR(100) NOT NULL,

  department VARCHAR(100),
  team VARCHAR(100),
  role VARCHAR(20) NOT NULL DEFAULT 'MD',

  is_active BOOLEAN DEFAULT TRUE,
  last_login_at TIMESTAMPTZ,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE supplier_patterns (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,

  pattern_code VARCHAR(10) NOT NULL,
  brand_category VARCHAR(20),
  main_products TEXT,

  md_user_id BIGINT REFERENCES users(id),
  mgr_user_id BIGINT REFERENCES users(id),
  department_name VARCHAR(100),
  team_name VARCHAR(100),

  is_active BOOLEAN DEFAULT TRUE,
  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  -- UNIQUE(supplier_id, pattern_code) removed: same supplier can have multiple rows with same pattern (different brand/team)
);

CREATE TABLE supplier_parent_links (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,
  parent_supplier_id BIGINT NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,

  link_order SMALLINT NOT NULL DEFAULT 1,
  link_type VARCHAR(50),

  is_active BOOLEAN DEFAULT TRUE,
  started_at DATE,
  ended_at DATE,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE(supplier_id, link_order),
  CHECK(link_order BETWEEN 1 AND 8),
  CHECK(supplier_id != parent_supplier_id)
);

CREATE TABLE contract_document_types (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(50) NOT NULL UNIQUE,
  display_number VARCHAR(10),
  name VARCHAR(200) NOT NULL,
  short_name VARCHAR(100),
  category VARCHAR(50),
  requires_date BOOLEAN DEFAULT TRUE,
  requires_doc_no BOOLEAN DEFAULT TRUE,
  sort_order SMALLINT DEFAULT 0,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE supplier_contract_documents (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,
  document_type_id BIGINT NOT NULL REFERENCES contract_document_types(id),

  status VARCHAR(20) NOT NULL DEFAULT 'not_required',

  document_number VARCHAR(100),
  signed_date DATE,
  expiry_date DATE,

  doc_management_system_no VARCHAR(50),

  pattern_required VARCHAR(20),

  notes TEXT,

  submitted_at TIMESTAMPTZ,
  submitted_by VARCHAR(50),
  last_checked_at TIMESTAMPTZ,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE(supplier_id, document_type_id)
);

CREATE INDEX idx_scd_supplier ON supplier_contract_documents(supplier_id);
CREATE INDEX idx_scd_status ON supplier_contract_documents(status);
CREATE INDEX idx_scd_expiry ON supplier_contract_documents(expiry_date) WHERE expiry_date IS NOT NULL;

CREATE TABLE pl_insurance_management (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id),

  insurance_year SMALLINT NOT NULL,
  due_date DATE NOT NULL,
  collection_status VARCHAR(20) NOT NULL DEFAULT 'pending',

  collected_at DATE,
  collected_by VARCHAR(50),

  alert_sent_at TIMESTAMPTZ,
  reminder_count SMALLINT DEFAULT 0,

  notes TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE(supplier_id, insurance_year)
);

CREATE TABLE price_negotiation_management (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id),

  fiscal_year SMALLINT NOT NULL,
  is_target BOOLEAN NOT NULL,
  exclusion_reason TEXT,

  status VARCHAR(30) NOT NULL DEFAULT 'not_started',

  department VARCHAR(100),
  category VARCHAR(100),

  negotiation_date DATE,
  completed_at DATE,
  result_notes TEXT,

  alert_sent_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE(supplier_id, fiscal_year)
);

CREATE TABLE factory_masters (
  id BIGSERIAL PRIMARY KEY,
  factory_code VARCHAR(20) NOT NULL UNIQUE,
  factory_name VARCHAR(200) NOT NULL,
  factory_name_kana VARCHAR(200),

  zip_code VARCHAR(10),
  prefecture VARCHAR(50),
  address TEXT,
  latitude DECIMAL(10, 7),
  longitude DECIMAL(10, 7),

  phone VARCHAR(30),

  status VARCHAR(20) DEFAULT 'active',
  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE supplier_factory_links (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id) ON DELETE CASCADE,
  factory_id BIGINT NOT NULL REFERENCES factory_masters(id),

  link_type VARCHAR(50),
  is_primary BOOLEAN DEFAULT FALSE,

  started_at DATE,
  ended_at DATE,
  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE(supplier_id, factory_id)
);

CREATE TABLE pb_orders (
  id BIGSERIAL PRIMARY KEY,

  supplier_id BIGINT REFERENCES suppliers(id),
  factory_id BIGINT REFERENCES factory_masters(id),

  pb_category VARCHAR(20),
  department_name VARCHAR(100),
  team_name VARCHAR(100),
  applicant_name VARCHAR(100),
  large_category_code VARCHAR(10),
  large_category_name VARCHAR(100),

  ms_code VARCHAR(20),
  product_name VARCHAR(300),
  brand_type VARCHAR(10),
  specification VARCHAR(200),

  order_number VARCHAR(50),
  order_date DATE,
  quantity INTEGER,
  unit VARCHAR(20),
  unit_price DECIMAL(15,2),
  total_amount DECIMAL(15,2),

  aw_decision_no VARCHAR(50),
  aw_approval_status VARCHAR(30),
  aw_approved_at TIMESTAMPTZ,

  has_next_order VARCHAR(30),
  next_order_planned_at DATE,

  order_doc_sent_at DATE,
  order_doc_deadline DATE,
  recipient_name VARCHAR(200),
  recipient_email VARCHAR(200),

  exclusion_type VARCHAR(50),

  status VARCHAR(30) NOT NULL DEFAULT 'active',
  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(50),
  updated_by VARCHAR(50)
);

CREATE INDEX idx_pb_orders_supplier ON pb_orders(supplier_id);
CREATE INDEX idx_pb_orders_ms_code ON pb_orders(ms_code);
CREATE INDEX idx_pb_orders_next_order ON pb_orders(next_order_planned_at) WHERE has_next_order = '有り';
CREATE INDEX idx_pb_orders_status ON pb_orders(status);

CREATE TABLE rebate_contracts (
  id BIGSERIAL PRIMARY KEY,

  management_no VARCHAR(50) UNIQUE,
  contract_no VARCHAR(50) NOT NULL UNIQUE,
  prev_year_contract_no VARCHAR(50),
  next_year_contract_no VARCHAR(50),
  rebate_calc_contract_no VARCHAR(50),

  supplier_id BIGINT NOT NULL REFERENCES suppliers(id),

  aw_decision_no VARCHAR(50),
  cvs_ff_hd_type VARCHAR(20),

  draft_department VARCHAR(100),
  draft_team VARCHAR(100),
  drafter_name VARCHAR(100),
  input_person VARCHAR(100),
  input_date DATE,
  draft_date DATE,

  contract_signed_date DATE,
  contract_start_date DATE NOT NULL,
  contract_end_date DATE NOT NULL,
  calculation_start_date DATE,
  calculation_end_date DATE,
  payment_due_date DATE,
  rebate_recognition_date DATE,

  rebate_type VARCHAR(50),
  large_category_code VARCHAR(10),
  target_products TEXT,
  target_region VARCHAR(100),
  calculation_basis TEXT,

  direct_rebate_amount DECIMAL(15,2),
  all_store_rebate_amount DECIMAL(15,2),

  payment_method VARCHAR(30),
  bank_name VARCHAR(100),
  bank_branch VARCHAR(100),
  account_number VARCHAR(50),

  seal_request_date DATE,
  seal_request_no VARCHAR(50),
  seal_return_status VARCHAR(30) DEFAULT 'pending',

  invoice_no VARCHAR(50),
  invoice_input_at TIMESTAMPTZ,

  is_subcontractor_target BOOLEAN DEFAULT FALSE,

  status VARCHAR(30) NOT NULL DEFAULT 'draft',

  contract_result VARCHAR(30),
  has_agreement_letter VARCHAR(10),
  reason_letter_required VARCHAR(10),
  reason_letter_content TEXT,

  notes TEXT,
  other_notes TEXT,
  supplier_contact_dept TEXT,
  supplier_contact_person VARCHAR(200),

  recognition_payment_diff_note VARCHAR(500),

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(50),
  updated_by VARCHAR(50)
);

CREATE INDEX idx_rebate_contracts_supplier ON rebate_contracts(supplier_id);
CREATE INDEX idx_rebate_contracts_status ON rebate_contracts(status);
CREATE INDEX idx_rebate_contracts_payment_due ON rebate_contracts(payment_due_date);
CREATE INDEX idx_rebate_contracts_end_date ON rebate_contracts(contract_end_date);

CREATE TABLE promo_contracts (
  id BIGSERIAL PRIMARY KEY,
  management_no VARCHAR(50) UNIQUE,
  contract_no VARCHAR(50) NOT NULL UNIQUE,
  prev_year_contract_no VARCHAR(50),

  supplier_id BIGINT NOT NULL REFERENCES suppliers(id),

  aw_decision_no VARCHAR(50),
  cvs_ff_hd_type VARCHAR(20),

  draft_department VARCHAR(100),
  draft_team VARCHAR(100),
  drafter_name VARCHAR(100),
  input_person VARCHAR(100),
  input_date DATE,
  draft_date DATE,

  contract_signed_date DATE,
  contract_start_date DATE NOT NULL,
  contract_end_date DATE NOT NULL,
  calculation_start_date DATE,
  calculation_end_date DATE,
  payment_due_date DATE,

  event_content TEXT,
  calculation_basis TEXT,
  target_region VARCHAR(100),

  amount DECIMAL(15,2),
  payment_method VARCHAR(30),
  bank_name VARCHAR(100),
  bank_branch VARCHAR(100),
  account_number VARCHAR(50),

  seal_request_date DATE,
  seal_request_no VARCHAR(50),
  seal_return_status VARCHAR(30) DEFAULT 'pending',

  invoice_no VARCHAR(50),
  invoice_input_at TIMESTAMPTZ,

  is_subcontractor_target BOOLEAN DEFAULT FALSE,
  status VARCHAR(30) NOT NULL DEFAULT 'draft',
  contract_result VARCHAR(30),
  has_agreement_letter VARCHAR(10),
  reason_letter_required VARCHAR(10),
  reason_letter_content TEXT,

  notes TEXT,
  supplier_contact_dept TEXT,
  supplier_contact_person VARCHAR(200),

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(50),
  updated_by VARCHAR(50)
);

CREATE TABLE uncollected_payments (
  id BIGSERIAL PRIMARY KEY,

  contract_type VARCHAR(20) NOT NULL,
  contract_id BIGINT NOT NULL,

  period_type VARCHAR(30) NOT NULL,

  fiscal_year SMALLINT NOT NULL,
  period_start_date DATE NOT NULL,
  period_end_date DATE NOT NULL,

  expected_amount DECIMAL(15,2),
  actual_amount DECIMAL(15,2),
  difference DECIMAL(15,2),

  status VARCHAR(30) DEFAULT 'uncollected',

  collected_at DATE,
  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  UNIQUE(contract_type, contract_id, period_type, fiscal_year)
);

CREATE TABLE invoices (
  id BIGSERIAL PRIMARY KEY,

  invoice_no VARCHAR(50) NOT NULL UNIQUE,
  invoice_source VARCHAR(20) NOT NULL DEFAULT 'agile_works',

  contract_type VARCHAR(20),
  rebate_contract_id BIGINT REFERENCES rebate_contracts(id),
  promo_contract_id BIGINT REFERENCES promo_contracts(id),

  supplier_id BIGINT REFERENCES suppliers(id),
  supplier_code_aw VARCHAR(30),
  supplier_name_aw VARCHAR(200),

  issuer_department VARCHAR(100),
  issuer_name VARCHAR(100),
  issuer_code VARCHAR(30),
  issuer_org_code VARCHAR(30),

  charge_dept_code VARCHAR(30),
  charge_dept_name VARCHAR(100),

  amount DECIMAL(15,2) NOT NULL,
  tax_rate DECIMAL(5,3),

  applied_at TIMESTAMPTZ,
  approved_at TIMESTAMPTZ,
  recognition_month VARCHAR(7),
  transfer_due_date DATE,
  offset_date DATE,

  account_code VARCHAR(20),
  account_detail_code VARCHAR(20),
  account_name VARCHAR(200),
  debit_account_code VARCHAR(20),
  debit_account_name VARCHAR(200),

  description TEXT,
  payment_method VARCHAR(30),

  aw_doc_status VARCHAR(30),
  line_item_no VARCHAR(10),

  invoice_type VARCHAR(30),
  is_cancelled BOOLEAN DEFAULT FALSE,
  management_input_at TIMESTAMPTZ,

  notes TEXT,

  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by VARCHAR(50),
  updated_by VARCHAR(50)
);

CREATE INDEX idx_invoices_supplier ON invoices(supplier_id);
CREATE INDEX idx_invoices_recognition_month ON invoices(recognition_month);
CREATE INDEX idx_invoices_transfer_due ON invoices(transfer_due_date);
CREATE INDEX idx_invoices_rebate_contract ON invoices(rebate_contract_id);
CREATE INDEX idx_invoices_promo_contract ON invoices(promo_contract_id);

CREATE TABLE alert_logs (
  id BIGSERIAL PRIMARY KEY,

  alert_type VARCHAR(50) NOT NULL,

  target_type VARCHAR(30),
  target_id BIGINT,

  recipient_user_id BIGINT REFERENCES users(id),
  recipient_email VARCHAR(200),

  message TEXT,
  sent_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_read BOOLEAN DEFAULT FALSE,
  read_at TIMESTAMPTZ,

  metadata JSONB
);

CREATE INDEX idx_alert_logs_recipient ON alert_logs(recipient_user_id, is_read);
CREATE INDEX idx_alert_logs_type ON alert_logs(alert_type, sent_at DESC);

CREATE TABLE import_jobs (
  id BIGSERIAL PRIMARY KEY,

  job_type VARCHAR(50) NOT NULL,

  file_name VARCHAR(500),
  file_size_bytes BIGINT,

  status VARCHAR(20) NOT NULL DEFAULT 'pending',

  total_rows INTEGER,
  success_rows INTEGER,
  error_rows INTEGER,
  skipped_rows INTEGER,

  started_at TIMESTAMPTZ,
  completed_at TIMESTAMPTZ,

  error_log JSONB,
  summary JSONB,

  triggered_by VARCHAR(50),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE audit_logs (
  id BIGSERIAL PRIMARY KEY,

  table_name VARCHAR(100) NOT NULL,
  record_id BIGINT NOT NULL,
  action VARCHAR(10) NOT NULL,

  old_values JSONB,
  new_values JSONB,
  changed_fields VARCHAR[],

  performed_by VARCHAR(50),
  performed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  ip_address INET,
  user_agent TEXT
);

CREATE INDEX idx_audit_logs_table_record ON audit_logs(table_name, record_id);
CREATE INDEX idx_audit_logs_performed_at ON audit_logs(performed_at DESC);

CREATE TABLE supplier_code_aliases (
  supplier_id BIGINT NOT NULL REFERENCES suppliers(id),
  system_name VARCHAR(50) NOT NULL,
  alias_code VARCHAR(50) NOT NULL,
  notes TEXT,

  PRIMARY KEY (supplier_id, system_name, alias_code),
  UNIQUE(system_name, alias_code)
);

CREATE TABLE pattern_document_requirements (
  id BIGSERIAL PRIMARY KEY,
  pattern_code VARCHAR(20) NOT NULL,
  document_type_id BIGINT NOT NULL REFERENCES contract_document_types(id),
  is_mandatory BOOLEAN NOT NULL DEFAULT TRUE,
  notes TEXT,
  UNIQUE(pattern_code, document_type_id)
);

CREATE TABLE alert_configurations (
  alert_type VARCHAR(50) PRIMARY KEY,
  is_enabled BOOLEAN DEFAULT TRUE,
  advance_days_1 SMALLINT,
  advance_days_2 SMALLINT,
  advance_days_3 SMALLINT,
  email_enabled BOOLEAN DEFAULT TRUE,
  push_enabled BOOLEAN DEFAULT TRUE,
  recipient_roles VARCHAR[]
);

-- NOTE:
-- invoice_line_items is intentionally omitted because detailed DDL
-- is not defined in the provided specification.
