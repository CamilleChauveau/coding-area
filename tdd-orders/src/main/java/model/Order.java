package model;

import enums.OrderStatus;

import java.util.List;

public record Order(String id, String product, int quantity, OrderStatus status, String customerId, List<String> productsIds) {
}
