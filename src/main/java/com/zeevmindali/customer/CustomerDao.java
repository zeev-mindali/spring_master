package com.zeevmindali.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    void deleteCustomerById(Integer id);
}
