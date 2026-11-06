package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaleForm(
		 String invoice, 
		 String customerName,
		 LocalDate saleDate, 
		 BigDecimal totalAmount,
		 List<SaleItemForm> items){

}
