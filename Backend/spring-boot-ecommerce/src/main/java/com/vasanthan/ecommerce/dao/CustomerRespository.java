package com.vasanthan.ecommerce.dao;

import com.vasanthan.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRespository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String theEmail);
}
