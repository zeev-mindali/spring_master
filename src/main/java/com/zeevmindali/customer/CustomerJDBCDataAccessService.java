package com.zeevmindali.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Customer> customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, RowMapper<Customer> customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email,age from customer;
                """;

        return jdbcTemplate.query(sql, customerRowMapper);

        //old way
//        return  jdbcTemplate.query(sql, (rs,rowNum)->
//          new Customer (
//                rs.getInt("id"),
//                rs.getString("name"),
//                rs.getString("email"),
//                rs.getInt("age")
//        ));
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = """
                SELECT id, name, email,age from customer where id=?;
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer (name,email,age)
                VALUES (?,?,?)
                """;
        int result = jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbcTemplate.update =" + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT id from customer where email=?;
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, email).intValue() > 0;
    }

    public boolean existsCustomerWithId(int id) {
        var sql = """
                    SELECT count(id)
                    FROM customer
                    WHERE id = ?;
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, id).intValue() > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = """
                    DELETE FROM customer
                    WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer update) {
        var sql = """
                  UPDATE customer
                  SET name=?,email=?,age=?
                  WHERE id=?;
                """;
        jdbcTemplate.update(sql, update.getName(), update.getEmail(), update.getAge(), update.getId());
    }
}
