package com.dgmf.messaging;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    // Method for Receiving Messages
    public void receiveMessage(String message) {
        System.out.println("Receive <" + message + ">");

        // To Signal that the Message Has Been Received. Not to
        // Implement in Production
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
