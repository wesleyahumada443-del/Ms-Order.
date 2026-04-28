package ms.order.service.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ms.order.service.domain.enums.PaymentStatusEnum;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PaymentEntity extends BaseAnonymousEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatusEnum status;
}
