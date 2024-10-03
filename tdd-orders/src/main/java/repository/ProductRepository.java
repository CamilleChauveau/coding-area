package repository;

import entity.ProductEntity;

public interface ProductRepository {
    void save(ProductEntity entity);
    ProductEntity findById(Long id);
    void deleteById(Long id);
}
