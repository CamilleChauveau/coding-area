package service.impl;

import entity.ProductEntity;
import mapper.ProductMapper;
import model.Product;
import repository.ProductRepository;
import service.ProductService;

public class ProductServiceImpl implements ProductService {

    private ProductMapper productMapper;
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    @Override
    public void createProduct(Product product) {
        ProductEntity entity = this.productMapper.toEntity(product);
        this.productRepository.save(entity);
    }

    @Override
    public Product getProduct(Long productId) {
        ProductEntity entity = this.productRepository.findById(productId);
        return this.productMapper.toDto(entity);
    }

    @Override
    public void updateStock(Long productId, int stock) {
        ProductEntity entity = this.productRepository.findById(productId);
        entity.setStock(stock);
        this.productRepository.save(entity);
    }

    @Override
    public void deleteProduct(Long productId) {
        this.productRepository.deleteById(productId);
    }

}
