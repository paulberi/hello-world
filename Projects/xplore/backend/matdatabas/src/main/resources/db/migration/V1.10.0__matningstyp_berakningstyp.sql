ALTER TABLE definition_matningstyp ADD COLUMN berakningstyp varchar;


UPDATE definition_matningstyp SET berakningstyp = 'NIVA_VATTEN_LUFTTRYCK' WHERE namn = 'Nivå (vatten- o lufttryck)';
UPDATE definition_matningstyp SET berakningstyp = 'NIVA_NEDMATNING' WHERE namn = 'Nivå (nedmätning)';
UPDATE definition_matningstyp SET berakningstyp = 'NIVA_PORTRYCK' WHERE namn = 'Nivå (portryck)';
UPDATE definition_matningstyp SET berakningstyp = 'SATTNING' WHERE namn = 'Sättning';
UPDATE definition_matningstyp SET berakningstyp = 'LUFTTRYCK_PER_TIMME' WHERE namn = 'Lufttryck per timme';
UPDATE definition_matningstyp SET berakningstyp = 'INFILTRATION_MOMENTANT_FLODE' WHERE namn = 'Infiltration (momentant flöde)';
UPDATE definition_matningstyp SET berakningstyp = 'INFILTRATION_MEDEL_FLODE' WHERE namn = 'Infiltration (medelflöde)';
UPDATE definition_matningstyp SET berakningstyp = 'TUNNELVATTEN_MOMENTANT_FLODE' WHERE namn = 'Tunnelvatten (momentant flöde)';
UPDATE definition_matningstyp SET berakningstyp = 'TUNNELVATTEN_MEDEL_FLODE' WHERE namn = 'Tunnelvatten (medelflöde)';
