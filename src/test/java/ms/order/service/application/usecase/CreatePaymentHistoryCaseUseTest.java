package ms.order.service.application.usecase;

import ms.order.service.domain.mock.PaymentEventMockFactory;
import ms.order.service.domain.mock.PaymentMockFactory;
import ms.order.service.domain.model.PaymentHistory;
import ms.order.service.domain.port.out.PaymentHistoryRepositoryOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class CreatePaymentHistoryCaseUseTest {

    @InjectMocks
    private CreatePaymentHistoryUseCase createPaymentHistoryUseCase;

    @Mock
    private PaymentHistoryRepositoryOutPort paymentHistoryRepositoryOutPort;
    @Test
    void shouldCreatePaymentHistoryByPaymentSuccessfully() {
        var payment = new PaymentMockFactory().build();

        when(paymentHistoryRepositoryOutPort.create(any())).thenReturn(any(PaymentHistory.class));

        createPaymentHistoryUseCase.createPaymentHistory(payment);

        verify(paymentHistoryRepositoryOutPort, atLeastOnce()).create(any());
    }

    @Test
    void shouldCreatePaymentHistoryByPaymentEventSuccessfully() {
        var paymentEvent = new PaymentEventMockFactory().build();

        when(paymentHistoryRepositoryOutPort.create(any())).thenReturn(any(PaymentHistory.class));

        createPaymentHistoryUseCase.createPaymentHistory(paymentEvent);

        verify(paymentHistoryRepositoryOutPort, atLeastOnce()).create(any());

    }

}

