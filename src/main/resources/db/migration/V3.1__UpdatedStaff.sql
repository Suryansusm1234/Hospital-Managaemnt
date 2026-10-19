ALTER TABLE staff DROP CONSTRAINT staff_role_check;

ALTER TABLE staff ADD CONSTRAINT staff_role_check
    CHECK (role IN ('DOCTOR', 'RECEPTIONIST', 'ADMIN', 'Doctor', 'Receptionist', 'Admin'));