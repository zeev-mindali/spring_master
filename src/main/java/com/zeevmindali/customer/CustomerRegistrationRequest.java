package com.zeevmindali.customer;

public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age) {
}
