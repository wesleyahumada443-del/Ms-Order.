package ms.order.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class PaymentEvent extends BaseModel implements Serializable{

    private  UUID id;
    private UUID orderId;
    private String status;
    private String description;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
}
