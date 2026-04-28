package ms.order.service.infrastructure.dto.http.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ms.order.service.domain.model.Order;

@Getter
@Setter
@ToString
@SuperBuilder
public class OrderResponse extends Order {

}
