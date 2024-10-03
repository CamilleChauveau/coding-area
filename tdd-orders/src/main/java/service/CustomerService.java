package service;

import model.Customer;

public interface CustomerService {

    void createCustomer(Customer customer);
    Customer getCustomer(long id);
    void updateCustomer(Customer customer);
    void deleteCustomer(long id);

}
