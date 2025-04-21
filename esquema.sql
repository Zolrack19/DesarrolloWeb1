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
-- Name: diccionario_falla; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.diccionario_falla (
    id integer NOT NULL,
    fecha_registro date,
    nombrefalla character varying(200) NOT NULL,
    solucionregitrada text NOT NULL,
    tecnico_id integer
);


ALTER TABLE public.diccionario_falla OWNER TO zolrack;

--
-- Name: diccionario_falla_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.diccionario_falla_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.diccionario_falla_id_seq OWNER TO zolrack;

--
-- Name: diccionario_falla_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.diccionario_falla_id_seq OWNED BY public.diccionario_falla.id;


--
-- Name: equipo; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.equipo (
    id integer NOT NULL,
    fecha_adquisicion date NOT NULL,
    estado_equipo_id smallint,
    tipo_equipo_id smallint,
    usuario_id integer
);


ALTER TABLE public.equipo OWNER TO zolrack;

--
-- Name: equipo_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.equipo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.equipo_id_seq OWNER TO zolrack;

--
-- Name: equipo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.equipo_id_seq OWNED BY public.equipo.id;


--
-- Name: estado_equipo; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.estado_equipo (
    id integer NOT NULL,
    nombre character varying(200) NOT NULL
);


ALTER TABLE public.estado_equipo OWNER TO zolrack;

--
-- Name: estado_equipo_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.estado_equipo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.estado_equipo_id_seq OWNER TO zolrack;

--
-- Name: estado_equipo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.estado_equipo_id_seq OWNED BY public.estado_equipo.id;


--
-- Name: estadoincidencia; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.estadoincidencia (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE public.estadoincidencia OWNER TO zolrack;

--
-- Name: estadoincidencia_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.estadoincidencia_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.estadoincidencia_id_seq OWNER TO zolrack;

--
-- Name: estadoincidencia_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.estadoincidencia_id_seq OWNED BY public.estadoincidencia.id;


--
-- Name: incidencia; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.incidencia (
    id integer NOT NULL,
    descripcion character varying(2000) NOT NULL,
    fecha_registro timestamp without time zone NOT NULL,
    equipo_id integer,
    estado_incidencia_id smallint,
    persona_registro_id integer,
    tecnico_id integer NOT NULL
);


ALTER TABLE public.incidencia OWNER TO zolrack;

--
-- Name: incidencia_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.incidencia_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.incidencia_id_seq OWNER TO zolrack;

--
-- Name: incidencia_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.incidencia_id_seq OWNED BY public.incidencia.id;


--
-- Name: informe_tecnico; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.informe_tecnico (
    id integer NOT NULL,
    descripcion_solucion text NOT NULL,
    fecha_emision timestamp without time zone NOT NULL,
    incidencia_id integer
);


ALTER TABLE public.informe_tecnico OWNER TO zolrack;

--
-- Name: informe_tecnico_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.informe_tecnico_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.informe_tecnico_id_seq OWNER TO zolrack;

--
-- Name: informe_tecnico_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.informe_tecnico_id_seq OWNED BY public.informe_tecnico.id;


--
-- Name: jefe_area; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.jefe_area (
    correo_jefe character varying(150) NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.jefe_area OWNER TO zolrack;

--
-- Name: persona; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.persona (
    id integer NOT NULL,
    apellido character varying(200) NOT NULL,
    contrasena character varying(100) NOT NULL,
    correo character varying(100) NOT NULL,
    nombre character varying(200) NOT NULL,
    numero_celular character varying(100) NOT NULL,
    rol_id smallint NOT NULL
);


ALTER TABLE public.persona OWNER TO zolrack;

--
-- Name: persona_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.persona_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.persona_id_seq OWNER TO zolrack;

--
-- Name: persona_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.persona_id_seq OWNED BY public.persona.id;


--
-- Name: repuesto_solicitud; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.repuesto_solicitud (
    id integer NOT NULL,
    estado_entrega boolean NOT NULL,
    fecha_entrega timestamp without time zone,
    fecha_solicitud timestamp without time zone,
    nombre_repuesto character varying(200) NOT NULL,
    incidencia_id integer
);


ALTER TABLE public.repuesto_solicitud OWNER TO zolrack;

--
-- Name: repuesto_solicitud_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.repuesto_solicitud_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.repuesto_solicitud_id_seq OWNER TO zolrack;

--
-- Name: repuesto_solicitud_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.repuesto_solicitud_id_seq OWNED BY public.repuesto_solicitud.id;


--
-- Name: rol; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.rol (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE public.rol OWNER TO zolrack;

--
-- Name: rol_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.rol_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rol_id_seq OWNER TO zolrack;

--
-- Name: rol_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.rol_id_seq OWNED BY public.rol.id;


--
-- Name: tecnico; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.tecnico (
    cor character varying(150) NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.tecnico OWNER TO zolrack;

--
-- Name: tipo_equipo; Type: TABLE; Schema: public; Owner: zolrack
--

CREATE TABLE public.tipo_equipo (
    id integer NOT NULL,
    nombre character varying(200) NOT NULL
);


ALTER TABLE public.tipo_equipo OWNER TO zolrack;

--
-- Name: tipo_equipo_id_seq; Type: SEQUENCE; Schema: public; Owner: zolrack
--

CREATE SEQUENCE public.tipo_equipo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tipo_equipo_id_seq OWNER TO zolrack;

--
-- Name: tipo_equipo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: zolrack
--

ALTER SEQUENCE public.tipo_equipo_id_seq OWNED BY public.tipo_equipo.id;


--
-- Name: diccionario_falla id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.diccionario_falla ALTER COLUMN id SET DEFAULT nextval('public.diccionario_falla_id_seq'::regclass);


--
-- Name: equipo id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.equipo ALTER COLUMN id SET DEFAULT nextval('public.equipo_id_seq'::regclass);


--
-- Name: estado_equipo id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.estado_equipo ALTER COLUMN id SET DEFAULT nextval('public.estado_equipo_id_seq'::regclass);


--
-- Name: estadoincidencia id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.estadoincidencia ALTER COLUMN id SET DEFAULT nextval('public.estadoincidencia_id_seq'::regclass);


--
-- Name: incidencia id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.incidencia ALTER COLUMN id SET DEFAULT nextval('public.incidencia_id_seq'::regclass);


--
-- Name: informe_tecnico id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.informe_tecnico ALTER COLUMN id SET DEFAULT nextval('public.informe_tecnico_id_seq'::regclass);


--
-- Name: persona id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.persona ALTER COLUMN id SET DEFAULT nextval('public.persona_id_seq'::regclass);


--
-- Name: repuesto_solicitud id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.repuesto_solicitud ALTER COLUMN id SET DEFAULT nextval('public.repuesto_solicitud_id_seq'::regclass);


--
-- Name: rol id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.rol ALTER COLUMN id SET DEFAULT nextval('public.rol_id_seq'::regclass);


--
-- Name: tipo_equipo id; Type: DEFAULT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.tipo_equipo ALTER COLUMN id SET DEFAULT nextval('public.tipo_equipo_id_seq'::regclass);


--
-- Name: diccionario_falla diccionario_falla_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.diccionario_falla
    ADD CONSTRAINT diccionario_falla_pkey PRIMARY KEY (id);


--
-- Name: equipo equipo_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.equipo
    ADD CONSTRAINT equipo_pkey PRIMARY KEY (id);


--
-- Name: estado_equipo estado_equipo_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.estado_equipo
    ADD CONSTRAINT estado_equipo_pkey PRIMARY KEY (id);


--
-- Name: estadoincidencia estadoincidencia_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.estadoincidencia
    ADD CONSTRAINT estadoincidencia_pkey PRIMARY KEY (id);


--
-- Name: incidencia incidencia_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.incidencia
    ADD CONSTRAINT incidencia_pkey PRIMARY KEY (id);


--
-- Name: informe_tecnico informe_tecnico_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.informe_tecnico
    ADD CONSTRAINT informe_tecnico_pkey PRIMARY KEY (id);


--
-- Name: jefe_area jefe_area_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.jefe_area
    ADD CONSTRAINT jefe_area_pkey PRIMARY KEY (id);


--
-- Name: persona persona_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.persona
    ADD CONSTRAINT persona_pkey PRIMARY KEY (id);


--
-- Name: repuesto_solicitud repuesto_solicitud_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.repuesto_solicitud
    ADD CONSTRAINT repuesto_solicitud_pkey PRIMARY KEY (id);


--
-- Name: rol rol_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);


--
-- Name: tecnico tecnico_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.tecnico
    ADD CONSTRAINT tecnico_pkey PRIMARY KEY (id);


--
-- Name: tipo_equipo tipo_equipo_pkey; Type: CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.tipo_equipo
    ADD CONSTRAINT tipo_equipo_pkey PRIMARY KEY (id);


--
-- Name: jefe_area fk3guldpcqx5bdr12ufgbvsn0on; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.jefe_area
    ADD CONSTRAINT fk3guldpcqx5bdr12ufgbvsn0on FOREIGN KEY (id) REFERENCES public.persona(id);


--
-- Name: equipo fk5j4yjuiqww4dsqshstney0f8s; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.equipo
    ADD CONSTRAINT fk5j4yjuiqww4dsqshstney0f8s FOREIGN KEY (tipo_equipo_id) REFERENCES public.tipo_equipo(id);


--
-- Name: repuesto_solicitud fk649hfnyhowoju0krbt1pftv8t; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.repuesto_solicitud
    ADD CONSTRAINT fk649hfnyhowoju0krbt1pftv8t FOREIGN KEY (incidencia_id) REFERENCES public.incidencia(id);


--
-- Name: equipo fk77dgxr9pm61xd41swghwda3ox; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.equipo
    ADD CONSTRAINT fk77dgxr9pm61xd41swghwda3ox FOREIGN KEY (usuario_id) REFERENCES public.persona(id);


--
-- Name: incidencia fk8i3qpbg52x89hmaopt9g39gf; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.incidencia
    ADD CONSTRAINT fk8i3qpbg52x89hmaopt9g39gf FOREIGN KEY (persona_registro_id) REFERENCES public.persona(id);


--
-- Name: tecnico fk9wgjuba4nq2tcnfai2qsmve2c; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.tecnico
    ADD CONSTRAINT fk9wgjuba4nq2tcnfai2qsmve2c FOREIGN KEY (id) REFERENCES public.persona(id);


--
-- Name: equipo fkc08l27jcrcsii35mopxi1p1w9; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.equipo
    ADD CONSTRAINT fkc08l27jcrcsii35mopxi1p1w9 FOREIGN KEY (estado_equipo_id) REFERENCES public.estado_equipo(id);


--
-- Name: diccionario_falla fkgn9nk6o7kuunuc3s9q3svdcea; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.diccionario_falla
    ADD CONSTRAINT fkgn9nk6o7kuunuc3s9q3svdcea FOREIGN KEY (tecnico_id) REFERENCES public.tecnico(id);


--
-- Name: incidencia fkjyr4nsy6y57iov096wcwkeorl; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.incidencia
    ADD CONSTRAINT fkjyr4nsy6y57iov096wcwkeorl FOREIGN KEY (tecnico_id) REFERENCES public.tecnico(id);


--
-- Name: incidencia fklm3wgi7yowjv7b9t9c97vpt46; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.incidencia
    ADD CONSTRAINT fklm3wgi7yowjv7b9t9c97vpt46 FOREIGN KEY (estado_incidencia_id) REFERENCES public.estadoincidencia(id);


--
-- Name: incidencia fkor4imt0iel4h5psa5iiau5tgc; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.incidencia
    ADD CONSTRAINT fkor4imt0iel4h5psa5iiau5tgc FOREIGN KEY (equipo_id) REFERENCES public.equipo(id);


--
-- Name: informe_tecnico fkqdkaisubh33j2xmtx0nq0x3r7; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.informe_tecnico
    ADD CONSTRAINT fkqdkaisubh33j2xmtx0nq0x3r7 FOREIGN KEY (incidencia_id) REFERENCES public.incidencia(id);


--
-- Name: persona fkyoa2iw2ckdnwyw2ibckku8li; Type: FK CONSTRAINT; Schema: public; Owner: zolrack
--

ALTER TABLE ONLY public.persona
    ADD CONSTRAINT fkyoa2iw2ckdnwyw2ibckku8li FOREIGN KEY (rol_id) REFERENCES public.rol(id);


--
-- PostgreSQL database dump complete
--

