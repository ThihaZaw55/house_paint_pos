package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PurchaseItemForm(
		@NotNull(message = "Product ID မဖြစ်မနေ ပါရပါမည်")
	    Integer productId,

	    @Min(value = 1, message = "ဝယ်ယူသည့် အရေအတွက်သည် အနည်းဆုံး 1 ဖြစ်ရပါမည်")
		int buyQuantity,

	    @NotNull @PositiveOrZero
	    BigDecimal unitCostPrice,

	    @NotNull @PositiveOrZero
	    BigDecimal unitSalesPrice
		) 
{

}
