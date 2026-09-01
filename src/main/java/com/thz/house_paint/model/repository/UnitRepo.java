package com.thz.house_paint.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Unit;

public interface UnitRepo extends JpaRepository<Unit, Integer> {

	boolean existsByUnitName(String unitName);
	
	List<Unit> findByOrderByUnitIdAsc();
	
}
