package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;

import com.thz.house_paint.model.entity.Colour;
import com.thz.house_paint.model.entity.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductForm(
	    Integer colourId,
	    
	    @NotNull(message = "Stock quantity မဖြစ်မနေ ပါရပါမည်")
	    @PositiveOrZero(message = "Stock quantity သည် 0 သို့မဟုတ် 0 ထက် ကြီးရပါမည်")
	    Integer stockQuantity,
	    
	    @NotNull(message = "Cost price မဖြစ်မနေ ပါရပါမည်")
	    @PositiveOrZero(message = "Cost price သည် 0 သို့မဟုတ် 0 ထက် ကြီးရပါမည်")
	    BigDecimal costPrice,
	    
	    @NotNull(message = "Sales price မဖြစ်မနေ ပါရပါမည်")
	    @PositiveOrZero(message = "Sales price သည် 0 သို့မဟုတ် 0 ထက် ကြီးရပါမည်")
	    BigDecimal salesPrice
) {
	public Product toEntity(Colour colour, String imageUrl) {
		        Product product = new Product();
		        product.setColour(colour);
		        product.setStockQuantity(this.stockQuantity());
		        product.setCostPrice(this.costPrice());
		        product.setSalesPrice(this.salesPrice());
		        product.setImageUrl(imageUrl);
		        return product;
	}
}
