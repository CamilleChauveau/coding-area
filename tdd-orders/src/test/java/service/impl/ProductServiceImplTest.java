package service.impl;

import entity.ProductEntity;
import mapper.ProductMapper;
import mapper.ProductMapperImpl;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        this.productMapper = new ProductMapperImpl();
        MockitoAnnotations.openMocks(this);
    }

    // Constantes
    private final Long ID = 1L;
    private final String NAME = "Etagère";
    private final String DESCRIPTION = "Etagère 6 cases";
    private final double PRICE = 100.00;
    private final int STOCK = 10;

    @Test
    public void createProductShouldCallRepositoryAndMapperOnce() {
        // Given
        Product product = new Product(ID, NAME, DESCRIPTION, PRICE, STOCK);

        // When
        productService.createProduct(product);

        // Then
        verify(productMapper, times(1)).toEntity(product);
        verify(productRepository, times(1)).save(any(ProductEntity.class));

    }

    @Test
    public void getProductShouldCallRepositoryAndMapperOnce() {
        // Given
        ProductEntity entity = new ProductEntity(ID, NAME, DESCRIPTION, PRICE, STOCK);

        // Mock
        when(productRepository.findById(anyLong())).thenReturn(entity);

        // When
        Product returnedProduct = this.productService.getProduct(ID);

        // Then
        verify(productMapper, times(1)).toDto(any(ProductEntity.class));
        assertAll(
                () -> assertEquals(NAME, returnedProduct.name()),
                () -> assertEquals(DESCRIPTION, returnedProduct.description()),
                () -> assertEquals(PRICE, returnedProduct.price()),
                () -> assertEquals(STOCK, returnedProduct.stock())
        );
    }

    @Test
    public void updateStockShouldCallRepositoryFindFirstSaveNext() {
        // Given
        int newStock = STOCK-1;
        ProductEntity entity = new ProductEntity(ID, NAME, DESCRIPTION, PRICE, STOCK);

        // Mock
        when(productRepository.findById(anyLong())).thenReturn(entity);

        // When
        this.productService.updateStock(ID, newStock);

        // Then
        InOrder inOrder = inOrder(productRepository);
        inOrder.verify(productRepository).findById(anyLong());
        inOrder.verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    public void deleteProductShouldCallRepositoryOnce() {
        // When
        this.productService.deleteProduct(ID);

        // Then
        verify(productRepository, times(1)).deleteById(anyLong());
    }

}