package ms.order.service.infrastructure.dto.message.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ms.order.service.infrastructure.dto.PayloadMessage;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatusMessage implements PayloadMessage {

    @JsonProperty("source_id")
    private UUID sourceId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("payment_id")
    private String paymentId;
    @JsonProperty("description")
    private String description;
}

/*
TODO payment ->
PAYMENT_METHOD (ID, TYPE, DESCRIPTION)
    -> PAYMENT_METHOD_CUSTOMER_DATA (ID, CUSTOMER_ID, BRAND, NAME, NUMBER, SECURITY, DEFAULT)
        -> PAYMENT_METHOD_CUSTOMER (ID, PAYMENT_METHOD_ID, PAYMENT_METHOD_CUSTOMER_DATA_ID, CUSTOMER_ID)
PAYMENT (ID, ORDER_ID, AMOUNT, STATUS, DESCRIPTION)
*/
//TODO order ->
//PAYMENTS_HISTORY (ID, PAYMENT_ID, AMOUNT, ORDER_ID, STATUS, DESCRIPTION)