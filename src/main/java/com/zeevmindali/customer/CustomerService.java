package com.zeevmindali.customer;

import com.zeevmindali.exceptions.DuplicateResourceException;
import com.zeevmindali.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;
    private final CustomerRepository customerRepository;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, CustomerRepository customerRepository) {
        this.customerDao = customerDao;
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomers(Integer id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email already exists");
        }
        //add
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        ));
    }

    public void deleteCustomer(Integer id){
        //customer is not exists
        if (!customerRepository.existsById(id)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }
}
