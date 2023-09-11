DELETE
FROM acl_object_identity
    USING acl_class
WHERE acl_object_identity.object_id_class = acl_class.id
  AND (acl_class.class = 'se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity'
    OR acl_class.class = 'se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity');