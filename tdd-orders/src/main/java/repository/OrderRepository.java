package repository;

import entity.OrderEntity;

public interface OrderRepository {
    void save(OrderEntity entity);
    OrderEntity findById(Long id);
}
