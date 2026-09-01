package com.thz.house_paint.api.management.output;

import com.thz.house_paint.model.entity.Unit;

public record UnitDTO(int unitId, String unitName) {

	public static UnitDTO toDto(Unit entity) {
		if(entity == null ) return null;
		var dto = new UnitDTO(entity.getUnitId(), entity.getUnitName());
		return dto;
	}
	
}
