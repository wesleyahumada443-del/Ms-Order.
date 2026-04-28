package ms.order.service.infrastructure.dto.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class OrderStatusResponse{
    @JsonProperty("status")
    private String status;
}
