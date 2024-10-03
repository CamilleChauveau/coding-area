package model;

import enums.OrderStatus;

import java.util.Map;

public record Order(Long id, Long customerId, Map<Long, Integer> productsIdsAndQuantity, OrderStatus status) {
}
