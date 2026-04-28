package ms.order.service.infrastructure.adapter.http;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.port.in.ChangeOrderInPort;
import ms.order.service.domain.port.in.CreateOrderInPort;
import ms.order.service.domain.port.in.DeleteOrderInPort;
import ms.order.service.domain.port.in.GetOrderInPort;
import ms.order.service.infrastructure.adapter.mapper.OrderMapper;
import ms.order.service.infrastructure.dto.http.request.OrderRequest;
import ms.order.service.infrastructure.dto.http.response.OrderResponse;
import ms.order.service.infrastructure.dto.http.response.OrderStatusResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@Log4j2
@RequiredArgsConstructor
@Tag(name = "Controller for crud", description = "CRUD order app")
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderMapper orderMapper;
    private final CreateOrderInPort createOrderInPort;
    private final GetOrderInPort getOrderInPort;
    private final ChangeOrderInPort changeOrderInPort;
    private final DeleteOrderInPort deleteOrderInPort;

    @Operation(description = "Create new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Unexpected error in server.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderStatusResponse> create(@Valid @RequestBody OrderRequest request) {
        log.info("Create order was triggered successfully by Controller");
        createOrderInPort.createOrder(orderMapper.requestToDomain(request));
        return ResponseEntity.ok(OrderStatusResponse.builder().status("RECEIVED").build());
    }

    @Operation(description = "Get orders paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Unexpected error in server.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        var pagination = PageRequest.of(page, size);
        log.info("Get all orders was triggered successfully by Controller. %s".formatted(pagination));
        var responses = getOrderInPort.getPaginated(pagination).stream().map(orderMapper::domainToResponse).toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(description = "Get order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error in server.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") UUID id) {
        log.info("Get order by id was triggered successfully by Controller");
        return ResponseEntity.ok(orderMapper.domainToResponse(getOrderInPort.getById(id)));
    }

    @Operation(description = "Update order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error in server.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody OrderRequest request) {
        log.info("Update was triggered successfully by Controller");
        var response = orderMapper.domainToResponse(changeOrderInPort.updateById(orderMapper.requestToDomain(request)));

        return ResponseEntity.ok(response);
    }

    @Operation(description = "Delete order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "No content"),
            @ApiResponse(responseCode = "500", description = "Unexpected error in server.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable("id") UUID id) {
        log.info("Delete by id order was triggered successfully by Controller");

        deleteOrderInPort.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
