package mapper;

import entity.CustomerEntity;
import model.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer toDto(CustomerEntity entity);
    CustomerEntity toEntity(Customer customer);
}
