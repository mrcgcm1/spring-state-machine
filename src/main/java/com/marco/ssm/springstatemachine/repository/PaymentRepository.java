package com.marco.ssm.springstatemachine.repository;

import com.marco.ssm.springstatemachine.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
