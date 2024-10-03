package service;

import model.Product;

public interface ProductService {

    void createProduct(Product product);
    Product getProduct(Long productId);
    void updateStock(Long productId, int stock);
    void deleteProduct(Long productId);

}
