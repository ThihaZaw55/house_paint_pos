package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record SaleForm(
		 String invoiceUuid, 
		 String customerName,
		 LocalDateTime saleDate, 
		 BigDecimal totalAmount,
		 List<SaleItemForm> items){

}
