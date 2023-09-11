DELETE FROM matdatabas.matningstyp;
DELETE FROM matdatabas.definition_matningstyp;
DELETE FROM matdatabas.matobjekt;

INSERT INTO matdatabas.definition_matningstyp(id, matobjekt_typ, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet,	beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, automatisk_granskning, beskrivning, andringsbar,	skapad_datum, andrad_datum, andrad_av_id)
	VALUES (10000, 0, 'Nivå', NULL, 'm', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, false, NULL, true, NULL, NULL, NULL);

-- 4 st mätobjekt
INSERT INTO matdatabas.matobjekt(id, typ, namn, aktiv, kontrollprogram, pos_n, pos_e, fastighet, lage, bifogad_bild_id,	skapad_datum, andrad_datum, andrad_av_id)
	VALUES (10001, 0, 'matobjekt1', true, true, 6577368, 159355, NULL,	NULL, NULL,	NULL, NULL, NULL);
INSERT INTO matdatabas.matobjekt(id, typ, namn, aktiv, kontrollprogram, pos_n, pos_e, fastighet, lage, bifogad_bild_id,	skapad_datum, andrad_datum, andrad_av_id)
	VALUES (10002, 0, 'matobjekt2', true, true, 6577368, 159355, NULL,	NULL, NULL,	NULL, NULL, NULL);
INSERT INTO matdatabas.matobjekt(id, typ, namn, aktiv, kontrollprogram, pos_n, pos_e, fastighet, lage, bifogad_bild_id,	skapad_datum, andrad_datum, andrad_av_id)
	VALUES (10003, 0, 'matobjekt3', true, true, 6577368, 159355, NULL,	NULL, NULL,	NULL, NULL, NULL);
-- inaktivt mätobjekt
INSERT INTO matdatabas.matobjekt(id, typ, namn, aktiv, kontrollprogram, pos_n, pos_e, fastighet, lage, bifogad_bild_id,	skapad_datum, andrad_datum, andrad_av_id)
	VALUES (10004, 0, 'matobjekt4', false, true, 6577368, 159355, NULL,	NULL, NULL,	NULL, NULL, NULL);

-- 1 st mätningstyp till mätobjekt1
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10001, 10001, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);

-- 2 st mätningstyper till mätobjekt2
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10002, 10002, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10003, 10002, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);

-- 3 st mätningstyper till mätobjekt3
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10004, 10003, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10005, 10003, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10006, 10003, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);

-- 1 st mätningstyp till mätobjekt4
INSERT INTO matdatabas.matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10007, 10004, 10000, NULL, 1, 3, true, 14, NULL, false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
