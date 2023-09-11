UPDATE matning
    SET status = 1
    WHERE status = 2 AND NOT (felkod = 'OK');
