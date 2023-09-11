SET search_path = forestlink, pg_catalog;

DROP VIEW gv_informationslinjer_avv;

DROP VIEW gv_informationslinjer_gal;

DROP VIEW gv_informationslinjer_mb;

DROP VIEW gv_informationspolygoner_avv;

DROP VIEW gv_informationspolygoner_gal;

DROP VIEW gv_informationspolygoner_mb;

DROP VIEW gv_informationspunkter_avv;

DROP VIEW gv_informationspunkter_gal;

DROP VIEW gv_informationspunkter_mb;

DROP VIEW gv_korspar_avv;

DROP VIEW gv_korspar_for_transport_avv;

DROP VIEW gv_korspar_for_transport_gal;

DROP VIEW gv_korspar_for_transport_mb;

DROP VIEW gv_korspar_gal;

DROP VIEW gv_korspar_mb;

DROP VIEW gv_provytor;

DROP VIEW gv_resultat_avv;

DROP VIEW gv_resultat_gal;

DROP VIEW gv_resultat_mb;

REVOKE ALL ON SEQUENCE import_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE import_seq TO forestlink;

REVOKE ALL ON SEQUENCE import_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE informationslinjer_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE informationslinjer_seq TO forestlink;

REVOKE ALL ON SEQUENCE informationslinjer_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE informationspolygoner_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE informationspolygoner_seq TO forestlink;

REVOKE ALL ON SEQUENCE informationspolygoner_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE informationspunkter_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE informationspunkter_seq TO forestlink;

REVOKE ALL ON SEQUENCE informationspunkter_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE korspar_for_transport_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE korspar_for_transport_seq TO forestlink;

REVOKE ALL ON SEQUENCE korspar_for_transport_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE korspar_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE korspar_seq TO forestlink;

REVOKE ALL ON SEQUENCE korspar_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE provytor_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE provytor_seq TO forestlink;

REVOKE ALL ON SEQUENCE provytor_seq FROM read_forestlink;

REVOKE ALL ON SEQUENCE resultat_seq FROM forestlink;
GRANT SELECT, USAGE ON SEQUENCE resultat_seq TO forestlink;

REVOKE ALL ON SEQUENCE resultat_seq FROM read_forestlink;

REVOKE ALL ON TABLE "import" FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE "import" TO forestlink;
REVOKE ALL ON TABLE "import" FROM read_forestlink;

REVOKE ALL ON TABLE informationslinjer FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE informationslinjer TO forestlink;
REVOKE ALL ON TABLE informationslinjer FROM read_forestlink;

REVOKE ALL ON TABLE informationspolygoner FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE informationspolygoner TO forestlink;
REVOKE ALL ON TABLE informationspolygoner FROM read_forestlink;

REVOKE ALL ON TABLE informationspunkter FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE informationspunkter TO forestlink;
REVOKE ALL ON TABLE informationspunkter FROM read_forestlink;

REVOKE ALL ON TABLE korspar FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE korspar TO forestlink;
REVOKE ALL ON TABLE korspar FROM read_forestlink;

REVOKE ALL ON TABLE korspar_for_transport FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE korspar_for_transport TO forestlink;
REVOKE ALL ON TABLE korspar_for_transport FROM read_forestlink;

REVOKE ALL ON TABLE provytor FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE provytor TO forestlink;
REVOKE ALL ON TABLE provytor FROM read_forestlink;

REVOKE ALL ON TABLE resultat FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE resultat TO forestlink;
REVOKE ALL ON TABLE resultat FROM read_forestlink;

REVOKE ALL ON TABLE importdata FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE importdata TO forestlink;
REVOKE ALL ON TABLE importdata FROM read_forestlink;

REVOKE ALL ON TABLE importstatus FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE importstatus TO forestlink;
REVOKE ALL ON TABLE importstatus FROM read_forestlink;

CREATE VIEW gv_informationslinjer_avv WITH (security_barrier='false') AS
	SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationslinjer
     JOIN forestlink.import ON (((import.id = informationslinjer.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));
REVOKE ALL ON TABLE gv_informationslinjer_avv FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationslinjer_avv TO c_forestlink;
REVOKE ALL ON TABLE gv_informationslinjer_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationslinjer_avv TO forestlink;

CREATE VIEW gv_informationslinjer_gal WITH (security_barrier='false') AS
	SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationslinjer
     JOIN forestlink.import ON (((import.id = informationslinjer.import_id) AND (import.pakettyp = 'Gallring'::text))));
REVOKE ALL ON TABLE gv_informationslinjer_gal FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationslinjer_gal TO c_forestlink;
REVOKE ALL ON TABLE gv_informationslinjer_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationslinjer_gal TO forestlink;

CREATE VIEW gv_informationslinjer_mb WITH (security_barrier='false') AS
	SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationslinjer
     JOIN forestlink.import ON (((import.id = informationslinjer.import_id) AND (import.pakettyp = 'Markberedning'::text))));
REVOKE ALL ON TABLE gv_informationslinjer_mb FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationslinjer_mb TO c_forestlink;
REVOKE ALL ON TABLE gv_informationslinjer_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationslinjer_mb TO forestlink;

CREATE VIEW gv_informationspolygoner_avv WITH (security_barrier='false') AS
	SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationspolygoner
     JOIN forestlink.import ON (((import.id = informationspolygoner.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));
REVOKE ALL ON TABLE gv_informationspolygoner_avv FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationspolygoner_avv TO c_forestlink;
REVOKE ALL ON TABLE gv_informationspolygoner_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspolygoner_avv TO forestlink;

CREATE VIEW gv_informationspolygoner_gal WITH (security_barrier='false') AS
	SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationspolygoner
     JOIN forestlink.import ON (((import.id = informationspolygoner.import_id) AND (import.pakettyp = 'Gallring'::text))));
REVOKE ALL ON TABLE gv_informationspolygoner_gal FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationspolygoner_gal TO c_forestlink;
REVOKE ALL ON TABLE gv_informationspolygoner_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspolygoner_gal TO forestlink;

CREATE VIEW gv_informationspolygoner_mb WITH (security_barrier='false') AS
	SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationspolygoner
     JOIN forestlink.import ON (((import.id = informationspolygoner.import_id) AND (import.pakettyp = 'Markberedning'::text))));
REVOKE ALL ON TABLE gv_informationspolygoner_mb FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationspolygoner_mb TO c_forestlink;
REVOKE ALL ON TABLE gv_informationspolygoner_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspolygoner_mb TO forestlink;

CREATE VIEW gv_informationspunkter_avv WITH (security_barrier='false') AS
	SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationspunkter
     JOIN forestlink.import ON (((import.id = informationspunkter.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));
REVOKE ALL ON TABLE gv_informationspunkter_avv FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationspunkter_avv TO c_forestlink;
REVOKE ALL ON TABLE gv_informationspunkter_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspunkter_avv TO forestlink;

CREATE VIEW gv_informationspunkter_gal WITH (security_barrier='false') AS
	SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationspunkter
     JOIN forestlink.import ON (((import.id = informationspunkter.import_id) AND (import.pakettyp = 'Gallring'::text))));
REVOKE ALL ON TABLE gv_informationspunkter_gal FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationspunkter_gal TO c_forestlink;
REVOKE ALL ON TABLE gv_informationspunkter_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspunkter_gal TO forestlink;

CREATE VIEW gv_informationspunkter_mb WITH (security_barrier='false') AS
	SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.informationspunkter
     JOIN forestlink.import ON (((import.id = informationspunkter.import_id) AND (import.pakettyp = 'Markberedning'::text))));
REVOKE ALL ON TABLE gv_informationspunkter_mb FROM c_forestlink;
GRANT SELECT ON TABLE gv_informationspunkter_mb TO c_forestlink;
REVOKE ALL ON TABLE gv_informationspunkter_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspunkter_mb TO forestlink;

CREATE VIEW gv_korspar_avv WITH (security_barrier='false') AS
	SELECT korspar.id,
    korspar.import_id,
    korspar.name,
    korspar.activity,
    korspar.starttime,
    korspar.endtime,
    korspar.corr_status,
    korspar.agg_status,
    korspar.operator,
    korspar.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.korspar
     JOIN forestlink.import ON (((import.id = korspar.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));
REVOKE ALL ON TABLE gv_korspar_avv FROM c_forestlink;
GRANT SELECT ON TABLE gv_korspar_avv TO c_forestlink;
REVOKE ALL ON TABLE gv_korspar_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_avv TO forestlink;

CREATE VIEW gv_korspar_for_transport_avv WITH (security_barrier='false') AS
	SELECT korspar_for_transport.id,
    korspar_for_transport.import_id,
    korspar_for_transport.name,
    korspar_for_transport.activity,
    korspar_for_transport.starttime,
    korspar_for_transport.endtime,
    korspar_for_transport.corr_status,
    korspar_for_transport.agg_status,
    korspar_for_transport.operator,
    korspar_for_transport.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.korspar_for_transport
     JOIN forestlink.import ON (((import.id = korspar_for_transport.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));
REVOKE ALL ON TABLE gv_korspar_for_transport_avv FROM c_forestlink;
GRANT SELECT ON TABLE gv_korspar_for_transport_avv TO c_forestlink;
REVOKE ALL ON TABLE gv_korspar_for_transport_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_for_transport_avv TO forestlink;

CREATE VIEW gv_korspar_for_transport_gal WITH (security_barrier='false') AS
	SELECT korspar_for_transport.id,
    korspar_for_transport.import_id,
    korspar_for_transport.name,
    korspar_for_transport.activity,
    korspar_for_transport.starttime,
    korspar_for_transport.endtime,
    korspar_for_transport.corr_status,
    korspar_for_transport.agg_status,
    korspar_for_transport.operator,
    korspar_for_transport.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.korspar_for_transport
     JOIN forestlink.import ON (((import.id = korspar_for_transport.import_id) AND (import.pakettyp = 'Gallring'::text))));
REVOKE ALL ON TABLE gv_korspar_for_transport_gal FROM c_forestlink;
GRANT SELECT ON TABLE gv_korspar_for_transport_gal TO c_forestlink;
REVOKE ALL ON TABLE gv_korspar_for_transport_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_for_transport_gal TO forestlink;

CREATE VIEW gv_korspar_for_transport_mb WITH (security_barrier='false') AS
	SELECT korspar_for_transport.id,
    korspar_for_transport.import_id,
    korspar_for_transport.name,
    korspar_for_transport.activity,
    korspar_for_transport.starttime,
    korspar_for_transport.endtime,
    korspar_for_transport.corr_status,
    korspar_for_transport.agg_status,
    korspar_for_transport.operator,
    korspar_for_transport.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.korspar_for_transport
     JOIN forestlink.import ON (((import.id = korspar_for_transport.import_id) AND (import.pakettyp = 'Markberedning'::text))));
REVOKE ALL ON TABLE gv_korspar_for_transport_mb FROM c_forestlink;
GRANT SELECT ON TABLE gv_korspar_for_transport_mb TO c_forestlink;
REVOKE ALL ON TABLE gv_korspar_for_transport_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_for_transport_mb TO forestlink;

CREATE VIEW gv_korspar_gal WITH (security_barrier='false') AS
	SELECT korspar.id,
    korspar.import_id,
    korspar.name,
    korspar.activity,
    korspar.starttime,
    korspar.endtime,
    korspar.corr_status,
    korspar.agg_status,
    korspar.operator,
    korspar.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.korspar
     JOIN forestlink.import ON (((import.id = korspar.import_id) AND (import.pakettyp = 'Gallring'::text))));
REVOKE ALL ON TABLE gv_korspar_gal FROM c_forestlink;
GRANT SELECT ON TABLE gv_korspar_gal TO c_forestlink;
REVOKE ALL ON TABLE gv_korspar_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_gal TO forestlink;

CREATE VIEW gv_korspar_mb WITH (security_barrier='false') AS
	SELECT korspar.id,
    korspar.import_id,
    korspar.name,
    korspar.activity,
    korspar.starttime,
    korspar.endtime,
    korspar.corr_status,
    korspar.agg_status,
    korspar.operator,
    korspar.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.korspar
     JOIN forestlink.import ON (((import.id = korspar.import_id) AND (import.pakettyp = 'Markberedning'::text))));
REVOKE ALL ON TABLE gv_korspar_mb FROM c_forestlink;
GRANT SELECT ON TABLE gv_korspar_mb TO c_forestlink;
REVOKE ALL ON TABLE gv_korspar_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_mb TO forestlink;

CREATE VIEW gv_provytor WITH (security_barrier='false') AS
	SELECT provytor.id,
    provytor.import_id,
    provytor.driver,
    provytor.marktyp,
    provytor.kommentar,
    provytor.h3,
    provytor.m3,
    provytor.t3,
    provytor.fyra,
    provytor.fem,
    provytor.sjuxsju,
    provytor.laagor,
    provytor.sm,
    provytor.lf,
    provytor.mp,
    provytor.x,
    provytor.y,
    provytor.z,
    provytor.fc_olcolor,
    provytor.fc_olsize,
    provytor.fc_style,
    provytor.fc_color,
    provytor.geom,
    import.organisation,
    import.objektnummer
   FROM (forestlink.provytor
     JOIN forestlink.import ON ((import.id = provytor.import_id)));
REVOKE ALL ON TABLE gv_provytor FROM c_forestlink;
GRANT SELECT ON TABLE gv_provytor TO c_forestlink;
REVOKE ALL ON TABLE gv_provytor FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_provytor TO forestlink;

CREATE VIEW gv_resultat_avv WITH (security_barrier='false') AS
	SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation,
    import.objektnummer
   FROM (forestlink.resultat
     JOIN forestlink.import ON (((import.id = resultat.import_id) AND (import.pakettyp = 'Slutavverkning'::text))));
REVOKE ALL ON TABLE gv_resultat_avv FROM c_forestlink;
GRANT SELECT ON TABLE gv_resultat_avv TO c_forestlink;
REVOKE ALL ON TABLE gv_resultat_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_resultat_avv TO forestlink;

CREATE VIEW gv_resultat_gal WITH (security_barrier='false') AS
	SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation,
    import.objektnummer
   FROM (forestlink.resultat
     JOIN forestlink.import ON (((import.id = resultat.import_id) AND (import.pakettyp = 'Gallring'::text))));
REVOKE ALL ON TABLE gv_resultat_gal FROM c_forestlink;
GRANT SELECT ON TABLE gv_resultat_gal TO c_forestlink;
REVOKE ALL ON TABLE gv_resultat_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_resultat_gal TO forestlink;

CREATE VIEW gv_resultat_mb WITH (security_barrier='false') AS
	SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation,
    import.objektnummer
   FROM (forestlink.resultat
     JOIN forestlink.import ON (((import.id = resultat.import_id) AND (import.pakettyp = 'Markberedning'::text))));
REVOKE ALL ON TABLE gv_resultat_mb FROM c_forestlink;
GRANT SELECT ON TABLE gv_resultat_mb TO c_forestlink;
REVOKE ALL ON TABLE gv_resultat_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_resultat_mb TO forestlink;

REVOKE ALL ON TABLE gv_informationslinjer_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationslinjer_avv TO forestlink;

REVOKE ALL ON TABLE gv_informationslinjer_avv FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationslinjer_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationslinjer_gal TO forestlink;

REVOKE ALL ON TABLE gv_informationslinjer_gal FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationslinjer_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationslinjer_mb TO forestlink;

REVOKE ALL ON TABLE gv_informationslinjer_mb FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationspolygoner_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspolygoner_avv TO forestlink;

REVOKE ALL ON TABLE gv_informationspolygoner_avv FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationspolygoner_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspolygoner_gal TO forestlink;

REVOKE ALL ON TABLE gv_informationspolygoner_gal FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationspolygoner_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspolygoner_mb TO forestlink;

REVOKE ALL ON TABLE gv_informationspolygoner_mb FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationspunkter_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspunkter_avv TO forestlink;

REVOKE ALL ON TABLE gv_informationspunkter_avv FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationspunkter_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspunkter_gal TO forestlink;

REVOKE ALL ON TABLE gv_informationspunkter_gal FROM read_forestlink;

REVOKE ALL ON TABLE gv_informationspunkter_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_informationspunkter_mb TO forestlink;

REVOKE ALL ON TABLE gv_informationspunkter_mb FROM read_forestlink;

REVOKE ALL ON TABLE gv_korspar_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_avv TO forestlink;

REVOKE ALL ON TABLE gv_korspar_avv FROM read_forestlink;

REVOKE ALL ON TABLE gv_korspar_for_transport_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_for_transport_avv TO forestlink;

REVOKE ALL ON TABLE gv_korspar_for_transport_avv FROM read_forestlink;

REVOKE ALL ON TABLE gv_korspar_for_transport_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_for_transport_gal TO forestlink;

REVOKE ALL ON TABLE gv_korspar_for_transport_gal FROM read_forestlink;

REVOKE ALL ON TABLE gv_korspar_for_transport_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_for_transport_mb TO forestlink;

REVOKE ALL ON TABLE gv_korspar_for_transport_mb FROM read_forestlink;

REVOKE ALL ON TABLE gv_korspar_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_gal TO forestlink;

REVOKE ALL ON TABLE gv_korspar_gal FROM read_forestlink;

REVOKE ALL ON TABLE gv_korspar_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_korspar_mb TO forestlink;

REVOKE ALL ON TABLE gv_korspar_mb FROM read_forestlink;

REVOKE ALL ON TABLE gv_provytor FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_provytor TO forestlink;

REVOKE ALL ON TABLE gv_provytor FROM read_forestlink;

REVOKE ALL ON TABLE gv_resultat_avv FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_resultat_avv TO forestlink;

REVOKE ALL ON TABLE gv_resultat_avv FROM read_forestlink;

REVOKE ALL ON TABLE gv_resultat_gal FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_resultat_gal TO forestlink;

REVOKE ALL ON TABLE gv_resultat_gal FROM read_forestlink;

REVOKE ALL ON TABLE gv_resultat_mb FROM forestlink;
GRANT SELECT, INSERT, UPDATE ON TABLE gv_resultat_mb TO forestlink;

REVOKE ALL ON TABLE gv_resultat_mb FROM read_forestlink;
