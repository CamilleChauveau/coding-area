package entity;

import enums.OrderStatus;

import java.util.Map;

public class OrderEntity {
    private Long id;
    private Long cutomerId;
    private Map<Long, Integer> productsIdsAndQuantity;
    private OrderStatus status;

    public OrderEntity(Long id, Long cutomerId, Map<Long, Integer> productsIdsAndQuantity, OrderStatus status) {
        this.id = id;
        this.cutomerId = cutomerId;
        this.productsIdsAndQuantity = productsIdsAndQuantity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCutomerId() {
        return cutomerId;
    }

    public void setCutomerId(Long cutomerId) {
        this.cutomerId = cutomerId;
    }

    public Map<Long, Integer> getProductsIdsAndQuantity() {
        return productsIdsAndQuantity;
    }

    public void setProductsIdsAndQuantity(Map<Long, Integer> productsIdsAndQuantity) {
        this.productsIdsAndQuantity = productsIdsAndQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
