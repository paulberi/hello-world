CREATE OR REPLACE VIEW forestlink.gv_informationslinjer_avv
    WITH (security_barrier=false)
    AS
     SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationslinjer
     JOIN import ON import.id = informationslinjer.import_id AND import.pakettyp = 'Slutavverkning'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationslinjer_gal
    WITH (security_barrier=false)
    AS
     SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationslinjer
     JOIN import ON import.id = informationslinjer.import_id AND import.pakettyp = 'Gallring'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationslinjer_mb
    WITH (security_barrier=false)
    AS
     SELECT informationslinjer.id,
    informationslinjer.import_id,
    informationslinjer.company,
    informationslinjer.type,
    informationslinjer.sort,
    informationslinjer.essay,
    informationslinjer.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationslinjer
     JOIN import ON import.id = informationslinjer.import_id AND import.pakettyp = 'Markberedning'::text;


CREATE OR REPLACE VIEW forestlink.gv_informationspolygoner_avv
    WITH (security_barrier=false)
    AS
     SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationspolygoner
     JOIN import ON import.id = informationspolygoner.import_id AND import.pakettyp = 'Slutavverkning'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationspolygoner_gal
    WITH (security_barrier=false)
    AS
     SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationspolygoner
     JOIN import ON import.id = informationspolygoner.import_id AND import.pakettyp = 'Gallring'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationspolygoner_mb
    WITH (security_barrier=false)
    AS
     SELECT informationspolygoner.id,
    informationspolygoner.import_id,
    informationspolygoner.company,
    informationspolygoner.type,
    informationspolygoner.sort,
    informationspolygoner.essay,
    informationspolygoner.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationspolygoner
     JOIN import ON import.id = informationspolygoner.import_id AND import.pakettyp = 'Markberedning'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationspunkter_avv
    WITH (security_barrier=false)
    AS
     SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationspunkter
     JOIN import ON import.id = informationspunkter.import_id AND import.pakettyp = 'Slutavverkning'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationspunkter_gal
    WITH (security_barrier=false)
    AS
     SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationspunkter
     JOIN import ON import.id = informationspunkter.import_id AND import.pakettyp = 'Gallring'::text;

CREATE OR REPLACE VIEW forestlink.gv_informationspunkter_mb
    WITH (security_barrier=false)
    AS
     SELECT informationspunkter.id,
    informationspunkter.import_id,
    informationspunkter.company,
    informationspunkter.type,
    informationspunkter.sort,
    informationspunkter.essay,
    informationspunkter.geom,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM informationspunkter
     JOIN import ON import.id = informationspunkter.import_id AND import.pakettyp = 'Markberedning'::text;

CREATE OR REPLACE VIEW forestlink.gv_korspar_avv
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM korspar
     JOIN import ON import.id = korspar.import_id AND import.pakettyp = 'Slutavverkning'::text;

CREATE OR REPLACE VIEW forestlink.gv_korspar_gal
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM korspar
     JOIN import ON import.id = korspar.import_id AND import.pakettyp = 'Gallring'::text;

CREATE OR REPLACE VIEW forestlink.gv_korspar_mb
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM korspar
     JOIN import ON import.id = korspar.import_id AND import.pakettyp = 'Markberedning'::text;

CREATE OR REPLACE VIEW forestlink.gv_provytor
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM provytor
     JOIN import ON import.id = provytor.import_id;

CREATE OR REPLACE VIEW forestlink.gv_korspar_for_transport_avv
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM korspar_for_transport
     JOIN import ON import.id = korspar_for_transport.import_id AND import.pakettyp = 'Slutavverkning'::text;

CREATE OR REPLACE VIEW forestlink.gv_korspar_for_transport_gal
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM korspar_for_transport
     JOIN import ON import.id = korspar_for_transport.import_id AND import.pakettyp = 'Gallring'::text;

CREATE OR REPLACE VIEW forestlink.gv_korspar_for_transport_mb
    WITH (security_barrier=false)
    AS
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
    import.objektnummer,
    import.enddate
   FROM korspar_for_transport
     JOIN import ON import.id = korspar_for_transport.import_id AND import.pakettyp = 'Markberedning'::text;

CREATE OR REPLACE VIEW forestlink.gv_resultat_avv
    WITH (security_barrier=false)
    AS
     SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM resultat
     JOIN import ON import.id = resultat.import_id AND import.pakettyp = 'Slutavverkning'::text;

CREATE OR REPLACE VIEW forestlink.gv_resultat_gal
    WITH (security_barrier=false)
    AS
     SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM resultat
     JOIN import ON import.id = resultat.import_id AND import.pakettyp = 'Gallring'::text;

CREATE OR REPLACE VIEW forestlink.gv_resultat_mb
    WITH (security_barrier=false)
    AS
     SELECT resultat.id,
    resultat.import_id,
    resultat.name,
    resultat.area,
    resultat.perimeter,
    resultat.length,
    resultat.geom,
    resultat.status,
    import.organisation,
    import.objektnummer,
    import.enddate
   FROM resultat
     JOIN import ON import.id = resultat.import_id AND import.pakettyp = 'Markberedning'::text;
