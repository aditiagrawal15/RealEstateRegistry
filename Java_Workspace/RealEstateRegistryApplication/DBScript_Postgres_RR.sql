--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: property_ownership; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.property_ownership (
    property_id bigint NOT NULL,
    property_type character varying,
    property_location character varying,
    property_area bigint,
    property_value bigint,
    property_pincode bigint,
    current_owner_fk bigint,
    previous_owner_fk bigint,
    plot_no character varying,
    police_station character varying,
    district character varying
);


ALTER TABLE public.property_ownership OWNER TO postgres;

--
-- Name: property_ownership_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.property_ownership_seq
    START WITH 400000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.property_ownership_seq OWNER TO postgres;

--
-- Name: property_ownership_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.property_ownership_seq OWNED BY public.property_ownership.property_id;


--
-- Name: registry_nagarpalika; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.registry_nagarpalika (
    reg_no bigint NOT NULL,
    buy_type character varying,
    owner_fk bigint,
    active_flag boolean DEFAULT false NOT NULL,
    stamp_value bigint,
    property_fk bigint
);


ALTER TABLE public.registry_nagarpalika OWNER TO postgres;

--
-- Name: registry_nagarpalika_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.registry_nagarpalika_seq
    START WITH 300000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.registry_nagarpalika_seq OWNER TO postgres;

--
-- Name: registry_nagarpalika_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.registry_nagarpalika_seq OWNED BY public.registry_nagarpalika.reg_no;


--
-- Name: registry_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.registry_user (
    id bigint NOT NULL,
    name character varying NOT NULL,
    dob date NOT NULL,
    aadhar_number bigint NOT NULL,
    pan_number character varying NOT NULL,
    contact_number bigint,
    gender character varying,
    address character varying,
    username character varying,
    password character varying,
    is_logged_in boolean DEFAULT false NOT NULL,
    active_flag boolean DEFAULT true NOT NULL,
    login_time timestamp without time zone,
    is_first_login boolean DEFAULT true NOT NULL,
    logged_in_role character varying,
    is_registrar boolean
);


ALTER TABLE public.registry_user OWNER TO postgres;

--
-- Name: registry_user_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.registry_user_seq
    START WITH 100000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.registry_user_seq OWNER TO postgres;

--
-- Name: registry_user_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.registry_user_seq OWNED BY public.registry_user.id;


--
-- Name: sale_agreement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_agreement (
    agreement_id bigint NOT NULL,
    seller_user_fk bigint,
    purchaser_user_fk bigint,
    tentative_date date,
    sale_agreement_doc_name character varying,
    first_witness_name character varying,
    second_witness_name character varying,
    property_fk bigint,
    active_flag boolean
);


ALTER TABLE public.sale_agreement OWNER TO postgres;

--
-- Name: sale_agreement_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sale_agreement_seq
    START WITH 500000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sale_agreement_seq OWNER TO postgres;

--
-- Name: sale_agreement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sale_agreement_seq OWNED BY public.sale_agreement.agreement_id;


--
-- Name: workflow; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.workflow (
    id bigint NOT NULL,
    details character varying,
    buyer_comments character varying,
    status character varying,
    seller_approver bigint,
    approver_role character varying,
    raised_by bigint,
    raised_role character varying,
    active_flag boolean,
    registrar_approver bigint,
    sale_agreement_fk bigint,
    created timestamp without time zone,
    updated timestamp without time zone,
    pending_with bigint,
    seller_comments character varying,
    registrar_comments character varying,
    registry_fk bigint
);


ALTER TABLE public.workflow OWNER TO postgres;

--
-- Name: workflow_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.workflow_seq
    START WITH 900000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.workflow_seq OWNER TO postgres;

--
-- Name: workflow_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.workflow_seq OWNED BY public.workflow.id;


--
-- Name: property_ownership property_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_ownership ALTER COLUMN property_id SET DEFAULT nextval('public.property_ownership_seq'::regclass);


--
-- Name: registry_nagarpalika reg_no; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registry_nagarpalika ALTER COLUMN reg_no SET DEFAULT nextval('public.registry_nagarpalika_seq'::regclass);


--
-- Name: registry_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registry_user ALTER COLUMN id SET DEFAULT nextval('public.registry_user_seq'::regclass);


--
-- Name: sale_agreement agreement_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_agreement ALTER COLUMN agreement_id SET DEFAULT nextval('public.sale_agreement_seq'::regclass);


--
-- Data for Name: property_ownership; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.property_ownership (property_id, property_type, property_location, property_area, property_value, property_pincode, current_owner_fk, previous_owner_fk, plot_no, police_station, district) VALUES (123, 'Office', 'Pune', 10023, 12600000, 411006, 100001, NULL, '210', 'Pune Nagar Police Station', 'Hinjewadi');


--
-- Data for Name: registry_nagarpalika; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.registry_nagarpalika (reg_no, buy_type, owner_fk, active_flag, stamp_value, property_fk) VALUES (4325, 'Direct', 100000, true, 20000, 123);
INSERT INTO public.registry_nagarpalika (reg_no, buy_type, owner_fk, active_flag, stamp_value, property_fk) VALUES (100002, 'Direct', 100000, false, 1500, 123);


--
-- Data for Name: registry_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.registry_user (id, name, dob, aadhar_number, pan_number, contact_number, gender, address, username, password, is_logged_in, active_flag, login_time, is_first_login, logged_in_role, is_registrar) VALUES (100000, 'Aditi', '2022-08-15', 123456789012, 'SHK76MN', 2763881, 'F', 'Karad', 'aditi', 'b45cffe084dd3d20d928bee85e7bf21', true, true, '2022-08-16 23:02:34.587', false, 'BUYER', false);
INSERT INTO public.registry_user (id, name, dob, aadhar_number, pan_number, contact_number, gender, address, username, password, is_logged_in, active_flag, login_time, is_first_login, logged_in_role, is_registrar) VALUES (100001, 'Pranit', '1998-06-27', 987654321098, 'HGN45GY', 3431, 'M', 'Karad', 'pranit', 'b45cffe084dd3d20d928bee85e7bf21', false, true, '2022-08-16 23:02:34.587', true, 'SELLER', false);
INSERT INTO public.registry_user (id, name, dob, aadhar_number, pan_number, contact_number, gender, address, username, password, is_logged_in, active_flag, login_time, is_first_login, logged_in_role, is_registrar) VALUES (100002, 'Kumar', '1968-09-23', 654321098776, 'KH87LKU', 98765137, 'M', 'Satara', 'kumar', 'b45cffe084dd3d20d928bee85e7bf21', false, true, '2022-08-16 23:02:34.587', false, 'REGISTRAR', true);


--
-- Data for Name: sale_agreement; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: workflow; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: property_ownership_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.property_ownership_seq', 400000, false);


--
-- Name: registry_nagarpalika_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.registry_nagarpalika_seq', 300000, false);


--
-- Name: registry_user_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.registry_user_seq', 100002, true);


--
-- Name: sale_agreement_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sale_agreement_seq', 500003, true);


--
-- Name: workflow_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.workflow_seq', 900001, true);


--
-- Name: property_ownership property_ownership_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_ownership
    ADD CONSTRAINT property_ownership_pkey PRIMARY KEY (property_id);


--
-- Name: registry_nagarpalika registry_nagarpalika_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registry_nagarpalika
    ADD CONSTRAINT registry_nagarpalika_pkey PRIMARY KEY (reg_no);


--
-- Name: registry_user registry_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registry_user
    ADD CONSTRAINT registry_user_pkey PRIMARY KEY (id);


--
-- Name: sale_agreement sale_agreement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_agreement
    ADD CONSTRAINT sale_agreement_pkey PRIMARY KEY (agreement_id);


--
-- Name: workflow workflow_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_pkey PRIMARY KEY (id);


--
-- Name: property_ownership current_owner_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_ownership
    ADD CONSTRAINT current_owner_fk FOREIGN KEY (current_owner_fk) REFERENCES public.registry_user(id);


--
-- Name: registry_nagarpalika owner_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registry_nagarpalika
    ADD CONSTRAINT owner_fk FOREIGN KEY (owner_fk) REFERENCES public.registry_user(id);


--
-- Name: property_ownership previous_owner_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_ownership
    ADD CONSTRAINT previous_owner_fk FOREIGN KEY (previous_owner_fk) REFERENCES public.registry_user(id);


--
-- Name: sale_agreement property_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_agreement
    ADD CONSTRAINT property_fk FOREIGN KEY (property_fk) REFERENCES public.property_ownership(property_id) NOT VALID;


--
-- Name: registry_nagarpalika property_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registry_nagarpalika
    ADD CONSTRAINT property_fk FOREIGN KEY (property_fk) REFERENCES public.property_ownership(property_id) NOT VALID;


--
-- Name: sale_agreement purchaser_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_agreement
    ADD CONSTRAINT purchaser_user_fk FOREIGN KEY (purchaser_user_fk) REFERENCES public.registry_user(id);


--
-- Name: sale_agreement seller_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_agreement
    ADD CONSTRAINT seller_user_fk FOREIGN KEY (seller_user_fk) REFERENCES public.registry_user(id);


--
-- Name: workflow workflow_approver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_approver_fkey FOREIGN KEY (seller_approver) REFERENCES public.registry_user(id);


--
-- Name: workflow workflow_pending_with_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_pending_with_fkey FOREIGN KEY (pending_with) REFERENCES public.registry_user(id) NOT VALID;


--
-- Name: workflow workflow_raised_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_raised_by_fkey FOREIGN KEY (raised_by) REFERENCES public.registry_user(id);


--
-- Name: workflow workflow_registrar_approver_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_registrar_approver_fkey FOREIGN KEY (registrar_approver) REFERENCES public.registry_user(id) NOT VALID;


--
-- Name: workflow workflow_registry_fk_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_registry_fk_fkey FOREIGN KEY (registry_fk) REFERENCES public.registry_nagarpalika(reg_no) NOT VALID;


--
-- Name: workflow workflow_sale_agreement_fk_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.workflow
    ADD CONSTRAINT workflow_sale_agreement_fk_fkey FOREIGN KEY (sale_agreement_fk) REFERENCES public.sale_agreement(agreement_id) NOT VALID;


--
-- PostgreSQL database dump complete
--

