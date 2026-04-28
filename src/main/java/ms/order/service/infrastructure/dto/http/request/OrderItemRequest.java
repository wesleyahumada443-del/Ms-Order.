package ms.order.service.infrastructure.dto.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class OrderItemRequest {

    @JsonProperty("product_id")
    private UUID productId;

    @JsonProperty("quantity")
    private Integer quantity;
}
