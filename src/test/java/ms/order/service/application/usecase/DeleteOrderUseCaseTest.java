package ms.order.service.application.usecase;

import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class DeleteOrderUseCaseTest {

    @InjectMocks
    private DeleteOrderUseCase deleteOrderUseCase;

    @Mock
    private OrderRepositoryOutPort orderRepositoryOutPort;

    @Test
    void shouldDeleteOrderIsSuccessfully() {
        var orderId = UUID.randomUUID();

        doNothing().when(orderRepositoryOutPort).deleteById(orderId);

        deleteOrderUseCase.deleteById(orderId);

        verify(orderRepositoryOutPort, atLeastOnce()).deleteById(eq(orderId));
    }

}

