package ms.order.service.infrastructure.adapter.message.kafka.in;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.infrastructure.utils.TraceIdUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

import java.util.UUID;

import static java.util.Objects.isNull;

@Log4j2
@AllArgsConstructor
public abstract class AbstractProcessHeaderAdapter {

    public Header getMessageIdFromHeader(ConsumerRecord<String, String> message) {
        var traceId = message.headers().lastHeader(TraceIdUtils.TRACE_ID_KEY);
        var messageId = message.headers().lastHeader(TraceIdUtils.MESSAGE_ID_KEY);
        return isNull(traceId) ? messageId : traceId;
    }

    public UUID getTraceId(ConsumerRecord<String, String> message) {
        var traceIdHeader = getMessageIdFromHeader(message);
        return !isNull(traceIdHeader) ? UUID.fromString(new String(traceIdHeader.value())) : UUID.randomUUID();
    }
}
