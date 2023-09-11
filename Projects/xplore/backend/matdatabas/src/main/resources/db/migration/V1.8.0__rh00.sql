ALTER TABLE matdatabas.kallsystem
ADD tips text;

INSERT INTO matdatabas.kallsystem (
    namn, beskrivning, default_godkand, manuell_import, tips
)
VALUES ('Annat (RH00, granskade mätningar)', 'Mätningar i höjdsystemet RH00 för Stockholm', true, true, 'Adderar genomsnittlig skillnad mellan RH2000 och RH00 i Stockholm (0.525) till mätningar. Obs! Detta tillämpas på alla mätningar i importen, oavsett mätningstyp.');



INSERT INTO matdatabas.kallsystem (
    namn, beskrivning, default_godkand, manuell_import, tips
)
VALUES ('Annat (RH2000, granskade mätningar)', 'Annat system, importen innehåller redan granskade mätningar', true, true, NULL);

UPDATE matdatabas.matning
SET kallsystem='Annat (RH2000, granskade mätningar)'
WHERE kallsystem='Annat (granskade mätningar)';

DELETE FROM matdatabas.kallsystem
WHERE namn='Annat (granskade mätningar)';



INSERT INTO matdatabas.kallsystem (
    namn, beskrivning, default_godkand, manuell_import, tips
)
VALUES ('Annat (RH2000, ej granskade mätningar)', 'Annat system, importen innehåller ogranskade mätningar', false, true, NULL);

UPDATE matdatabas.matning
SET kallsystem='Annat (RH2000, ej granskade mätningar)'
WHERE kallsystem='Annat (ej granskade mätningar)';

DELETE FROM matdatabas.kallsystem
WHERE namn='Annat (ej granskade mätningar)';