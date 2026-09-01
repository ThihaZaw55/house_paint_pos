package com.thz.house_paint.api.management.output;

import com.thz.house_paint.model.entity.Item;

public record ItemDTO(int itemId, String itemName) {
	
	public static ItemDTO toDTO(Item entity) {
		if(entity == null) return null;
		ItemDTO dto = new ItemDTO(entity.getItemId(), entity.getItemName());
		return dto;
	}
	
}
