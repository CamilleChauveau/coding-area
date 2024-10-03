package service.impl;

import entity.CustomerEntity;
import mapper.CustomerMapper;
import model.Customer;
import repository.CustomerRepository;
import service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public void createCustomer(Customer customer) {
        this.saveCustomer(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id);
        return customerMapper.toDto(entity);
    }

    @Override
    public void saveCustomer(Customer customer) {
        CustomerEntity entity = this.customerMapper.toEntity(customer);
        this.customerRepository.save(entity);
    }

    @Override
    public void deleteCustomer(Long id) {
        this.customerRepository.deleteById(id);
    }

}
