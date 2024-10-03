package service.impl;

import entity.OrderEntity;
import enums.OrderStatus;
import mapper.OrderMapper;
import mapper.OrderMapperImpl;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Spy
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

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
        this.orderServiceImpl.placeOrder(order);

        // Then
        verify(orderMapper, times(1)).toEntity(order);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    public void getOrderStatusShouldReturnStatus() {
        // Given
        OrderEntity entity = new OrderEntity(ID, CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);

        // Mock
        when(orderRepository.findById(anyLong())).thenReturn(entity);

        // When
        OrderStatus returnedStatus = this.orderServiceImpl.getOrderStatus(ID);

        // Then
        verify(orderRepository, times(1)).findById(ID);
        assertEquals(STATUS, returnedStatus);

    }

}