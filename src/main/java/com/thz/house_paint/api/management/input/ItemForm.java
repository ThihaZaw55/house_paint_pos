package com.thz.house_paint.api.management.input;

import com.thz.house_paint.model.entity.Item;

import jakarta.validation.constraints.NotBlank;

public record ItemForm(
		@NotBlank(message = "Please enter item.")
		String itemName) {
	
	public Item toEntity() {
		var entity = new Item();
		entity.setItemName(itemName);
		return entity;
	}

}
