package se.metria.matdatabas.service.scheduler;

import org.quartz.Job;

public interface JobService extends Job {
	void jobDone(String jobServiceTyp, Short status);
}
