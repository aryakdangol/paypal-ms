package org.payments.service;

public interface PaymentService {

    public void  createOrder();

    public void completeOrderSuccess();

    public void completeOrderFailed();

    public void notification();


}
