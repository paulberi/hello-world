SET search_path TO matdatabas;

-- 2 st mätningstyper till Vattenstånd
INSERT INTO matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10001, 1, 3, NULL, 1, 3, true, 14, '1879', false, NULL, NULL, 0.05, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
INSERT INTO matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10002, 1, 4, NULL, 1, 3, true, 14, '1879', false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
