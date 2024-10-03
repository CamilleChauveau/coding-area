package service.impl;

import enums.OrderStatus;
import model.Order;
import service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Override
    public void placeOrder(Order order) {

    }

    @Override
    public OrderStatus getOrderStatus(Long id) {
        return null;
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return List.of();
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        return List.of();
    }

    @Override
    public List<Order> getOrdersByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus) {
        return List.of();
    }

    @Override
    public void deleteOrder(Long id) {

    }

}
