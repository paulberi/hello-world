CREATE TABLE scheduler_job_info (
      id integer NOT NULL,
      cron_expression character varying(30) DEFAULT NULL,
      cron_job boolean DEFAULT true NOT NULL,
      job_class character varying(60) NOT NULL,
      job_group character varying(60) DEFAULT NULL,
      job_name character varying(60) DEFAULT NULL,
      repeat_time bigint DEFAULT NULL,
      latest_ok timestamp without time zone,
      latest_run timestamp without time zone,
      latest_status smallint DEFAULT 0
);

CREATE SEQUENCE scheduler_job_info_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE scheduler_job_info_seq OWNED BY scheduler_job_info.id;


INSERT INTO scheduler_job_info (id, cron_expression, job_class, job_group, job_name, cron_job, repeat_time, latest_ok, latest_run, latest_status)
VALUES (1, '0 0 1 ? * *', 'statusService', NULL, 'Skicka statusmail', true, NULL, NULL, NULL, 0),
       (2, '0 0 1 ? * *', 'importVattenstandService', NULL, 'Importera Vattenstånd', true, NULL, NULL, NULL, 0);


