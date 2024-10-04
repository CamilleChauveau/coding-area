package service.impl;

import entity.OrderEntity;
import entity.ProductEntity;
import enums.OrderStatus;
import mapper.OrderMapper;
import model.Order;
import repository.OrderRepository;
import repository.ProductRepository;
import service.OrderService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void placeOrder(Order order) {
        OrderEntity entity = this.orderMapper.toEntity(order);
        this.orderRepository.save(entity);
    }

    @Override
    public OrderStatus getOrderStatus(Long id) {
        OrderEntity entity = this.orderRepository.findById(id);
        return entity.getStatus();
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        List<OrderEntity> entities = this.orderRepository.findAllByCustomerId(customerId);
        return entities.stream()
                .map(orderEntity -> this.orderMapper.toDto(orderEntity))
                .toList();
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        List<OrderEntity> entities = this.orderRepository.findAllByOrderStatus(orderStatus);
        return entities.stream()
                .map(orderEntity -> this.orderMapper.toDto(orderEntity))
                .toList();
    }

    @Override
    public List<Order> getOrdersByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus) {
        return List.of();
    }

    @Override
    public void deleteOrder(Long id) {
        this.orderRepository.deleteById(id);
    }

    @Override
    public BigDecimal calculateTotalOrderAmount(Order order) {
        Map<Long, Integer> productsIdsAndQuantity = order.productsIdsAndQuantity();
        Map<ProductEntity, Integer> productsAndQuantity = new HashMap<>();
        BigDecimal totalOrderAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : productsIdsAndQuantity.entrySet()) {
            ProductEntity productEntity = productRepository.findById(entry.getKey());
            Integer quantity = entry.getValue();
            productsAndQuantity.put(productEntity, quantity);
        }

        for (Map.Entry<ProductEntity, Integer> entry : productsAndQuantity.entrySet()) {
            ProductEntity productEntity = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal productPrice = BigDecimal.valueOf(productEntity.getPrice());
            BigDecimal totalPriceForSameProduct = productPrice.multiply(BigDecimal.valueOf(quantity));
            totalOrderAmount = totalOrderAmount.add(totalPriceForSameProduct);
        }

        return totalOrderAmount;
    }


}
