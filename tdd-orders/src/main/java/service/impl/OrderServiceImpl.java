package service.impl;

import entity.OrderEntity;
import enums.OrderStatus;
import mapper.OrderMapper;
import model.Order;
import repository.OrderRepository;
import service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
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

}
