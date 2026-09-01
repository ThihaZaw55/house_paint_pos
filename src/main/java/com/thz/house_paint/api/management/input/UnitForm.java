package com.thz.house_paint.api.management.input;

import com.thz.house_paint.model.entity.Unit;

import jakarta.validation.constraints.NotBlank;

public record UnitForm(
		@NotBlank(message = "Unit is not blank.") 
		String unitName) {

	public Unit toEntity() {
		var entity = new Unit();
		entity.setUnitName(unitName);
		return entity;
	}
}
