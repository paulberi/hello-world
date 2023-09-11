UPDATE scheduler_job_info SET cron_expression = '0 0 8 ? * *' WHERE job_class='importVattenstandJob';
