ALTER TABLE patient
    ADD COLUMN IF NOT EXISTS date_of_birth DATE NOT NULL,
    ADD COLUMN IF NOT EXISTS gender varchar check ( gender in ('Male', 'Female') );
