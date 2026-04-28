package ms.order.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class PaymentHistory extends BaseModel implements Serializable {

    private UUID orderId;
    private String description;
    private UUID paymentId;
    private BigDecimal amount;
    private String status;
}
