package ms.order.service.application.usecase;

import ms.order.service.domain.mock.OrderMockFactory;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class GetOrderUseCaseTest {

    @InjectMocks
    private GetOrderUseCase getOrderUseCase;

    @Mock
    private OrderRepositoryOutPort orderRepositoryOutPort;

    @Test
    void shouldGetPaginatedIsSuccessfully() {
        var order = new OrderMockFactory().build();
        var pageable = PageRequest.of(1,2);

        when(orderRepositoryOutPort.findAll(any())).thenReturn(List.of(order));

        var result = getOrderUseCase.getPaginated(pageable);

        verify(orderRepositoryOutPort, atLeastOnce()).findAll(eq(pageable));
        verifyNoMoreInteractions(orderRepositoryOutPort);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertSame(order, result.get(0));
    }

    @Test
    void shouldGetByIdIsSuccessfully() {
        var order = new OrderMockFactory().build();

        when(orderRepositoryOutPort.findById(any())).thenReturn(order);

        var result = getOrderUseCase.getById(order.getId());

        verify(orderRepositoryOutPort, atLeastOnce()).findById(eq(order.getId()));
        verifyNoMoreInteractions(orderRepositoryOutPort);
        assertNotNull(result);
        assertSame(order, result);
    }

}

