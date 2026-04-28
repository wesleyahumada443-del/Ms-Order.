package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.OrderOut;
import ms.order.service.domain.model.Product;
import ms.order.service.domain.port.in.CreateOrderInPort;
import ms.order.service.domain.port.in.ValidateProductInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.stream.Collectors;

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
public class CreateOrderUseCase implements CreateOrderInPort {

    private final OrderRepositoryOutPort orderRepositoryOutPort;
    private final ValidateProductInPort validateProductInPort;
    private final SendOrderUseCase sendOrderUseCase;

    public Order createOrder(Order order) {
        order.setStatus(OrderStatusEnum.PENDING);
        var productIds = order.getItems()
                .stream()
                .map(item -> item.getProduct()
                        .getId())
                .collect(Collectors.toSet());
        var products = validateProductInPort.validateByIds(productIds);

        var totalAmount = products.stream().map(Product::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setAmount(totalAmount);

        var orderCreated = orderRepositoryOutPort.create(order);

        sendOrderUseCase.send(OrderOut.builder()
                .id(orderCreated.getId())
                .amount(totalAmount)
                .paymentMethodCustomerId(orderCreated.getPaymentMethodCustomerId())
                .build());
        return orderCreated;
    }
}
