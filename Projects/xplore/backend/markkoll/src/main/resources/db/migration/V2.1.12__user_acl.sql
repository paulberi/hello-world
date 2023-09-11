INSERT INTO acl_class (class, class_id_type)
    VALUES
        ('se.metria.markkoll.entity.admin.UserEntity', 'java.lang.String');

WITH kund_object_id AS
    (SELECT acl_object_identity.id as kund_object_id, config.markkoll_user.id as user_id
     FROM acl_object_identity
    JOIN config.markkoll_user ON acl_object_identity.object_id_identity = config.markkoll_user.kund_id),
     user_class_id AS
(SELECT id FROM acl_class WHERE class = 'se.metria.markkoll.entity.admin.UserEntity')
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
     SELECT user_class_id.id, kund_object_id.user_id, kund_object_id.kund_object_id, 1, true
       FROM kund_object_id
       JOIN user_class_id ON true = true;