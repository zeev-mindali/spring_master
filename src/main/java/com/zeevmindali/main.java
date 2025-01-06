package com.zeevmindali;

import com.github.javafaker.Faker;
import com.zeevmindali.customer.Customer;
import com.zeevmindali.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication

public class main {
    public static void main(String[] args) {
        SpringApplication.run(main.class,args);

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        Faker faker = new Faker();
        return args -> {
            Customer alex = new Customer(
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.number().numberBetween(18,120)
            );
            Customer jamila = new Customer(
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.number().numberBetween(18,120)
            );

            List<Customer> customers = List.of(alex,jamila);
            //customerRepository.saveAll(customers);
        };
    }
}
