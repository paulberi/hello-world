ALTER TABLE person
ADD co_adress TEXT DEFAULT '';

WITH max_datum AS (
      SELECT max(importerad_datum) max_datum, personnummer
        FROM finfo.agare
    GROUP BY personnummer
),
     co_senast AS (
        SELECT ag.personnummer, ag.co, ag.importerad_datum
          FROM finfo.agare ag
    INNER JOIN max_datum
            ON ag.importerad_datum = max_datum.max_datum
           AND ag.personnummer = max_datum.personnummer
)
UPDATE person
   SET co_adress = co_senast.co
  FROM co_senast
 WHERE person.personnummer = co_senast.personnummer
   AND person.personnummer <> '';