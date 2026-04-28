package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.port.in.CreateOrderInPort;
import ms.order.service.domain.port.in.GetOrderInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
public class GetOrderUseCase implements GetOrderInPort{

    private final OrderRepositoryOutPort repositoryCrudOutPort;

    public List<Order> getPaginated(Pageable pageable){
        return repositoryCrudOutPort.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Order getById(UUID id){
        return repositoryCrudOutPort.findById(id);
    }
}
