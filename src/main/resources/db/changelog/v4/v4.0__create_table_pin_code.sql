CREATE TABLE pin_code (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    max_activations INT NOT NULL,
    used_activations INT NOT NULL DEFAULT 0,
    company_id INT REFERENCES t_company(id)
);