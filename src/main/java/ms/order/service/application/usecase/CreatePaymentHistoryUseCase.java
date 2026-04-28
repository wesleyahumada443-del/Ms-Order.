package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.model.Payment;
import ms.order.service.domain.model.PaymentEvent;
import ms.order.service.domain.model.PaymentHistory;
import ms.order.service.domain.port.in.CreatePaymentHistoryInPort;
import ms.order.service.domain.port.out.PaymentHistoryRepositoryOutPort;
import org.springframework.stereotype.Component;


/**
 * This class is responsible for the implement de business logic.
 *
 * @author Williams Ahumada
 * @author Wesley Ahumada
 * @author Jhonnatan Mendez
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class CreatePaymentHistoryUseCase implements CreatePaymentHistoryInPort {

    private final PaymentHistoryRepositoryOutPort paymentHistoryRepositoryOutPort;

    @Override
    public void createPaymentHistory(PaymentEvent event) {
        var paymentHistory = PaymentHistory.builder()
                .orderId(event.getOrderId())
                .status(event.getStatus())
                .paymentId(event.getId())
                .amount(event.getAmount())
                .description(event.getDescription())
                .build();
        paymentHistoryRepositoryOutPort.create(paymentHistory);
    }

    @Override
    public void createPaymentHistory(Payment event) {
        var paymentHistory = PaymentHistory.builder()
                .status(event.getStatus())
                .amount(event.getAmount())
                .paymentId(event.getId())
                .description(event.getDescription())
                .build();
        paymentHistoryRepositoryOutPort.create(paymentHistory);
    }
}
