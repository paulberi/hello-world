update matdatabas.definition_matningstyp set decimaler = 4 where namn = 'Sättning';

update matdatabas.matningstyp set decimaler = dm.decimaler
from matdatabas.definition_matningstyp dm
where matningstyp.definition_matningstyp_id = dm.id and dm.namn = 'Sättning';
