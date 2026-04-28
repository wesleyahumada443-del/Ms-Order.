package ms.order.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ms.order.service.domain.enums.OrderStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class Order extends BaseModel implements Serializable {

    private OrderStatusEnum status;
    private BigDecimal amount;
    private Payment payment;
    private List<OrderItem> items;
    private UUID paymentMethodCustomerId;
}
