package service;

import model.Customer;

public interface CustomerService {

    void createCustomer(Customer customer);
    Customer getCustomerById(Long id);
    void saveCustomer(Customer customer);
    void deleteCustomer(Long id);

}
