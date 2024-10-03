package repository;

import entity.CustomerEntity;

public interface CustomerRepository {
    void save(CustomerEntity customerEntity);
    CustomerEntity findById(Long id);
    void deleteById(Long id);
}
