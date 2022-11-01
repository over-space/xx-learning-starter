package com.learning.basic.disruptor.demo02;

import com.lmax.disruptor.EventFactory;

public class OrderEvent implements EventFactory<OrderEvent> {

    private String message;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public OrderEvent newInstance() {
        // TODO Auto-generated method stub
        return new OrderEvent();
    }
}
