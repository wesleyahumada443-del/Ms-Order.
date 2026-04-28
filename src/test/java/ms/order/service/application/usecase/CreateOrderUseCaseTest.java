package ms.order.service.application.usecase;

import ms.order.service.domain.mock.PaymentMockFactory;
import ms.order.service.domain.model.Payment;
import ms.order.service.domain.port.in.CreatePaymentHistoryInPort;
import ms.order.service.domain.port.out.PaymentOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class CreateOrderUseCaseTest {

    @InjectMocks
    private CreatePaymentUseCase createPaymentUseCase;

    @Mock
    private PaymentOutPort paymentOutPort;
    @Mock
    private CreatePaymentHistoryInPort createPaymentHistory;

    @Test
    void shouldCreatePaymentSuccessfully() {
        var payment = new PaymentMockFactory().build();
        when(paymentOutPort.create(any())).thenReturn(payment);
        doNothing().when(createPaymentHistory).createPaymentHistory(any(Payment.class));
        createPaymentUseCase.createPayment(payment);
        var paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentOutPort, atLeastOnce()).create(any());//TODO no se validó lo que se guardó
        verify(createPaymentHistory, atLeastOnce()).createPaymentHistory(paymentCaptor.capture());

        var paymentHistory = paymentCaptor.getValue();
        assertEquals(payment.getOrderId(), paymentHistory.getOrderId());
        assertEquals(payment.getAmount(), paymentHistory.getAmount());
        assertEquals(payment.getStatus(), paymentHistory.getStatus());
        assertEquals(payment.getDescription(), paymentHistory.getDescription());
    }

}