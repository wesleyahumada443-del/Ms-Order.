package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.port.in.DeleteOrderInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import org.springframework.stereotype.Component;

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
public class DeleteOrderUseCase implements DeleteOrderInPort{

    private final OrderRepositoryOutPort repositoryCrudOutPort;

    @Override
    public void deleteById(UUID id){
        repositoryCrudOutPort.deleteById(id);
    }
}
