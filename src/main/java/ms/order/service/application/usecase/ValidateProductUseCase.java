package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.model.Product;
import ms.order.service.domain.port.in.ValidateProductInPort;
import ms.order.service.domain.port.out.ProductRepositoryOutPort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
public class ValidateProductUseCase implements ValidateProductInPort {

    private final ProductRepositoryOutPort productRepositoryOutPort;

    @Override
    public List<Product> validateByIds(Set<UUID> ids) {//TODO Esto valida o busca? BUSCA
        var products = productRepositoryOutPort.findAllByIds(ids);
        Set<UUID> invalids = Collections.emptySet();

        if (!products.isEmpty()){
            var idsFound = products.stream().map(Product::getId).collect(Collectors.toSet());
            var missingIds = ids.stream().filter(id -> !idsFound.contains(id)).collect(Collectors.toSet());

            if (!missingIds.isEmpty()){
               invalids = missingIds;
            }

        } else {
            invalids = ids;
        }

        if (!invalids.isEmpty())
            throw new IllegalStateException("Product ids not found: " + ids);

        return products;
    }
}
