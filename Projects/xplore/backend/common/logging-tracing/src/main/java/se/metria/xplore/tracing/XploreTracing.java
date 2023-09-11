package se.metria.xplore.tracing;

import co.elastic.apm.attach.ElasticApmAttacher;
import io.opentracing.noop.NoopTracerFactory;
import io.opentracing.util.GlobalTracer;

public class XploreTracing {
    public static void StartTracing() {
        if (System.getenv("ELASTIC_APM_SERVER_URL") != null) {
            ElasticApmAttacher.attach();
        } else {
            GlobalTracer.registerIfAbsent(NoopTracerFactory.create());
        }
    }
}
