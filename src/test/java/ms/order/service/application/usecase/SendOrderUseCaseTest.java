package ms.order.service.application.usecase;

import ms.order.service.domain.mock.OrderOutMockFactory;
import ms.order.service.domain.port.out.PaymentOrderOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class SendOrderUseCaseTest {

    @InjectMocks
    private SendOrderUseCase sendOrderUseCase;

    @Mock
    private  PaymentOrderOutPort orderEventsOutPort;
    @Test
    void shouldSentOrderIsSuccessfully() {
        var orderOut = new OrderOutMockFactory().build();

        doNothing().when(orderEventsOutPort).sendMessage(any());

        sendOrderUseCase.send(orderOut);

        verify(orderEventsOutPort, atLeastOnce()).sendMessage(eq(orderOut));

    }

}

