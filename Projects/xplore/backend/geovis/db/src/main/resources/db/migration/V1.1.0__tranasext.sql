SET search_path = tranasext, pg_catalog;

ALTER TABLE karta_kulturforening
	DROP CONSTRAINT karta_kulturforening_pkey;

CREATE SEQUENCE adressplatser_tranas_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE adressplatser_tranas_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE adressplatser_tranas_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE adressplatser_tranas_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE adressplatser_tranas_gid_seq TO tranas_read;

CREATE SEQUENCE standard_kommunkarta_punkt_id_seq83
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE standard_kommunkarta_punkt_id_seq83 FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE standard_kommunkarta_punkt_id_seq83 TO geovis;
REVOKE ALL ON SEQUENCE standard_kommunkarta_punkt_id_seq83 FROM tranas_read;
GRANT SELECT ON SEQUENCE standard_kommunkarta_punkt_id_seq83 TO tranas_read;

CREATE SEQUENCE kommunala_bolags_mark_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE kommunala_bolags_mark_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE kommunala_bolags_mark_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE kommunala_bolags_mark_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE kommunala_bolags_mark_gid_seq TO tranas_read;

CREATE SEQUENCE kommunalagd_mark_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE kommunalagd_mark_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE kommunalagd_mark_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE kommunalagd_mark_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE kommunalagd_mark_gid_seq TO tranas_read;

CREATE SEQUENCE sopdistrikttatorten_sweref99tm_gid_seq
	START WITH 14
	INCREMENT BY 1
	MAXVALUE 2147483647
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE sopdistrikttatorten_sweref99tm_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE sopdistrikttatorten_sweref99tm_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE sopdistrikttatorten_sweref99tm_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE sopdistrikttatorten_sweref99tm_gid_seq TO tranas_read;

CREATE SEQUENCE sopdistrikttranas_kmn_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE sopdistrikttranas_kmn_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE sopdistrikttranas_kmn_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE sopdistrikttranas_kmn_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE sopdistrikttranas_kmn_gid_seq TO tranas_read;

CREATE SEQUENCE "strandskydd tranas fr flan_region_gid_seq"
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE "strandskydd tranas fr flan_region_gid_seq" FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE "strandskydd tranas fr flan_region_gid_seq" TO geovis;
REVOKE ALL ON SEQUENCE "strandskydd tranas fr flan_region_gid_seq" FROM tranas_read;
GRANT SELECT ON SEQUENCE "strandskydd tranas fr flan_region_gid_seq" TO tranas_read;

CREATE SEQUENCE svarta_50_ars_flode_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE svarta_50_ars_flode_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE svarta_50_ars_flode_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE svarta_50_ars_flode_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE svarta_50_ars_flode_gid_seq TO tranas_read;

CREATE SEQUENCE svartan_100_ars_flode_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE svartan_100_ars_flode_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE svartan_100_ars_flode_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE svartan_100_ars_flode_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE svartan_100_ars_flode_gid_seq TO tranas_read;

CREATE SEQUENCE svartan_bhf_gid_seq
	AS integer
	START WITH 1
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE svartan_bhf_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE svartan_bhf_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE svartan_bhf_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE svartan_bhf_gid_seq TO tranas_read;

CREATE SEQUENCE tranasext_kladdlager_pkey_seq
	START WITH 19
	INCREMENT BY 1
	NO MAXVALUE
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE tranasext_kladdlager_pkey_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE tranasext_kladdlager_pkey_seq TO geovis;
REVOKE ALL ON SEQUENCE tranasext_kladdlager_pkey_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE tranasext_kladdlager_pkey_seq TO tranas_read;

CREATE SEQUENCE vattenskyddsomr_99tm_gid_seq
	START WITH 5
	INCREMENT BY 1
	MAXVALUE 2147483647
	NO MINVALUE
	CACHE 1;
REVOKE ALL ON SEQUENCE vattenskyddsomr_99tm_gid_seq FROM geovis;
GRANT SELECT, USAGE ON SEQUENCE vattenskyddsomr_99tm_gid_seq TO geovis;
REVOKE ALL ON SEQUENCE vattenskyddsomr_99tm_gid_seq FROM tranas_read;
GRANT SELECT ON SEQUENCE vattenskyddsomr_99tm_gid_seq TO tranas_read;

CREATE TABLE fir_adressplatser_tranas (
	gid integer NOT NULL,
	adressetik character varying(254),
	rnpadrpl integer,
	ridadrpl numeric,
	adrkod character varying(122),
	lankod character varying(4),
	komkod character varying(4),
	adressplat character varying(254),
	komdellage character varying(70),
	popnamn character varying(70),
	gardsnamn character varying(70),
	adromrade character varying(70),
	adrplats character varying(70),
	pnr bigint,
	postort character varying(54),
	karta character varying(22),
	punkttyp character varying(2),
	ruta character varying(16),
	n_sweref99 numeric,
	e_sweref99 numeric,
	xkoord numeric,
	ykoord numeric,
	adrstatus character varying(2),
	fnr bigint,
	fast_bet character varying(100),
	adresspl00 character varying(214),
	adresspl01 character varying(142),
	adresspl02 character varying(214),
	uuid character varying(72),
	avvikerfra character varying(2),
	geom public.geometry(Point,3009)
);

ALTER TABLE fir_adressplatser_tranas OWNER TO "xplore-admin";
REVOKE ALL ON TABLE fir_adressplatser_tranas FROM geovis;
GRANT ALL ON TABLE fir_adressplatser_tranas TO geovis;
REVOKE ALL ON TABLE fir_adressplatser_tranas FROM tranas_read;
GRANT SELECT ON TABLE fir_adressplatser_tranas TO tranas_read;

CREATE TABLE fir_kommunala_bolags_mark (
	gid integer NOT NULL,
	fnr_fds character varying(9),
	fnr_fds_nu bigint,
	externid character varying(64),
	detaljtyp character varying(10),
	kommunkod character varying(4),
	kommunnamn character varying(16),
	trakt character varying(40),
	blockenhet character varying(9),
	omrnr integer,
	fastighet character varying(54),
	ytkval integer,
	adat character varying(16),
	objekt_id character varying(36),
	omrtyp character varying(30),
	fnr bigint,
	senasteand character varying(60),
	inskrivnin character varying(20),
	dagboksnum character varying(20),
	aktnummer character varying(40),
	beslut character varying(100),
	lagfartsna character varying(254),
	idnummer character varying(26),
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE fir_kommunala_bolags_mark OWNER TO "xplore-admin";
REVOKE ALL ON TABLE fir_kommunala_bolags_mark FROM geovis;
GRANT ALL ON TABLE fir_kommunala_bolags_mark TO geovis;
REVOKE ALL ON TABLE fir_kommunala_bolags_mark FROM tranas_read;
GRANT SELECT ON TABLE fir_kommunala_bolags_mark TO tranas_read;

CREATE TABLE fir_kommunalagd_mark (
	gid integer NOT NULL,
	fnr_fds character varying(9),
	fnr_fds_nu bigint,
	externid character varying(64),
	detaljtyp character varying(10),
	kommunkod character varying(4),
	kommunnamn character varying(16),
	trakt character varying(40),
	blockenhet character varying(9),
	omrnr integer,
	fastighet character varying(54),
	ytkval integer,
	adat character varying(16),
	objekt_id character varying(36),
	omrtyp character varying(30),
	fnr bigint,
	senasteand character varying(60),
	inskrivnin character varying(20),
	dagboksnum character varying(20),
	aktnummer character varying(40),
	beslut character varying(100),
	lagfartsna character varying(254),
	idnummer character varying(26),
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE fir_kommunalagd_mark OWNER TO "xplore-admin";
REVOKE ALL ON TABLE fir_kommunalagd_mark FROM geovis;
GRANT ALL ON TABLE fir_kommunalagd_mark TO geovis;
REVOKE ALL ON TABLE fir_kommunalagd_mark FROM tranas_read;
GRANT SELECT ON TABLE fir_kommunalagd_mark TO tranas_read;

CREATE TABLE geoa_sopdistrikttatorten_sweref99tm (
	gid integer NOT NULL,
	geodb_oid double precision,
	objectid double precision,
	beskr character varying(50),
	kommun character varying(50),
	"Ägare" character varying(50),
	link character varying(50),
	shape_leng numeric,
	shape_area numeric,
	geom public.geometry(MultiPolygonZM,3006)
);

ALTER TABLE geoa_sopdistrikttatorten_sweref99tm OWNER TO "xplore-admin";
REVOKE ALL ON TABLE geoa_sopdistrikttatorten_sweref99tm FROM geovis;
GRANT ALL ON TABLE geoa_sopdistrikttatorten_sweref99tm TO geovis;
REVOKE ALL ON TABLE geoa_sopdistrikttatorten_sweref99tm FROM tranas_read;
GRANT SELECT ON TABLE geoa_sopdistrikttatorten_sweref99tm TO tranas_read;

CREATE TABLE geoa_vattenskyddsomr_99tm (
	gid integer NOT NULL,
	nvrid character varying(254),
	namn character varying(254),
	skyddstyp character varying(254),
	beslstatus character varying(254),
	ursbesldat date,
	ikraftdatf date,
	ursgalldat date,
	sengalldat date,
	tillsynsmh character varying(254),
	provnmhdis character varying(254),
	provnmhtil character varying(254),
	lan character varying(254),
	kommun character varying(254),
	iucnkat character varying(254),
	forvaltare character varying(254),
	area_ha numeric,
	land_ha numeric,
	vatten_ha numeric,
	skog_ha numeric,
	geostatus character varying(254),
	diarienr character varying(254),
	lagrum character varying(254),
	beslmynd character varying(254),
	geom public.geometry(MultiPolygon,3006)
);

ALTER TABLE geoa_vattenskyddsomr_99tm OWNER TO "xplore-admin";
REVOKE ALL ON TABLE geoa_vattenskyddsomr_99tm FROM geovis;
GRANT ALL ON TABLE geoa_vattenskyddsomr_99tm TO geovis;
REVOKE ALL ON TABLE geoa_vattenskyddsomr_99tm FROM tranas_read;
GRANT SELECT ON TABLE geoa_vattenskyddsomr_99tm TO tranas_read;

ALTER TABLE tranasext."karta_uppstallningsplats-husbilar"
    RENAME TO karta_uppstallningsplats;

ALTER TABLE karta_uppstallningsplats OWNER TO "xplore-admin";
REVOKE ALL ON TABLE karta_uppstallningsplats FROM geovis;
GRANT ALL ON TABLE karta_uppstallningsplats TO geovis;
REVOKE ALL ON TABLE karta_uppstallningsplats FROM tranas_read;
GRANT SELECT ON TABLE karta_uppstallningsplats TO tranas_read;

CREATE TABLE kladdlager (
	id integer NOT NULL,
	skapad timestamp with time zone NOT NULL,
	geometry public.geometry NOT NULL,
	skapad_av character varying(40),
	stil json
);

ALTER TABLE kladdlager OWNER TO "xplore-admin";
REVOKE ALL ON TABLE kladdlager FROM geovis;
GRANT ALL ON TABLE kladdlager TO geovis;
REVOKE ALL ON TABLE kladdlager FROM tranas_read;
GRANT SELECT ON TABLE kladdlager TO tranas_read;

CREATE TABLE "lansstyrelsen_strandskydd tranas fr flan_region" (
	gid integer NOT NULL,
	obj_idnr numeric,
	obj_typkod character varying(20),
	rev_datum character varying(8),
	rev_av character varying(4),
	typ character varying(50),
	avstand_m character varying(16),
	shape_area numeric,
	shape_len numeric,
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE "lansstyrelsen_strandskydd tranas fr flan_region" OWNER TO "xplore-admin";
REVOKE ALL ON TABLE "lansstyrelsen_strandskydd tranas fr flan_region" FROM geovis;
GRANT ALL ON TABLE "lansstyrelsen_strandskydd tranas fr flan_region" TO geovis;
REVOKE ALL ON TABLE "lansstyrelsen_strandskydd tranas fr flan_region" FROM tranas_read;
GRANT SELECT ON TABLE "lansstyrelsen_strandskydd tranas fr flan_region" TO tranas_read;

CREATE TABLE tranas_kommun_sopdistrikttranas_kmn (
	gid integer NOT NULL,
	distrikt character varying(20),
	tomning character varying(50),
	kommun character varying(50),
	shape_leng numeric,
	shape_area numeric,
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE tranas_kommun_sopdistrikttranas_kmn OWNER TO "xplore-admin";
REVOKE ALL ON TABLE tranas_kommun_sopdistrikttranas_kmn FROM geovis;
GRANT ALL ON TABLE tranas_kommun_sopdistrikttranas_kmn TO geovis;
REVOKE ALL ON TABLE tranas_kommun_sopdistrikttranas_kmn FROM tranas_read;
GRANT SELECT ON TABLE tranas_kommun_sopdistrikttranas_kmn TO tranas_read;

CREATE TABLE tranas_kommun_svarta_50_ars_flode (
	gid integer NOT NULL,
	gridcode smallint,
	area double precision,
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE tranas_kommun_svarta_50_ars_flode OWNER TO "xplore-admin";
REVOKE ALL ON TABLE tranas_kommun_svarta_50_ars_flode FROM geovis;
GRANT ALL ON TABLE tranas_kommun_svarta_50_ars_flode TO geovis;
REVOKE ALL ON TABLE tranas_kommun_svarta_50_ars_flode FROM tranas_read;
GRANT SELECT ON TABLE tranas_kommun_svarta_50_ars_flode TO tranas_read;

CREATE TABLE tranas_kommun_svartan_100_ars_flode (
	gid integer NOT NULL,
	gridcode bigint,
	area double precision,
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE tranas_kommun_svartan_100_ars_flode OWNER TO "xplore-admin";
REVOKE ALL ON TABLE tranas_kommun_svartan_100_ars_flode FROM geovis;
GRANT ALL ON TABLE tranas_kommun_svartan_100_ars_flode TO geovis;
REVOKE ALL ON TABLE tranas_kommun_svartan_100_ars_flode FROM tranas_read;
GRANT SELECT ON TABLE tranas_kommun_svartan_100_ars_flode TO tranas_read;

CREATE TABLE tranas_kommun_svartan_bhf (
	gid integer NOT NULL,
	gridcode smallint,
	area double precision,
	geom public.geometry(MultiPolygon,3009)
);

ALTER TABLE tranas_kommun_svartan_bhf OWNER TO "xplore-admin";
REVOKE ALL ON TABLE tranas_kommun_svartan_bhf FROM geovis;
GRANT ALL ON TABLE tranas_kommun_svartan_bhf TO geovis;
REVOKE ALL ON TABLE tranas_kommun_svartan_bhf FROM tranas_read;
GRANT SELECT ON TABLE tranas_kommun_svartan_bhf TO tranas_read;

ALTER TABLE karta_kulturforening
	ALTER COLUMN id SET DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq78'::regclass);

ALTER TABLE karta_kulturskolan
	ALTER COLUMN id SET DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq69'::regclass);

ALTER TABLE karta_torghandel
	ALTER COLUMN id SET DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq83'::regclass),
	ALTER COLUMN geom TYPE public.geometry(MultiPoint,3009) USING geom::public.geometry(MultiPoint,3009) /* TYPE change - table: karta_torghandel original: public.geometry(MultiPolygon,3009) new: public.geometry(MultiPoint,3009) */;

DROP SEQUENCE standard_kommunkarta_yta_id_seq4;

ALTER SEQUENCE adressplatser_tranas_gid_seq
	OWNED BY tranasext.fir_adressplatser_tranas.gid;

ALTER SEQUENCE kommunala_bolags_mark_gid_seq
	OWNED BY tranasext.fir_kommunala_bolags_mark.gid;

ALTER SEQUENCE kommunalagd_mark_gid_seq
	OWNED BY tranasext.fir_kommunalagd_mark.gid;

ALTER SEQUENCE sopdistrikttatorten_sweref99tm_gid_seq
	OWNED BY tranasext.geoa_sopdistrikttatorten_sweref99tm.gid;

ALTER SEQUENCE sopdistrikttranas_kmn_gid_seq
	OWNED BY tranasext.tranas_kommun_sopdistrikttranas_kmn.gid;

ALTER SEQUENCE "strandskydd tranas fr flan_region_gid_seq"
	OWNED BY tranasext."lansstyrelsen_strandskydd tranas fr flan_region".gid;

ALTER SEQUENCE svarta_50_ars_flode_gid_seq
	OWNED BY tranasext.tranas_kommun_svarta_50_ars_flode.gid;

ALTER SEQUENCE svartan_100_ars_flode_gid_seq
	OWNED BY tranasext.tranas_kommun_svartan_100_ars_flode.gid;

ALTER SEQUENCE tranasext_kladdlager_pkey_seq
	OWNED BY tranasext.kladdlager.id;

ALTER SEQUENCE vattenskyddsomr_99tm_gid_seq
	OWNED BY tranasext.geoa_vattenskyddsomr_99tm.gid;


ALTER TABLE ONLY tranasext.fir_adressplatser_tranas ALTER COLUMN gid SET DEFAULT nextval('tranasext.adressplatser_tranas_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.fir_kommunala_bolags_mark ALTER COLUMN gid SET DEFAULT nextval('tranasext.kommunala_bolags_mark_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.fir_kommunalagd_mark ALTER COLUMN gid SET DEFAULT nextval('tranasext.kommunalagd_mark_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.geoa_sopdistrikttatorten_sweref99tm ALTER COLUMN gid SET DEFAULT nextval('tranasext.sopdistrikttatorten_sweref99tm_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.geoa_vattenskyddsomr_99tm ALTER COLUMN gid SET DEFAULT nextval('tranasext.vattenskyddsomr_99tm_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.kladdlager ALTER COLUMN id SET DEFAULT nextval('tranasext.tranasext_kladdlager_pkey_seq'::regclass);

ALTER TABLE ONLY tranasext."lansstyrelsen_strandskydd tranas fr flan_region" ALTER COLUMN gid SET DEFAULT nextval('tranasext."strandskydd tranas fr flan_region_gid_seq"'::regclass);

ALTER TABLE ONLY tranasext.tranas_kommun_sopdistrikttranas_kmn ALTER COLUMN gid SET DEFAULT nextval('tranasext.sopdistrikttranas_kmn_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.tranas_kommun_svarta_50_ars_flode ALTER COLUMN gid SET DEFAULT nextval('tranasext.svarta_50_ars_flode_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.tranas_kommun_svartan_100_ars_flode ALTER COLUMN gid SET DEFAULT nextval('tranasext.svartan_100_ars_flode_gid_seq'::regclass);

ALTER TABLE ONLY tranasext.tranas_kommun_svartan_bhf ALTER COLUMN gid SET DEFAULT nextval('tranasext.svartan_bhf_gid_seq'::regclass);


CREATE INDEX adressplatser_tranas_geom_idx ON tranasext.fir_adressplatser_tranas USING gist (geom);

CREATE INDEX kommunala_bolags_mark_geom_idx ON tranasext.fir_kommunala_bolags_mark USING gist (geom);

CREATE INDEX kommunalagd_mark_geom_idx ON tranasext.fir_kommunalagd_mark USING gist (geom);

SET default_tablespace = geovisdb_ts;

CREATE INDEX sopdistrikttestorten_sweref99tm_geom_idx ON tranasext.geoa_sopdistrikttatorten_sweref99tm USING gist (geom);

SET default_tablespace = '';

CREATE INDEX sopdistrikttranas_kmn_geom_idx ON tranasext.tranas_kommun_sopdistrikttranas_kmn USING gist (geom);

CREATE INDEX "strandskydd tranas fr flan_region_geom_idx" ON tranasext."lansstyrelsen_strandskydd tranas fr flan_region" USING gist (geom);

CREATE INDEX svarta_50_ars_flode_geom_idx ON tranasext.tranas_kommun_svarta_50_ars_flode USING gist (geom);

CREATE INDEX svartan_100_ars_flode_geom_idx ON tranasext.tranas_kommun_svartan_100_ars_flode USING gist (geom);

CREATE INDEX svartan_bhf_geom_idx ON tranasext.tranas_kommun_svartan_bhf USING gist (geom);

SET default_tablespace = geovisdb_ts;

CREATE INDEX vattenskyddsomr_99tm_geom_idx ON tranasext.geoa_vattenskyddsomr_99tm USING gist (geom);

CREATE FUNCTION tranasext.trigger_func_skapad_kladdlager()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
  NEW.skapad = NOW();
  RETURN NEW;
END;
$BODY$;

ALTER FUNCTION tranasext.trigger_func_skapad_kladdlager()
    OWNER TO xplore;



CREATE TRIGGER set_skapad_on_insert_trigger BEFORE INSERT ON tranasext.kladdlager FOR EACH ROW EXECUTE PROCEDURE tranasext.trigger_func_skapad_kladdlager();

CREATE OR REPLACE FUNCTION trigger_func_skapad_kladdlager() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
  NEW.skapad = NOW();
  RETURN NEW;
END;
$$;

ALTER TABLE fir_kommunala_bolags_mark
	ADD CONSTRAINT kommunala_bolags_mark_pkey PRIMARY KEY (gid);

ALTER TABLE fir_kommunalagd_mark
	ADD CONSTRAINT kommunalagd_mark_pkey PRIMARY KEY (gid);

ALTER TABLE geoa_sopdistrikttatorten_sweref99tm
	ADD CONSTRAINT sopdistrikttestorten_sweref99tm_pkey PRIMARY KEY (gid);

ALTER TABLE geoa_vattenskyddsomr_99tm
	ADD CONSTRAINT vattenskyddsomr_99tm_pkey PRIMARY KEY (gid);

ALTER TABLE karta_kulturforening
	ADD CONSTRAINT standard_kommunkarta_punkt_pkey78 PRIMARY KEY (id);

ALTER TABLE kladdlager
	ADD CONSTRAINT kladdlager_test_pkey PRIMARY KEY (id);

ALTER TABLE "lansstyrelsen_strandskydd tranas fr flan_region"
	ADD CONSTRAINT "strandskydd tranas fr flan_region_pkey" PRIMARY KEY (gid);

ALTER TABLE tranas_kommun_sopdistrikttranas_kmn
	ADD CONSTRAINT sopdistrikttranas_kmn_pkey PRIMARY KEY (gid);

ALTER TABLE tranas_kommun_svarta_50_ars_flode
	ADD CONSTRAINT svarta_50_ars_flode_pkey PRIMARY KEY (gid);

ALTER TABLE tranas_kommun_svartan_100_ars_flode
	ADD CONSTRAINT svartan_100_ars_flode_pkey PRIMARY KEY (gid);

ALTER TABLE tranas_kommun_svartan_bhf
	ADD CONSTRAINT svartan_bhf_pkey PRIMARY KEY (gid);

ALTER TABLE fir_adressplatser_tranas
    ADD CONSTRAINT adressplatser_tranas_pkey PRIMARY KEY (gid);
