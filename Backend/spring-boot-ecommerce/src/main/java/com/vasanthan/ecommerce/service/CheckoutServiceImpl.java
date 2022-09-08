package com.vasanthan.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.vasanthan.ecommerce.dao.CustomerRespository;
import com.vasanthan.ecommerce.dto.PaymentInfo;
import com.vasanthan.ecommerce.dto.Purchase;
import com.vasanthan.ecommerce.dto.PurchaseResponse;
import com.vasanthan.ecommerce.entity.Customer;
import com.vasanthan.ecommerce.entity.Order;
import com.vasanthan.ecommerce.entity.OrderItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    CustomerRespository customerRespository;

    public CheckoutServiceImpl(CustomerRespository customerRespository,
                               @Value("${stripe.key.secret}") String secretKey) {
        this.customerRespository=customerRespository;
        Stripe.apiKey=secretKey;
    }
    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // Retrive the order info from dto
        Order order=purchase.getOrder();

        // Generate tracking number
        String orderTrackingNumber=getOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Populate order with order items
        Set<OrderItem> orderItems=purchase.getOrderItems();
        for (OrderItem item : orderItems) {
            order.add(item);
        }

        // Populate order with billing address and shipping address
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // Populate Customer with order
        Customer customer=purchase.getCustomer();

        // Check if the customer's email already exists
        String theEmail=customer.getEmail();
        Customer customerFromDb=customerRespository.findByEmail(theEmail);
        if(customerFromDb!=null) {
            customer=customerFromDb;
        }
        customer.add(order);

        // Save to database
        customerRespository.save(customer);
        // Return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes=new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params=new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }

    private String getOrderTrackingNumber() {

        // Generate random UUID
        return UUID.randomUUID().toString();
    }
}
