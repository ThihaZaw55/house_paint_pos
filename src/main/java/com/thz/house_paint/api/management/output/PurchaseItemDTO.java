package com.thz.house_paint.api.management.output;

import java.math.BigDecimal;
import java.util.UUID;

import com.thz.house_paint.model.entity.PurchaseItem;

public record PurchaseItemDTO(
		UUID purchaseItemId,
		UUID purchaseId,
	    int productId,
	    int buyQuantity,
	    BigDecimal unitCostPrice,
	    BigDecimal unitSalesPrice
		 ) {
	
	public static PurchaseItemDTO toDTO(PurchaseItem entity) {
		
		var dto =  new PurchaseItemDTO(entity.getPurchaseItemId(), entity.getPurchase().getPurchaseId(),
				entity.getProduct().getProductId(),
				entity.getBuyQuantity(), entity.getUnitCostPrice(),
				entity.getUnitSalesPrice());
		
		return dto;
		
	}
}
