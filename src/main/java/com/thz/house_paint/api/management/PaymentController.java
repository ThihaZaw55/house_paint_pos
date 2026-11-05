package com.thz.house_paint.api.management;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thz.house_paint.api.management.input.SaleForm; // သို့မဟုတ် PaymentForm
import com.thz.house_paint.api.management.output.ApiResponse;
import com.thz.house_paint.api.management.service.PaymentService;
import com.thz.house_paint.model.entity.Payment;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
	
    private final PaymentService paymentService; // final ထည့်ခြင်းဖြင့် Dependency Injection ပိုမိုခိုင်မာစေသည်

    // 1. CREATE PAYMENT (Complete Payment & Stock Deduct)
    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> createPayment(@RequestBody SaleForm request) {
        Payment savedPayment = paymentService.savePayment(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment Completed Successfully", savedPayment));
    }

    // 2. READ ALL PAYMENTS (GET ALL)
    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all payments successfully", payments));
    }

    // 3. READ PAYMENT BY ID (GET BY ID)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment found successfully", payment));
    }

    // 4. DELETE PAYMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment deleted successfully", null));
    }
}