package ms.order.service.application.usecase;

import ms.order.service.domain.mock.ProductMockFactory;
import ms.order.service.domain.port.out.ProductRepositoryOutPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class ValidateProductUseCaseTest {

    @InjectMocks
    private ValidateProductUseCase validateProductUseCase;

    @Mock
    private  ProductRepositoryOutPort productRepositoryOutPort;

    @Test
    void shouldValidateByIdsIsSuccessfully() {
        var product = new ProductMockFactory().build();
        when(productRepositoryOutPort.findAllByIds(any())).thenReturn(List.of(product));

        var result = validateProductUseCase.validateByIds(Set.of(product.getId()));
        var resultActual = result.get(0);

        verify(productRepositoryOutPort, atLeastOnce()).findAllByIds(eq(Set.of(product.getId())));

        assertFalse(result.isEmpty());
        assertEquals(product.getProductName(), resultActual.getProductName());
        assertEquals(product.getAmount(), resultActual.getAmount());
        assertEquals(product.getDescription(), resultActual.getDescription());
        assertEquals(product.getStatus(), resultActual.getStatus());
        assertEquals(product.getId(), resultActual.getId());
    }

    @Test
    void shouldValidateByIdsIsInvalidThenReturnException() {
        var ids = Set.of(UUID.randomUUID(), UUID.randomUUID());

        when(productRepositoryOutPort.findAllByIds(any())).thenReturn(List.of(new ProductMockFactory().build()));

        var result = assertThrows(IllegalStateException.class, () -> validateProductUseCase.validateByIds(ids));

        verify(productRepositoryOutPort, atLeastOnce()).findAllByIds(eq(ids));
        assertEquals("Product ids not found: " + ids, result.getMessage());
    }

    @Test
    void shouldValidateByIdsWhenProductsNotFountThenReturnException() {
        var ids = Set.of(UUID.randomUUID(), UUID.randomUUID());

        when(productRepositoryOutPort.findAllByIds(any())).thenReturn(List.of());

        var result = assertThrows(IllegalStateException.class, () -> validateProductUseCase.validateByIds(ids));

        verify(productRepositoryOutPort, atLeastOnce()).findAllByIds(eq(ids));
        assertEquals("Product ids not found: " + ids, result.getMessage());
    }

}

