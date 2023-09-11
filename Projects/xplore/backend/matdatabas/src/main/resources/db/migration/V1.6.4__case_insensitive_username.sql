ALTER TABLE anvandare DROP CONSTRAINT uq_anvandare_inloggnings_namn;

create unique index uq_anvandare_inloggnings_namn
	on anvandare (lower(inloggnings_namn));
