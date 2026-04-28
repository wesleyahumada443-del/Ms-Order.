package ms.order.service.application.usecase;

import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.mock.OrderItemMockFactory;
import ms.order.service.domain.mock.ProductMockFactory;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.OrderOut;
import ms.order.service.domain.port.in.ValidateProductInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class CreatePaymentUseCaseTest {

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private OrderRepositoryOutPort repositoryCrudOutPort;

    @Mock
    private ValidateProductInPort validateProductInPort;

    @Mock
    private SendOrderUseCase sendOrderUseCase;

    @Test
    void shouldCreateOrderSuccessfully() {
        var product = new ProductMockFactory().getProduct().build();
        var orderItem = new OrderItemMockFactory().ofProduct(product).build();
        var order = Order.builder()
                .items(List.of(orderItem))
                .paymentMethodCustomerId(UUID.randomUUID())
                .build();

        when(validateProductInPort.validateByIds(Set.of(product.getId())))
                .thenReturn(List.of(product));

        when(repositoryCrudOutPort.create(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order o = invocation.getArgument(0);
                    o.setId(UUID.randomUUID());
                    return o;
                });

        var result = createOrderUseCase.createOrder(order);

        assertNotNull(result);
        assertEquals(product.getAmount(), result.getAmount());
        assertEquals(OrderStatusEnum.PENDING, result.getStatus());
        verify(sendOrderUseCase).send(any(OrderOut.class));
    }

    // Escenario 2: Productos no encontrados
    @Test
    void shouldThrowExceptionWhenProductsNotFound() {
        var fakeId = UUID.randomUUID();
        var product = new ProductMockFactory().getProduct().ofId(fakeId).build();
        var orderItem = new OrderItemMockFactory().ofProduct(product).build();
        var order = Order.builder()
                .items(List.of(orderItem))
                .build();

        when(validateProductInPort.validateByIds(Set.of(fakeId)))
                .thenThrow(new IllegalStateException("Product ids not found: " + fakeId));

        var exception = assertThrows(IllegalStateException.class, () ->
                createOrderUseCase.createOrder(order));

        Assertions.assertTrue(exception.getMessage().contains("Product ids not found"));
        verify(repositoryCrudOutPort, never()).create(any());
        verify(sendOrderUseCase, never()).send(any());
    }

    @Test
    void shouldCalculateTotalAmountFromMultipleProducts() {
        var product1 = new ProductMockFactory().getProduct().ofAmount(new BigDecimal("10.50")).build();
        var product2 = new ProductMockFactory().getProduct().ofAmount(new BigDecimal("20.00")).ofId(UUID.randomUUID()).build();

        var item1 = new OrderItemMockFactory().ofProduct(product1).build();
        var item2 = new OrderItemMockFactory().ofProduct(product2).build();

        var order = Order.builder()
                .items(List.of(item1, item2))
                .paymentMethodCustomerId(UUID.randomUUID())
                .build();

        when(validateProductInPort.validateByIds(Set.of(product1.getId(), product2.getId())))
                .thenReturn(List.of(product1, product2));

        when(repositoryCrudOutPort.create(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order o = invocation.getArgument(0);
                    o.setId(UUID.randomUUID());
                    return o;
                });

        var result = createOrderUseCase.createOrder(order);

        assertEquals(new BigDecimal("30.50"), result.getAmount());
    }

    @Test
    void shouldSendCorrectOrderOut() {
        var product = new ProductMockFactory().getProduct().ofAmount(new BigDecimal("99.99")).build();
        var item = new OrderItemMockFactory().ofProduct(product).build();
        var paymentId = UUID.randomUUID();

        var order = Order.builder()
                .items(List.of(item))
                .paymentMethodCustomerId(paymentId)
                .build();

        UUID orderId = UUID.randomUUID();
        when(validateProductInPort.validateByIds(anySet())).thenReturn(List.of(product));
        when(repositoryCrudOutPort.create(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(orderId);
            return o;
        });

        var captor = ArgumentCaptor.forClass(OrderOut.class);

        createOrderUseCase.createOrder(order);

        verify(sendOrderUseCase).send(captor.capture());

        var sent = captor.getValue();
        assertEquals(orderId, sent.getId());
        assertEquals(new BigDecimal("99.99"), sent.getAmount());
        assertEquals(paymentId, sent.getPaymentMethodCustomerId());
    }

    @Test
    void shouldThrowWhenRepositoryFails() {
        var product = new ProductMockFactory().getProduct().build();
        var item = new OrderItemMockFactory().ofProduct(product).build();
        var order = Order.builder().items(List.of(item)).build();

        when(validateProductInPort.validateByIds(anySet())).thenReturn(List.of(product));
        when(repositoryCrudOutPort.create(any(Order.class)))
                .thenThrow(new RuntimeException("Error creating order"));

        var exception = assertThrows(RuntimeException.class, () ->
                createOrderUseCase.createOrder(order));

        assertEquals("Error creating order", exception.getMessage());
        verify(sendOrderUseCase, never()).send(any());
    }
}

