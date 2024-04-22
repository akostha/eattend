-- DROP SCHEMA devldb;

CREATE SCHEMA devldb AUTHORIZATION postgres;

-- DROP SEQUENCE devldb.contact_id_seq;

CREATE SEQUENCE devldb.contact_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE devldb.employee_id_seq;

CREATE SEQUENCE devldb.employee_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE devldb.event_id_seq;

CREATE SEQUENCE devldb.event_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;-- devldb.contact definition

-- Drop table

-- DROP TABLE devldb.contact;

CREATE TABLE devldb.contact (
	id serial4 NOT NULL,
	first_name varchar NOT NULL,
	last_name varchar NULL,
	phone varchar NOT NULL,
	email varchar NULL,
	created_at date NULL DEFAULT now(),
	updated_at date NULL,
	event_id int4 NULL,
	CONSTRAINT contact_pkey PRIMARY KEY (id)
);
CREATE UNIQUE INDEX unique_phone_event_id ON devldb.contact USING btree (phone, event_id);


-- devldb.employee definition

-- Drop table

-- DROP TABLE devldb.employee;

CREATE TABLE devldb.employee (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	first_name varchar NOT NULL,
	last_name varchar NOT NULL,
	dept varchar NULL,
	salary int4 NULL,
	createdat date NULL
);


-- devldb."event" definition

-- Drop table

-- DROP TABLE devldb."event";

CREATE TABLE devldb."event" (
	id serial4 NOT NULL,
	descr varchar NOT NULL,
	event_date date NULL,
	created_at date NULL,
	updated_at date NULL,
	CONSTRAINT event_pkey PRIMARY KEY (id)
);