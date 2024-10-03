package mapper;

import entity.OrderEntity;
import model.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    Order toDto(OrderEntity entity);
    OrderEntity toEntity(Order dto);
}
