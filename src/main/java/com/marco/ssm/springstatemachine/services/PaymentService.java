package com.marco.ssm.springstatemachine.services;

import com.marco.ssm.springstatemachine.domain.Payment;
import com.marco.ssm.springstatemachine.domain.PaymentEvent;
import com.marco.ssm.springstatemachine.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    Payment newPayment(Payment payment);
    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);

}
