-- Добавляем колонку authenticated_at (TIMESTAMP NOT NULL)
ALTER TABLE t_user
ADD COLUMN authenticated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Добавляем колонку company_id (INT с FOREIGN KEY)
ALTER TABLE t_user
ADD COLUMN company_id INT,
ADD CONSTRAINT fk_t_user_company
    FOREIGN KEY (company_id)
    REFERENCES t_company(id);