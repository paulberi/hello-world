SET search_path TO matdatabas;
UPDATE scheduler_job_info
SET cron_job=false, repeat_time=1000000, latest_ok='2020-03-23 00:30:00.926667', latest_run='2020-03-23 00:30:00.926667'
WHERE job_class='importVattenstandService';
