package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;

import com.thz.house_paint.model.entity.Colour;
import com.thz.house_paint.model.entity.Item;
import com.thz.house_paint.model.entity.Product;
import com.thz.house_paint.model.entity.Unit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductForm(
		@NotNull(message = "Item ID မဖြစ်မနေ ပါရပါမည်")
	    Integer itemId,

	    @NotNull(message = "Unit ID မဖြစ်မနေ ပါရပါမည်")
	    Integer unitId,
	    
	    Integer colourId,
	    
	    @NotNull(message = "Stock quantity မဖြစ်မနေ ပါရပါမည်")
	    @PositiveOrZero(message = "Stock quantity သည် 0 သို့မဟုတ် 0 ထက် ကြီးရပါမည်")
	    Integer stockQuantity,
	    
	    @NotNull(message = "Cost price မဖြစ်မနေ ပါရပါမည်")
	    @PositiveOrZero(message = "Cost price သည် 0 သို့မဟုတ် 0 ထက် ကြီးရပါမည်")
	    BigDecimal costPrice,
	    
	    @NotNull(message = "Sales price မဖြစ်မနေ ပါရပါမည်")
	    @PositiveOrZero(message = "Sales price သည် 0 သို့မဟုတ် 0 ထက် ကြီးရပါမည်")
	    BigDecimal salePrice
	    
	    //String imageUrl
) {
	public Product toEntity(Item item, Unit unit, Colour colour, String imageUrl) {
		        Product product = new Product();
		        product.setItem(item);
		        product.setUnit(unit);
		        product.setColour(colour);
		        product.setStockQuantity(this.stockQuantity());
		        product.setCostPrice(this.costPrice());
		        product.setSalePrice(this.salePrice());
		        product.setImageUrl(imageUrl);
		        return product;
	}
}
