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
public class OrderOut extends BaseModel implements Serializable {
    private BigDecimal amount;
    private UUID paymentMethodCustomerId;
}
