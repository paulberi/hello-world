SET search_path TO matdatabas;

-- 2 st mätningstyper till Vattenstånd
INSERT INTO matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10001, 9, 19, NULL, 1, 3, true, 14, 'LevelMala_Avg', false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
INSERT INTO matningstyp(id, matobjekt_id, definition_matningstyp_id, matansvarig_anvandargrupp_id, matintervall_antal_ganger, matintervall_tidsenhet, aktiv, paminnelse_dagar, instrument, granskas_automatiskt, granskas_min, granskas_max, berakning_konstant, berakning_referensniva, max_pejlbart_djup, fixpunkt, enhet, decimaler, skapad_datum, andrad_datum, andrad_av_id)
  VALUES(10002, 9, 20, NULL, 1, 3, true, 14, 'LevelSalt_Avg', false, NULL, NULL, 1, 38.60, 3.02, NULL, 'm', 2, NULL, NULL, NULL);
ALTER SEQUENCE matning_id_seq RESTART WITH 27;
