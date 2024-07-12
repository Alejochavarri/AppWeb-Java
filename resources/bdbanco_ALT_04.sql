use bdbancos;

ALTER TABLE cuenta ADD CONSTRAINT chk_saldo CHECK (saldo >= 0);
