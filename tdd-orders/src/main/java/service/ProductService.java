package service;

import model.Product;

public interface ProductService {

    void createProduct(Product product);
    Product getProduct(String productId);
    void updateStock(String productId, int stock);
    void deleteProduct(String productId);

}
