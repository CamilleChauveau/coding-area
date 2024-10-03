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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private final Long[] IDS = {1L, 2L};
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
        Order order = new Order(IDS[0], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);

        // When
        this.orderServiceImpl.placeOrder(order);

        // Then
        verify(orderMapper, times(1)).toEntity(order);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    public void getOrderStatusShouldReturnStatus() {
        // Given
        OrderEntity entity = new OrderEntity(IDS[0], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);

        // Mock
        when(orderRepository.findById(anyLong())).thenReturn(entity);

        // When
        OrderStatus returnedStatus = this.orderServiceImpl.getOrderStatus(IDS[0]);

        // Then
        verify(orderRepository, times(1)).findById(IDS[0]);
        assertEquals(STATUS, returnedStatus);

    }

    @Test
    public void getOrdersByCustomerIdShouldReturnNoOrder() {
        // Mock
        when(orderRepository.findAllByCustomerId(CUSTOMER_ID)).thenReturn(List.of());

        // When
        List<Order> orders = this.orderServiceImpl.getOrdersByCustomerId(CUSTOMER_ID);

        // Then
        verify(orderRepository, times(1)).findAllByCustomerId(CUSTOMER_ID);
        verifyNoInteractions(orderMapper);
        assertTrue(orders.isEmpty());
    }

    @Test
    public void getOrdersByCustomerIdShould2Orders() {
        // Given
        OrderEntity firstOrderEntity = new OrderEntity(IDS[0], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);
        OrderEntity secondOrderEntity = new OrderEntity(IDS[1], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);

        // Mock
        when(orderRepository.findAllByCustomerId(CUSTOMER_ID))
                .thenReturn(List.of(firstOrderEntity, secondOrderEntity));

        // When
        List<Order> orders = this.orderServiceImpl.getOrdersByCustomerId(CUSTOMER_ID);

        // Then
        verify(orderRepository, times(1)).findAllByCustomerId(CUSTOMER_ID);
        verify(orderMapper, times(2)).toDto(any(OrderEntity.class));
        assertEquals(2, orders.size());
    }

    @Test
    public void getOrderByStatusShouldReturn2Orders() {
        // Given
        OrderEntity firstOrderEntity = new OrderEntity(IDS[0], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);
        OrderEntity secondOrderEntity = new OrderEntity(IDS[1], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);

        // When
        when(orderRepository.findAllByOrderStatus(STATUS)).thenReturn(List.of(firstOrderEntity, secondOrderEntity));

        // When
        List<Order> orders = this.orderServiceImpl.getOrdersByStatus(STATUS);

        // Then
        verify(orderRepository, times(1)).findAllByOrderStatus(STATUS);
        verify(orderMapper, times(2)).toDto(any(OrderEntity.class));
        assertEquals(2, orders.size());
    }

    @Test
    public void deleteOrderShouldCallRepositoryOnce() {
        // When
        this.orderServiceImpl.deleteOrder(IDS[0]);

        // Then
        verify(orderRepository, times(1)).deleteById(IDS[0]);
    }

}