package service.impl;

import enums.OrderStatus;
import model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.OrderRepository;
import service.OrderService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Spy
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderService orderService;

    // Constantes
    private final Long ID = 1L;
    private final Long CUSTOMER_ID = 1L;
    private final Map<Long, Integer> PRODUCTS_ID_AND_QUANTITY = Map.of();
    private final OrderStatus STATUS = OrderStatus.PENDING;

    @BeforeEach
    public void setUp() {
        this.orderMapper = new OrderMapperImpl();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void placeOrderShouldCallRepositoryAndMapperOnce() {
        // Given
        Order order = new Order(ID, CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);

        // When
        this.orderService.placeOrder(order);

        // Then
        verify(orderMapper, times(1)).toEntity(order);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

}