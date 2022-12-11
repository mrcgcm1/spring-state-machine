package com.marco.ssm.springstatemachine.services;

import com.marco.ssm.springstatemachine.domain.Payment;
import com.marco.ssm.springstatemachine.domain.PaymentEvent;
import com.marco.ssm.springstatemachine.domain.PaymentState;
import com.marco.ssm.springstatemachine.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.util.Assert;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Test
    void preAuth() {

        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

        Assert.isTrue(preAuthPayment != null, "Il pagamento non esiste");
        System.out.println("Stato reale " + sm.getState().getId());
        Assert.isTrue(PaymentState.PRE_AUTH.equals((PaymentState) sm.getState().getId()), "Lo stato non è quello atteso");

    }
}