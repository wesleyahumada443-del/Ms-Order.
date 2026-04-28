package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.model.Payment;
import ms.order.service.domain.port.in.CreatePaymentHistoryInPort;
import ms.order.service.domain.port.in.CreatePaymentInPort;
import ms.order.service.domain.port.out.PaymentOutPort;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for the implement de business logic.
 *
 * @author Williams Ahumada
 * @author Wesley Ahumada
 * @author Jhonnatan Mendez
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class CreatePaymentUseCase implements CreatePaymentInPort{

    private final PaymentOutPort paymentOutPort;
    private final CreatePaymentHistoryInPort createPaymentHistory;

    @Override
    public Payment createPayment(Payment payment){
        createPaymentHistory.createPaymentHistory(payment);
        return paymentOutPort.create(payment);
    }
}
