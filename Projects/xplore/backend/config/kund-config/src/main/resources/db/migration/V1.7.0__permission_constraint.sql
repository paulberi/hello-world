ALTER TABLE permissions ADD CONSTRAINT uq_user_produkt_kund UNIQUE(user_id, produkt, kund_id, roll);