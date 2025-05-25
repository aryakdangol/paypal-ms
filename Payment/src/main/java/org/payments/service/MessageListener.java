package org.payments.service;



public interface MessageListener {

    public void consumeEvent(Object message);
}
