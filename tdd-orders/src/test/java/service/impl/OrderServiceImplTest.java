package service.impl;

import entity.OrderEntity;
import entity.ProductEntity;
import enums.OrderStatus;
import mapper.OrderMapper;
import mapper.OrderMapperImpl;
import model.Order;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.OrderRepository;
import repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Mock
    private ProductRepository productRepository;

    // Constantes
    private final Long[] IDS = {1L, 2L};
    private final Long CUSTOMER_ID = 1L;
    private final Map<Long, Integer> PRODUCTS_ID_AND_QUANTITY = new HashMap<>();
    private final OrderStatus STATUS = OrderStatus.PENDING;
    private final double EIGHT_BOXES_SHELF_PRICE = 99.99;
    private final double SIX_BOXES_SHELF_PRICE = 79.99;
    private final double FOUR_BOXES_SHELF_PRICE = 59.99;

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

    @Test
    public void calculateTotalAmountShouldReturnPriceOfOneProduct() {
        // Given
        int stockForEachProduct = 2;
        initMapProductsAndQuantity(1, stockForEachProduct);
        Order order = new Order(IDS[0], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);
        List<ProductEntity> productsEntities = createProducts(ProductEntity.class, 1);

        // Mock
        when(productRepository.findById(anyLong()))
                .thenReturn(productsEntities.get(0));

        // When
        BigDecimal totalAmount = orderServiceImpl.calculateTotalOrderAmount(order);

        // Then
        BigDecimal expected = BigDecimal.valueOf(EIGHT_BOXES_SHELF_PRICE).multiply(BigDecimal.valueOf(stockForEachProduct));
        assertEquals(expected, totalAmount);
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void calculateTotalAmountShouldReturnSumOfProducts() {
        // Given
        int stockForEachProduct = 2;
        initMapProductsAndQuantity(3, stockForEachProduct);
        Order order = new Order(IDS[0], CUSTOMER_ID, PRODUCTS_ID_AND_QUANTITY, STATUS);
        List<ProductEntity> productsEntities = createProducts(ProductEntity.class, 3);

        // Mock
        when(productRepository.findById(anyLong()))
                .thenReturn(productsEntities.get(0))
                .thenReturn(productsEntities.get(1))
                .thenReturn(productsEntities.get(2));

        // When
        BigDecimal totalAmount = orderServiceImpl.calculateTotalOrderAmount(order);

        // Then
        BigDecimal expected = BigDecimal.valueOf(EIGHT_BOXES_SHELF_PRICE).multiply(BigDecimal.valueOf(stockForEachProduct))
                .add(BigDecimal.valueOf(SIX_BOXES_SHELF_PRICE).multiply(BigDecimal.valueOf(stockForEachProduct)))
                .add(BigDecimal.valueOf(FOUR_BOXES_SHELF_PRICE).multiply(BigDecimal.valueOf(stockForEachProduct)));

        verify(productRepository, times(3)).findById(anyLong());
        assertEquals(expected, totalAmount);
    }


    private void initMapProductsAndQuantity(int numberProducts, int stockForEachProduct) {
        List<Product> products = createProducts(Product.class, numberProducts);
        products.forEach(product -> {
            PRODUCTS_ID_AND_QUANTITY.put(product.id(), stockForEachProduct);
        });
    }

    private <T> List<T> createProducts(Class<T> type, int numberProducts) {

        if (type == Product.class) {
            List<T> products = new ArrayList<>();
            if (numberProducts >= 1) {
                products.add(type.cast(new Product(1L, "Shelf", "Eight boxes shelf", EIGHT_BOXES_SHELF_PRICE, 2)));
            }
            if (numberProducts > 1) {
                products.add(type.cast(new Product(2L, "Shelf", "Six boxes shelf", SIX_BOXES_SHELF_PRICE, 2)));
            }
            if (numberProducts > 2) {
                products.add(type.cast(new Product(3L, "Shelf", "Four boxes shelf", FOUR_BOXES_SHELF_PRICE, 2)));
            }
            return products;
        } else if (type == ProductEntity.class) {
            List<T> products = new ArrayList<>();
            if (numberProducts >= 1) {
                products.add(type.cast(new ProductEntity(1L, "Shelf", "Eight boxes shelf", EIGHT_BOXES_SHELF_PRICE, 2)));
            }
            if (numberProducts > 1) {
                products.add(type.cast(new ProductEntity(2L, "Shelf", "Six boxes shelf", SIX_BOXES_SHELF_PRICE, 2)));
            }
            if (numberProducts > 2) {
                products.add(type.cast(new ProductEntity(3L, "Shelf", "Four boxes shelf", FOUR_BOXES_SHELF_PRICE, 2)));
            }
            return products;
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }

}