package ms.order.service.infrastructure.adapter.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import ms.order.service.application.usecase.SendOrderUseCase;
import ms.order.service.config.BaseKafkaIntegrationTest;
import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.mock.*;
import ms.order.service.domain.model.OrderOut;
import ms.order.service.domain.model.Product;
import ms.order.service.domain.port.in.CreateOrderInPort;
import ms.order.service.domain.port.in.ValidateProductInPort;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import ms.order.service.infrastructure.adapter.mapper.OrderMapper;
import ms.order.service.infrastructure.adapter.mapper.PaymentMapper;
import ms.order.service.infrastructure.persistence.entity.OrderEntity;
import ms.order.service.infrastructure.persistence.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest extends BaseKafkaIntegrationTest {

    private static final String TOPIC = "test.topic.payment.order";

    @Autowired
    private CreateOrderInPort createOrderInPort;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private ValidateProductInPort validateProductInPort;

    @Autowired
    private OrderRepositoryOutPort orderRepositoryOutPort;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        createTopic(TOPIC, 3);

    }


    @SpyBean
    private SendOrderUseCase sendOrderUseCase;

    @Test
    void shouldCreateOrderAndPublishKafkaMessage() throws Exception {
        var order = new OrderMockFactory().build();
        var orderRequest = new OrderRequestMockFactory().build();
        var productIds = order.getItems().stream().map(item -> item.getProduct().getId()).collect(Collectors.toSet());
        var expectedOrderOutSent = OrderOut.builder()
                .paymentMethodCustomerId(orderRequest.getPaymentMethodCustomerId())
                .build();

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RECEIVED"));

        var products = validateProductInPort.validateByIds(productIds);

        var captor = ArgumentCaptor.forClass(OrderOut.class);

        verify(sendOrderUseCase, timeout(5000)).send(captor.capture());
        var orderOutSentCaptured = captor.getValue();

        var orderCreated = orderRepositoryOutPort.findById(orderOutSentCaptured.getId());
        expectedOrderOutSent.setId(orderCreated.getId());
        expectedOrderOutSent.setAmount(products.stream().map(Product::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

        assertFalse(products.isEmpty());
       // assertMessagePublished(TOPIC, paymentMapper.toPaymentOrder(expectedOrderOutSent));
    }

    @Test
    void shouldNotPublishMessageWhenInvalidRequest() {
        var product = new ProductMockFactory().getProduct().ofId(null).build();
        var item = new OrderItemMockFactory().getOrderItem().ofProduct(product).build();
        var order = new OrderMockFactory().ofItems(List.of(item)).build();

        assertThrows(IllegalStateException.class, () -> {
         createOrderInPort.createOrder(order);
        });
        assertNoMessageProduced(TOPIC, Duration.ofSeconds(5));
    }

    @Test
    void shouldNotPublishKafkaMessageWhenRequestIsInvalid() throws Exception {
        var item = new OrderItemRequestMockFactory().ofIProductId(null).build();
        var invalidRequest = new OrderRequestMockFactory().ofItems(List.of(item)).build();

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().is5xxServerError());

        assertNoMessageProduced(TOPIC, Duration.ofSeconds(5));
    }

    @Test
    void whenGetOrdersWithoutParams_thenDefaultPaginationIsApplied() throws Exception {

        int totalOrders = 10;

        var orders = IntStream.range(0, totalOrders)
                .mapToObj(i -> {
                    var order = new OrderEntity();
                    order.setId(UUID.randomUUID());
                    order.setPaymentMethodCustomerId(UUID.randomUUID());
                    order.setStatus(OrderStatusEnum.PENDING);
                    order.setCreatedDate(LocalDateTime.now().minusSeconds(i));
                    order.setAmount(BigDecimal.valueOf(200));
                    return order;
                })
                .collect(Collectors.toList());

        orderRepository.saveAll(orders);

        mockMvc.perform(get("/orders/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    void whenGetOrdersWithoutPaginationParams_thenDefaultPageIsReturned() throws Exception {
        int totalOrders = 5;

        List<OrderEntity> orders = IntStream.range(0, totalOrders)
                .mapToObj(i -> {
                    OrderEntity order = new OrderEntity();
                    order.setId(UUID.randomUUID());
                    order.setPaymentMethodCustomerId(UUID.randomUUID());
                    order.setStatus(OrderStatusEnum.PENDING);
                    order.setCreatedDate(LocalDateTime.now().minusMinutes(i));
                    order.setAmount(BigDecimal.valueOf(200));
                    return order;
                })
                .collect(Collectors.toList());

        orderRepository.saveAll(orders);

        mockMvc.perform(get("/orders/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(totalOrders));

        var results = orderRepository.findAll();

        assertFalse(results.isEmpty());
    }

    @Test
    void givenExistingOrder_whenGetOrderById_thenReturnsExpectedOrderResponse() throws Exception {
        var order = new OrderEntityMockFactory().build();
        orderRepository.save(order);


        mockMvc.perform(get("/orders/{id}", order.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId().toString()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.amount").value(300.0))
                .andExpect(jsonPath("$.paymentMethodCustomerId").value(order.getPaymentMethodCustomerId().toString()))
                .andExpect(jsonPath("$.items.length()").value(order.getItems().size()));

    }

    @Test
    void whenGetOrderByIdWithInvalidUUID_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/orders/{id}", "invalid-uuid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void givenExistingOrder_whenUpdateOrder_thenReturnsUpdatedOrderResponse() throws Exception {

        var order = new OrderEntityMockFactory().build();
        order.getItems().forEach(item -> item.setOrder(order));
        orderRepository.save(order);

        var updatedRequest = new OrderRequestMockFactory()
                .ofIStatus(OrderStatusEnum.COMPLETED)
                .ofPaymentMethod(UUID.randomUUID()).build();

        var jsonRequest = new ObjectMapper().writeValueAsString(updatedRequest);

        mockMvc.perform(put("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order.getId().toString()))
                .andExpect(jsonPath("$.status").value(OrderStatusEnum.COMPLETED))
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.paymentMethodCustomerId").value(order.getPaymentMethodCustomerId().toString()))
                .andExpect(jsonPath("$.items.length()").value(order.getItems().size()));
    }


    @TestConfiguration
    static class DisableJpaAuditingConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return Optional::empty;
        }
    }



}
