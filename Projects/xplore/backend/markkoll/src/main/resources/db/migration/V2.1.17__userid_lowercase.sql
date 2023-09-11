UPDATE acl_sid
   SET sid = LOWER(sid)
 WHERE principal = true;

ALTER TABLE config.user_role
DROP CONSTRAINT uc_user_role;

ALTER TABLE config.user_role
DROP CONSTRAINT fk_user;

UPDATE config.markkoll_user
SET id = LOWER(id);

UPDATE config.user_role
SET user_id = LOWER(user_id);

ALTER TABLE config.user_role
ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES config.markkoll_user (id);

ALTER TABLE config.user_role
ADD CONSTRAINT uc_user_role UNIQUE (user_id, role_type, object_id);