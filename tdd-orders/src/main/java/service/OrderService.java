package service;

import enums.OrderStatus;
import model.Order;

import java.util.List;

public interface OrderService {

    void placeOrder(Order order);
    OrderStatus getOrderStatus(Long id);
    List<Order> getOrdersByCustomerId(Long customerId);
    List<Order> getOrdersByStatus(OrderStatus orderStatus);
    List<Order> getOrdersByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus);
    void deleteOrder(Long id);

}
