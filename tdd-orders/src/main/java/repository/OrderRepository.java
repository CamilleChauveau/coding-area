package repository;

import entity.OrderEntity;

public interface OrderRepository {
    void save(OrderEntity entity);
}
