package com.vasanthan.ecommerce.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.vasanthan.ecommerce.dto.PaymentInfo;
import com.vasanthan.ecommerce.dto.Purchase;
import com.vasanthan.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
