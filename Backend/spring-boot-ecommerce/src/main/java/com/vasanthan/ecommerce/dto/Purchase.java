package com.vasanthan.ecommerce.dto;

import com.vasanthan.ecommerce.entity.Address;
import com.vasanthan.ecommerce.entity.Customer;
import com.vasanthan.ecommerce.entity.Order;
import com.vasanthan.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
