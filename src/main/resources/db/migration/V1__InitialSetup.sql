CREATE TABLE IF NOT EXISTS  staff (
  user_id bigint PRIMARY KEY Generated always as identity,
  role varchar check ( role IN ('doctor','receptionist', 'admin')) ,
  username varchar UNIQUE,
  password varchar
);

CREATE TABLE IF NOT EXISTS admin (
admin_id BIGINT PRIMARY KEY REFERENCES staff(user_id) ON DELETE CASCADE,
name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS  doctor (
   doctor_id bigint PRIMARY KEY REFERENCES staff(user_id) ON DELETE CASCADE ,
   name varchar,
   phone_number varchar,
   email varchar,
   designation varchar,
   is_available boolean DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS  patient (
   patient_id bigint PRIMARY KEY Generated always as identity,
   name varchar,
   phone_number varchar
);

CREATE TABLE IF NOT EXISTS  receptionist (
   receptionist_id bigint PRIMARY KEY REFERENCES staff(user_id) ON DELETE CASCADE,
   name varchar NOT NULL ,
    phone_number varchar(20)
);

CREATE TABLE IF NOT EXISTS appointments (
   id bigint PRIMARY KEY Generated always as identity,
   patient_id bigint references patient(patient_id),
   doctor_id bigint references doctor(doctor_id),
   appointment_time TIMESTAMP WITH TIME ZONE NOT NULL,
   remarks text,
   status varchar(50) not null default 'SCHEDULED' check ( status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')) ,
   created_by bigint references  staff (user_id),
   created_at timestamp with time zone default current_timestamp,
   updated_at timestamp with time zone default current_timestamp
);