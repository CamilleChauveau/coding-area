package service.impl;

import entity.CustomerEntity;
import mapper.CustomerMapper;
import mapper.CustomerMapperImpl;
import model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    // Constantes
    private final Long ID = 1L;
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Doe";
    private final String EMAIL = "john.doe@gmail.com";

    @BeforeEach
    public void setUp() {
        this.customerMapper  = new CustomerMapperImpl();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCustomerShouldCallRepositoryAndMapperOnce() {
        // Given
        Customer customer = new Customer(ID, FIRST_NAME, LAST_NAME, EMAIL);

        // When
        customerServiceImpl.createCustomer(customer);

        // Then
        verify(customerMapper, times(1)).toEntity(customer);
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    public void getCustomerByIdShouldReturnCustomer() {
        // Given
        CustomerEntity entity = new CustomerEntity(ID, FIRST_NAME, LAST_NAME, EMAIL);

        // Mock
        when(customerRepository.findById(anyLong())).thenReturn(entity);

        // When
        Customer returnedCustomer = customerServiceImpl.getCustomerById(ID);

        // Then
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerMapper, times(1)).toDto(entity);
        assertAll(
                () -> assertEquals(FIRST_NAME, returnedCustomer.firstName()),
                () -> assertEquals(LAST_NAME, returnedCustomer.lastName()),
                () -> assertEquals(EMAIL, returnedCustomer.email())
        );
    }

    @Test
    public void saveCustomerShouldCallRepositoryAndMapperOnce() {
        // Given
        Customer customer = new Customer(ID, FIRST_NAME, LAST_NAME, EMAIL);

        // When
        customerServiceImpl.saveCustomer(customer);

        // Then
        verify(customerMapper, times(1)).toEntity(customer);
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    public void deleteCustomerShouldCallRepositoryOnce() {
        // When
        customerServiceImpl.deleteCustomer(ID);

        // Then
        verify(customerRepository, times(1)).deleteById(anyLong());
    }

}