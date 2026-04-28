package ms.order.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class Product extends BaseModel implements Serializable {

    private String productName;
    private String description;
    private BigDecimal amount;
    private String status;
}
