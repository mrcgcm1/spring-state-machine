package com.marco.ssm.springstatemachine.services;

import com.marco.ssm.springstatemachine.domain.Payment;
import com.marco.ssm.springstatemachine.domain.PaymentEvent;
import com.marco.ssm.springstatemachine.domain.PaymentState;
import com.marco.ssm.springstatemachine.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Test
    void preAuth() {

        Payment savedPayment = paymentService.newPayment(payment);
        Assert.isTrue(PaymentState.NEW.equals(savedPayment.getState()), "Lo stato non è quello atteso");
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

        Assert.isTrue(preAuthPayment != null, "Il pagamento non esiste");
        System.out.println("Stato reale " + sm.getState().getId());
        Assert.isTrue(PaymentState.PRE_AUTH.equals((PaymentState) sm.getState().getId()), "Lo stato non è quello atteso");
    }

    @Transactional
    @RepeatedTest(10)
    void Auth() {

        Payment savedPayment = paymentService.newPayment(payment);
        Assert.isTrue(PaymentState.NEW.equals(savedPayment.getState()), "Lo stato non è quello atteso");
        StateMachine<PaymentState, PaymentEvent> preAuthSM = paymentService.preAuth(savedPayment.getId());

        if (preAuthSM.getState().getId() == PaymentState.PRE_AUTH) {
            System.out.println("Payment is Pre Authorized");
            StateMachine<PaymentState, PaymentEvent> sm = paymentService.authorizePayment(savedPayment.getId());
            Payment authPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

            System.out.println("Stato reale " + sm.getState().getId());
        }else{
            System.out.println("Payment failed pre-auth...");

        }
    }

}