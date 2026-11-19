package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PaymentForm(
		String invoice, 
		String customerName,
		LocalDate saleDate, 
		BigDecimal totalAmount,
		BigDecimal amount,
		String paymentMethod,
		String status,
		String note,
		LocalDateTime paymentDate,
		List<SaleItemForm> items) {

}
