package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.enums.PaymentStatusEnum;
import ms.order.service.domain.exceptions.OrderNotFoundException;
import ms.order.service.domain.exceptions.OrderStatusInvalidException;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.Payment;
import ms.order.service.domain.model.PaymentEvent;
import ms.order.service.domain.port.in.ChangeOrderInPort;
import ms.order.service.domain.port.in.CreatePaymentHistoryInPort;
import ms.order.service.domain.port.in.CreatePaymentInPort;
import ms.order.service.domain.port.in.GetOrderInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;

import static java.util.Objects.isNull;


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
public class ChangeOrderUseCase implements ChangeOrderInPort{

    private final OrderRepositoryOutPort orderRepositoryOutPort;
    private final CreatePaymentHistoryInPort createPaymentHistoryInPort;
    private final GetOrderInPort getOrderInPort;
    private final CreatePaymentInPort createPaymentInPort;

    @Override
    public Order updateById(Order order){
        return orderRepositoryOutPort.updateById(order);
    }

    @Override
    public void changePaymentOrder(PaymentEvent event){

        if (isNull(event.getOrderId()) || isNull(event.getId()) || (isNull(event.getStatus()))){
            throw new InvalidParameterException("Discarded - The orderId/paymentId is null!");
        }

        var order = getOrderInPort.getById(event.getOrderId());
        if (isNull(order)) {
            throw new OrderNotFoundException("Order not found for payment by id: " + event.getOrderId());
        }

        if (order.getStatus() == OrderStatusEnum.COMPLETED){
            throw new  OrderStatusInvalidException("The new order was processed");
        }

        event.setAmount(order.getAmount());

        // event.getStatus().equalsIgnoreCase(PaymentStatusEnum.PAID.name())

        if (PaymentStatusEnum.valueOf(event.getStatus().toUpperCase()) == PaymentStatusEnum.PAID) {
            Payment payment = createPaymentInPort.createPayment(buildPayment(order, event));
            order.setPayment(payment);
            order.setStatus(OrderStatusEnum.COMPLETED);


        } else {
            order.setStatus(OrderStatusEnum.PAYMENT_FAILED);
            createPaymentHistoryInPort.createPaymentHistory(event);
        }
        orderRepositoryOutPort.create(order);
        log.info("The order was updated" + order.getId() + order.getStatus());
    }

    private Payment buildPayment(Order order, PaymentEvent paymentEvent) {

        return Payment.builder()
                .id(paymentEvent.getId())
                .orderId(order.getId())
                .amount(order.getAmount())
                .status(paymentEvent.getStatus())
                .description(paymentEvent.getDescription())
                .build();
    }

}
