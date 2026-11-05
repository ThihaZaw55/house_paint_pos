package com.thz.house_paint.api.management.input;

public record OrderItemForm(
		  Long productId,
		  int quantity,
		  double price) 
{
	
}
