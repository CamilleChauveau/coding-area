package repository;

import entity.OrderEntity;
import enums.OrderStatus;

import java.util.List;

public interface OrderRepository {
    void save(OrderEntity entity);
    OrderEntity findById(Long id);
    List<OrderEntity> findAllByCustomerId(Long id);
    List<OrderEntity> findAllByOrderStatus(OrderStatus orderStatus);
    void deleteById(Long id);
}
