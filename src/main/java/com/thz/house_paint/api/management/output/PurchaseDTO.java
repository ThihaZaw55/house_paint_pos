package com.thz.house_paint.api.management.output;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.thz.house_paint.model.entity.Purchase;
import com.thz.house_paint.model.entity.PurchaseItem;

public record PurchaseDTO(
		UUID purchaseId,
	    String supplierName,
		LocalDate purchaseDate,
		BigDecimal totalAmount,
		List<PurchaseItem> items
	    ) 
{

	public static PurchaseDTO toDTO(Purchase entity) {
		if(entity == null) return null;
		
		var dto = new PurchaseDTO(
				entity.getPurchaseId(),
				entity.getSupplierName(),
				entity.getPurchaseDate(),
				entity.getTotalAmount(),
				entity.getItems());
		return dto;
	}
}
