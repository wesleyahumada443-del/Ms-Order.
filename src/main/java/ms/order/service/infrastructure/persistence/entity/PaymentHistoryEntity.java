package ms.order.service.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments_history")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PaymentHistoryEntity extends BaseAnonymousEntity{

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "description")
    private String description;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    private String status;
}
