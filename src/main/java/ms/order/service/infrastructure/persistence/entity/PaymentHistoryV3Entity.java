package ms.order.service.infrastructure.persistence.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_history_v3")
public class PaymentHistoryV3Entity {

    @Id
    private UUID id;

    private UUID paymentId;

    private UUID orderId;

    private BigDecimal amount;

    private String status;

    private String description;
}
