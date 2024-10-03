package mapper;

import entity.CustomerEntity;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperTest {

    private CustomerMapper mapper;

    // Constantes
    private final Long ID = 1L;
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "john.doe@gmail.com";

    @BeforeEach
    public void setUp() {
        this.mapper = new CustomerMapperImpl();
    }

    @Test
    public void customerEntityToDtoShouldReturnCustomer() {
        // Given
        CustomerEntity entity = new CustomerEntity(ID, FIRST_NAME, LAST_NAME, EMAIL);

        // When
        Customer customer = mapper.toDto(entity);

        // Then
        assertEquals(FIRST_NAME, customer.firstName());
        assertEquals(LAST_NAME, customer.lastName());
        assertEquals(EMAIL, customer.email());

    }

    @Test
    public void customerToEntityShouldReturnCustomerEntity() {
        // Given
        Customer customer = new Customer(ID, FIRST_NAME, LAST_NAME, EMAIL);

        // When
        CustomerEntity entity = mapper.toEntity(customer);

        // Then
        assertEquals(FIRST_NAME, entity.getFirstName());
        assertEquals(LAST_NAME, entity.getLastName());
        assertEquals(EMAIL, entity.getEmail());

    }

}