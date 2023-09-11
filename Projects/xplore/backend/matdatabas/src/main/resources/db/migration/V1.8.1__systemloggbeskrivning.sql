ALTER TABLE systemlogg
ALTER COLUMN beskrivning TYPE varchar USING beskrivning::varchar;