package ms.order.service.infrastructure.dto.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import ms.order.service.domain.enums.OrderStatusEnum;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class
OrderRequest {

    @JsonProperty("status")
    private OrderStatusEnum status;

    @JsonProperty("payment_method_customer_id")
    private UUID paymentMethodCustomerId;

    @NotEmpty
    @Valid
    @JsonProperty("items")
    private List<OrderItemRequest> items;
}
