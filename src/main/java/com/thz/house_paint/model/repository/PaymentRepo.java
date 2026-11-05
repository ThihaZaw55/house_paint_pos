package com.thz.house_paint.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

	
}
