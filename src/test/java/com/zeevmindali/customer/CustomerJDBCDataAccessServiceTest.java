package com.zeevmindali.customer;

import com.zeevmindali.AbstractTestContainers;
import com.zeevmindali.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + " " + UUID.randomUUID(),
                FAKER.number().numberBetween(18, 70)
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> actual = underTest.selectAllCustomers();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        var email = FAKER.internet().emailAddress() + " " + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(18, 70)
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //given
        int id = -1;

        //when
        var actual = underTest.selectCustomerById(id);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        var email = FAKER.internet().emailAddress() + " " + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(18, 70)
        );
        underTest.insertCustomer(customer);
        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        var email = FAKER.internet().emailAddress() + " " + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(18, 70)
        );

        //when
        underTest.insertCustomer(customer);

        // Then
        assertThat(underTest.existsPersonWithEmail(email)).isTrue();
    }

    @Test
    void existsCustomerWithId() {
        // Given
        var email = FAKER.internet().emailAddress() + " " + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(18, 70)
        );

        //when
        underTest.insertCustomer(customer);

        // Then
        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        assertThat(underTest.existsCustomerWithId(id)).isTrue();
    }

    @Test
    void deleteCustomerById() {
        // Given
        var email = FAKER.internet().emailAddress() + " " + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(18, 70)
        );

        //when
        underTest.insertCustomer(customer);

        //then
        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        //check if id still exists
        assertThat(underTest.existsCustomerWithId(id)).isFalse();
    }

    @Test
    void updateCustomer() {
        // Given
        var email = FAKER.internet().emailAddress() + " " + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(18, 70)
        );

        //when
        underTest.insertCustomer(customer);

        Integer id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        System.out.println("id: " + id);


        Customer update = new Customer();
        update.setEmail("zeev@test.test");
        update.setAge(50);
        update.setName("zeev");
        update.setId(id);
        System.out.println("update: " + update);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        System.out.println("Actual: " + actual);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(update.getId());
            assertThat(c.getEmail()).isEqualTo(update.getEmail());
            assertThat(c.getAge()).isEqualTo(update.getAge());
        });


    }
}