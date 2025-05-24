package org.payments.service;

import org.payments.model.Transaction;

public interface PaymentService {

    public Transaction createOrder(String username);

    public void completeOrderSuccess();

    public void completeOrderFailed();

    public void notification();

}
