package ms.order.service.application.usecase;

import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.enums.PaymentStatusEnum;
import ms.order.service.domain.exceptions.OrderNotFoundException;
import ms.order.service.domain.exceptions.OrderStatusInvalidException;
import ms.order.service.domain.mock.OrderMockFactory;
import ms.order.service.domain.mock.PaymentEventMockFactory;
import ms.order.service.domain.mock.PaymentMockFactory;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.Payment;
import ms.order.service.domain.model.PaymentEvent;
import ms.order.service.domain.port.in.CreatePaymentHistoryInPort;
import ms.order.service.domain.port.in.CreatePaymentInPort;
import ms.order.service.domain.port.in.GetOrderInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class ChangeOrderUseCaseTest {

    @InjectMocks
    private ChangeOrderUseCase changeOrderUseCase;
    @Mock
    private  OrderRepositoryOutPort orderRepositoryOutPort;
    @Mock
    private  CreatePaymentHistoryInPort createPaymentHistoryInPort;
    @Mock
    private  GetOrderInPort getOrderInPort;
    @Mock
    private  CreatePaymentInPort createPaymentInPort;

    @Test
    void shouldChangeOrderWhenPaymentStatusIsPaid() {
        var order = new OrderMockFactory().ofId(UUID.randomUUID()).ofAmount(BigDecimal.valueOf(1200)).build();
        var paymentEvent = new PaymentEventMockFactory().ofOrderId(order.getId()).ofId(UUID.randomUUID()).build();
        var payment = new PaymentMockFactory().ofOrderId(order.getId()).ofId(UUID.randomUUID()).ofAmount(order.getAmount()).build();


        when(getOrderInPort.getById(any())).thenReturn((order));
        when(createPaymentInPort.createPayment(any())).thenReturn(payment);
        when(orderRepositoryOutPort.create(any())).thenReturn(order);

        changeOrderUseCase.changePaymentOrder(paymentEvent);

        var paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        var orderCaptor = ArgumentCaptor.forClass(Order.class);

        verify(getOrderInPort, atLeastOnce()).getById(any());
        verify(createPaymentInPort, atLeastOnce()).createPayment(paymentCaptor.capture());
        verify(orderRepositoryOutPort, atLeastOnce()).create(orderCaptor.capture());

        var paymentValue = paymentCaptor.getValue();
        var orderValue = orderCaptor.getValue();

        assertEquals(orderValue.getStatus(), OrderStatusEnum.COMPLETED);

        assertEquals(paymentValue.getStatus(), PaymentStatusEnum.PAID.name());
        assertEquals(paymentValue.getAmount(), orderValue.getAmount());
        assertEquals(paymentValue.getOrderId(), orderValue.getId());



        assertEquals(paymentValue.getStatus(), orderValue.getPayment().getStatus());
        assertEquals(paymentValue.getDescription(), orderValue.getPayment().getDescription());
        assertEquals(paymentValue.getAmount(), orderValue.getPayment().getAmount());
    }

    @Test
    void shouldChangeOrderWhenPaymentStatusIsFailed() {
        var order = new OrderMockFactory().ofId(UUID.randomUUID())
                .ofAmount(BigDecimal.valueOf(1200)).build();

        var paymentEvent = new PaymentEventMockFactory().ofOrderId(order.getId())
                .ofId(UUID.randomUUID())
                .ofStatus(PaymentStatusEnum.FAILED.name())
                .build();

        when(getOrderInPort.getById(any())).thenReturn((order));
        doNothing().when(createPaymentHistoryInPort).createPaymentHistory(any(PaymentEvent.class));
        when(orderRepositoryOutPort.create(any())).thenReturn(order);

        changeOrderUseCase.changePaymentOrder(paymentEvent);

        var paymentCaptor = ArgumentCaptor.forClass(PaymentEvent.class);
        var orderCaptor = ArgumentCaptor.forClass(Order.class);

        verify(createPaymentHistoryInPort, atLeastOnce()).createPaymentHistory(paymentCaptor.capture());
        verify(getOrderInPort, atLeastOnce()).getById(any());
        verify(orderRepositoryOutPort, atLeastOnce()).create(orderCaptor.capture());

        var paymentEventValue = paymentCaptor.getValue();
        var orderValue = orderCaptor.getValue();

        assertEquals(orderValue.getStatus(), OrderStatusEnum.PAYMENT_FAILED);

        assertEquals(paymentEventValue.getStatus(), PaymentStatusEnum.FAILED.name());
        assertEquals(paymentEventValue.getAmount(), orderValue.getAmount());

    }


    @Test
    void shouldChangeOrderWhenOrderIdIsNullThenThrowsException() {
        var paymentEvent = new PaymentEventMockFactory().ofOrderId(null)
                .ofId(UUID.randomUUID())
                .build();


        var exception = assertThrows(InvalidParameterException.class,
                () -> changeOrderUseCase.changePaymentOrder(paymentEvent));

        verify(getOrderInPort, never()).getById(any());
        verify(createPaymentInPort, never()).createPayment(any(Payment.class));
        verify(createPaymentHistoryInPort, never()).createPaymentHistory(any(PaymentEvent.class));
        verify(orderRepositoryOutPort, never()).create(any());

        assertNull(paymentEvent.getOrderId());
        assertEquals("Discarded - The orderId/paymentId is null!", exception.getMessage());

    }

    @Test
    void shouldChangeOrderWhenEventIdIsNullThenThrowsException() {
        var paymentEvent = new PaymentEventMockFactory().ofOrderId(UUID.randomUUID())
                .ofId(null)
                .build();


        var exception = assertThrows(InvalidParameterException.class,
                () -> changeOrderUseCase.changePaymentOrder(paymentEvent));

        verify(getOrderInPort, never()).getById(any());
        verify(createPaymentInPort, never()).createPayment(any(Payment.class));
        verify(createPaymentHistoryInPort, never()).createPaymentHistory(any(PaymentEvent.class));
        verify(orderRepositoryOutPort, never()).create(any());

        assertNull(paymentEvent.getId());
        assertEquals("Discarded - The orderId/paymentId is null!", exception.getMessage());

    }

    @Test
    void shouldChangeOrderWhenEventStatusIsNullThenThrowsException() {
        var paymentEvent = new PaymentEventMockFactory().ofOrderId(UUID.randomUUID())
                .ofId(UUID.randomUUID())
                .ofStatus(null)
                .build(); //todo


        var exception = assertThrows(InvalidParameterException.class,
                () -> changeOrderUseCase.changePaymentOrder(paymentEvent));

        verify(getOrderInPort, never()).getById(any());
        verify(createPaymentInPort, never()).createPayment(any(Payment.class));
        verify(createPaymentHistoryInPort, never()).createPaymentHistory(any(PaymentEvent.class));
        verify(orderRepositoryOutPort, never()).create(any());

        assertNull(paymentEvent.getStatus());
        assertEquals("Discarded - The orderId/paymentId is null!", exception.getMessage());

    }

    @Test
    void shouldChangeOrderWhenOrderIsConfirmedThenThrowsException() {
        var order = new OrderMockFactory()
                .ofId(UUID.randomUUID())
                .ofAmount(BigDecimal.valueOf(1200))
                .ofStatus(OrderStatusEnum.COMPLETED)
                .build();

        var paymentEvent = new PaymentEventMockFactory()
                .ofOrderId(UUID.randomUUID()) //todo
                .ofId(UUID.randomUUID())
                .build();

        when(getOrderInPort.getById(any())).thenReturn(order);

        var exception = assertThrows(OrderStatusInvalidException.class,
                () -> changeOrderUseCase.changePaymentOrder(paymentEvent));

        verify(getOrderInPort, atLeastOnce()).getById(any());
        verify(createPaymentInPort, never()).createPayment(any(Payment.class));
        verify(createPaymentHistoryInPort, never()).createPaymentHistory(any(PaymentEvent.class));
        verify(orderRepositoryOutPort, never()).create(any());

        assertEquals("The new order was processed", exception.getMessage());

    }

    @Test
    void shouldChangeOrderWhenOrderNotExistByIdThenThrowsException() {
        var paymentEvent = new PaymentEventMockFactory()
                .ofOrderId(UUID.randomUUID())
                .ofId(UUID.randomUUID())
                .build();

        when(getOrderInPort.getById(any())).thenReturn(null);

        var exception = assertThrows(OrderNotFoundException.class,
                () -> changeOrderUseCase.changePaymentOrder(paymentEvent));

        verify(getOrderInPort, atLeastOnce()).getById(any());
        verify(createPaymentInPort, never()).createPayment(any(Payment.class));
        verify(createPaymentHistoryInPort, never()).createPaymentHistory(any(PaymentEvent.class));
        verify(orderRepositoryOutPort, never()).create(any());

        assertEquals("Order not found for payment by id: " + paymentEvent.getOrderId(), exception.getMessage());

    }

    @Test
    void shouldUpdateOrderByIdIsSuccessfully() {
        var order = new OrderMockFactory().ofAmount(BigDecimal.valueOf(1500)).build();

        when(orderRepositoryOutPort.updateById(any())).thenReturn(order);

        var result = changeOrderUseCase.updateById(order);

        assertSame(order, result);
        verify(orderRepositoryOutPort, atLeastOnce()).updateById(any());
        verifyNoMoreInteractions(orderRepositoryOutPort);
    }

}