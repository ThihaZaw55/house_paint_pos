package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thz.house_paint.model.entity.Sale;


public record PaymentForm(
		Sale sale,BigDecimal amount,
String paymentMethod,
String status,
String note,
LocalDateTime paymentDate) {

}
