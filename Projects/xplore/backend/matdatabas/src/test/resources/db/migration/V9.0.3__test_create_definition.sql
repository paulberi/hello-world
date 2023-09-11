
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (0, 'NIVA_NEDMATNING', 'Nivå', 'Nedmätning', 'm', 2, 'Nivå', 'm', 2, 'Flödar, Torr, Fruset, Hinder, Annat fel', 0, false, 'Mätning av grundvattennivå med hjälp av lod', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (0, 'NIVA_PORTRYCK', 'Nivå', 'Portryck', 'hPa', 2, 'Nivå', 'm', 2, 'Flödar, Torr, Fruset, Hinder, Annat fel', 0, false, 'Mätning av grundvattennivå med hjälp av portryck', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (0, 'NIVA_VATTEN_LUFTTRYCK', 'Nivå', 'Vattentryck', 'cmvp', 2, 'Nivå', 'm', 2, 'Hinder, Annat fel', 0, true, 'Mätning av grundvattennivå med hjälp av vattentryck och lufttryck (separat mätning)', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (0, 'LUFTTRYCK_PER_TIMME', 'Lufttryck per timme', 'Lufttryck', 'hPa', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, true, 'Mätning av lufttryck för beräkning av grundvattennivå', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (1, 'INFILTRATION_MOMENTANT_FLODE', 'Infiltration (momentant flöde)', 'Flöde', 'l/min', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, 'Mätning av momentant vattenflöde med hjälp av flödesmätare och klocka', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (1, 'INFILTRATION_MEDEL_FLODE', 'Infiltratio', 'Volym', 'm³', 2, 'Flöde', 'l/min', 2, 'Hinder, Annat fel', 1, true, 'Mätning av vattenvolym som passerat mätaren sedan den installerats. Beräkning av medelflöde sker genom att jämföra med föregående mätvärde (skillnad i volym/tid)', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (3, 'TUNNELVATTEN_MOMENTANT_FLODE', 'Tunnelvatten', 'Flöde', 'l/min', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, 'Mätning av momentant vattenflöde med hjälp av t.ex hink och klocka', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (3, 'TUNNELVATTEN_MEDEL_FLODE', 'Tunnelvatten', 'Volym', 'm³', 2, 'Flöde', 'l/min', 2, 'Hinder, Annat fel', 1, true, 'Mätning av vattenvolym som passerat mätaren sedan den installerats. Beräkning av medelflöde sker genom att jämföra med föregående mätvärde (skillnad i volym/tid)', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (2, 'SATTNING', 'Sättning', 'Rörelse', 'm', 3, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, 'Mätning av rörelse i höjdled för sättningar', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'pH', NULL, '-', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Suspenderat material', NULL, 'mg/l', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Konduktivitet', NULL, 'mS/m', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Totalt kväve', NULL, 'mg/l', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Oljeindex', NULL, 'mg/l', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Bly', NULL, 'mg/l', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Escherichia coli', NULL, 'cfu/100 ml', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (5, NULL, 'Dygnsnederbörd', 'Nederbörd', 'mm', 1, NULL, NULL, NULL, 'Annat fel', 1, true, 'Dygnsnederbörd från SMHI, automatisk import via fil', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (5, NULL, 'Temperatur', 'Temperatur', '°C', 1, NULL, NULL, NULL, 'Annat fel', 1, true, 'Temperatur per timme från SMHI, automatisk import via fil', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (5, NULL, 'Nivå - Mälaren', 'Vattenstånd', 'm', 2, NULL, NULL, NULL, 'Annat fel', 0, true, 'Vattenstånd för Mälaren från Stockholms Hamnar, automatisk import via fil', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (5, NULL, 'Nivå - Saltsjön', 'Vattenstånd', 'm', 2, NULL, NULL, NULL, 'Annat fel', 0, true, 'Vattenstånd för Saltsjön från Stockholms Hamnar, automatisk import via fil', false, '2018-03-19 10:00:00', '2018-03-19 10:00:00', NULL, true);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Akrylamid', NULL, 'µg/l', 4, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, 'Akrylamid är en reaktiv kemisk förening som i ren form är ett lukt- och färglöst pulver. Är klassificerad som en sannolik carcinogen (grupp 2A).', true, '2019-12-03 15:12:00.740801', '2019-12-03 15:12:00.740801', 2, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Ammoniumkväve', NULL, 'mg/l', 4, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, 'Ammoniumkväve (NH4-N) anger det kväve som förekommer som ammonium i vattnet. Många fiskarter och andra vattenlevande organismer är känsliga för höga halter av ammonium.', true, '2019-12-03 15:12:41.025961', '2019-12-03 15:12:41.025961', 2, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Turbiditet', NULL, 'ppm', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, NULL, true, '2019-12-09 15:37:40.339459', '2019-12-09 15:37:40.339459', 40, false);
INSERT INTO definition_matningstyp (matobjekt_typ, berakningstyp, namn, storhet, enhet, decimaler, beraknad_storhet, beraknad_enhet, beraknad_decimaler, felkoder, graftyp, automatisk_inrapportering, beskrivning, andringsbar, skapad_datum, andrad_datum, andrad_av_id, automatisk_granskning) VALUES (4, NULL, 'Bensin', NULL, 'x', 2, NULL, NULL, NULL, 'Hinder, Annat fel', 0, false, 'd', true, '2019-12-11 13:44:55.953711', '2019-12-11 13:44:55.953711', 40, false);