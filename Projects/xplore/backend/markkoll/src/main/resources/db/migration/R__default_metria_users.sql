CREATE TEMPORARY TABLE metria_users (
    id TEXT PRIMARY KEY,
    fornamn TEXT,
    efternamn TEXT
);

INSERT INTO metria_users
    VALUES
        ('christoffer.karlsson@metria.se', 'Christoffer', 'Karlsson'),
        ('emil.edberg@metria.se', 'Emil', 'Edberg'),
        ('john.eriksson@metria.se', 'John', 'Eriksson'),
        ('jenny.odeblom@metria.se', 'Jenny', 'Odeblom'),
        ('jonny.siikavaara@metria.se', 'Jonny', 'Siikavara'),
        ('joel.nandorf@metria.se', 'Joel', 'Nandorf'),
        ('andre.oberg@metria.se', 'Andre', 'Oberg'),
        ('johan.morin@metria.se', 'Johan', 'Morin');

INSERT INTO config.kund (id)
    VALUES ('556799-2242')
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.utskicksnummer (kund_id)
    VALUES ('556799-2242')
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.niskalla (kund_id, dp_com, dp_power, trimble)
    VALUES ('556799-2242', true, true, true)
ON CONFLICT DO NOTHING;

INSERT INTO config.markkoll_user (id, fornamn, efternamn, email, kund_id)
SELECT id, fornamn, efternamn, id, '556799-2242'
  FROM metria_users
ON CONFLICT DO NOTHING;

INSERT INTO config.user_role (user_id, role_type, object_id)
SELECT id, 'KUNDADMIN', '556799-2242'
    FROM metria_users
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_sid (principal, sid)
SELECT true, id
FROM metria_users
ON CONFLICT DO NOTHING ;

INSERT INTO markkoll.acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting)
SELECT 2, '556799-2242', 1, true
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
SELECT 21, mu.id, 1, sid.id, true
FROM metria_users mu
JOIN acl_sid sid
 ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT 1,
       6 * ROW_NUMBER() OVER (ORDER BY mu.id) - 6,
       sid.id,
       1,
       true,
       false,
       false
FROM metria_users mu
JOIN acl_sid sid
ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT 1,
       6 * ROW_NUMBER() OVER (ORDER BY mu.id) - 5,
       sid.id,
       2,
       true,
       false,
       false
FROM metria_users mu
JOIN acl_sid sid
ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT 1,
       6 * ROW_NUMBER() OVER (ORDER BY mu.id) - 4,
       sid.id, 4,
       true,
       false,
       false
FROM metria_users mu
JOIN acl_sid sid
ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT 1,
       6 * ROW_NUMBER() OVER (ORDER BY mu.id) - 3,
       sid.id,
       8,
       true,
       false,
       false
FROM metria_users mu
JOIN acl_sid sid
ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT 1,
       6 * ROW_NUMBER() OVER (ORDER BY mu.id) - 2,
       sid.id,
       16,
       true,
       false,
       false
FROM metria_users mu
JOIN acl_sid sid
ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

INSERT INTO markkoll.acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
SELECT 1,
       6 * ROW_NUMBER() OVER (ORDER BY mu.id) - 1,
       sid.id,
       32,
       true,
       false,
       false
FROM metria_users mu
JOIN acl_sid sid
ON sid.sid = mu.id
ON CONFLICT DO NOTHING;

DROP TABLE metria_users;