package ms.order.service.infrastructure.dto.message.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ms.order.service.infrastructure.dto.PayloadMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class PaymentOrderMessage implements PayloadMessage {
    @JsonProperty("source_id")
    private UUID orderId;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("payment_method")
    private UUID paymentMethodCustomerId;
}
