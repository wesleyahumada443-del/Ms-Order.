package ms.order.service.infrastructure.utils;

import org.slf4j.MDC;

import java.util.UUID;

public class TraceIdUtils {
    public static final String TRACE_ID_KEY = "traceId";
    public static final String MESSAGE_ID_KEY = "messageId";

    public static void setTraceId(final String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    public static void setTraceId(final UUID traceId) {
        MDC.put(TRACE_ID_KEY, traceId.toString());
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    public static UUID generate() {
        return UUID.randomUUID();
    }
}
