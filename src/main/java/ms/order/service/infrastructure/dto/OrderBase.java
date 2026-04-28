package ms.order.service.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ms.order.service.domain.enums.OrderStatusEnum;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
public class OrderBase implements Serializable {//TODO ver si eliminar o cambiar de paquete

    @JsonProperty("product")
    private UUID productId;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("status")
    private OrderStatusEnum status;
    @JsonProperty("payment_status")
    private String paymentStatus;
}
