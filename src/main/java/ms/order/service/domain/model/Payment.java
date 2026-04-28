package ms.order.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class Payment extends BaseModel {

    private UUID orderId;
    private BigDecimal amount;
    private String status;
    private String description;
}
