package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.thz.house_paint.model.entity.Purchase;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PurchaseForm(
		 
		String supplierName,
	    
	    @NotNull(message = "ဝယ်ယူသည့် ရက်စွဲ ပါရပါမည်")
	    LocalDate purchaseDate,
	    
	    @NotNull(message = "Total not null")
	    BigDecimal totalCost,
	    
	    @NotEmpty(message = "ဝယ်ယူသည့် ပစ္စည်းစာရင်း အနည်းဆုံး ၁ ခု ပါရပါမည်")
	    List<PurchaseItemForm> items
	    ) 
{
	public Purchase toEntity(PurchaseForm form) {
		var entity = new Purchase();
		entity.setSupplierName(supplierName);
		entity.setPurchaseDate(purchaseDate);
		entity.setTotalAmount(totalCost);
		return entity;
	}
}
