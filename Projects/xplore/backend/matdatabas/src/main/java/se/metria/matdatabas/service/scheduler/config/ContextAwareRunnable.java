package se.metria.matdatabas.service.scheduler.config;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Decorate a Runnable task with attributes for request scope and security context
 */
public class ContextAwareRunnable implements Runnable
{
	private Runnable task;
	private RequestAttributes requestAttributes;
	private SecurityContext securityContext;
	private SecurityContext originalSecurityContext;

	public ContextAwareRunnable(Runnable task, RequestAttributes requestAttributes, SecurityContext securityContext) {
		this.task = task;
		this.requestAttributes = requestAttributes;
		this.securityContext = securityContext;
	}

	@Override
	public void run()
	{
		this.originalSecurityContext = SecurityContextHolder.getContext();
		try {
			SecurityContextHolder.setContext(this.securityContext);
			RequestContextHolder.setRequestAttributes(requestAttributes);
			task.run();
		} finally {
			SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
			if (emptyContext.equals(this.originalSecurityContext)) {
				SecurityContextHolder.clearContext();
			} else {
				SecurityContextHolder.setContext(this.originalSecurityContext);
			}
			this.originalSecurityContext = null;
			RequestContextHolder.resetRequestAttributes();
		}
	}
}
