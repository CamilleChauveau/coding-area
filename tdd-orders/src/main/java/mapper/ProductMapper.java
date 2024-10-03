package mapper;

import entity.ProductEntity;
import model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    Product toDto(ProductEntity product);
    ProductEntity toEntity(Product product);
}
