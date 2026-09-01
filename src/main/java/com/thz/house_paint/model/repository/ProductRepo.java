package com.thz.house_paint.model.repository;

import com.thz.house_paint.model.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	
    Optional<Product> findByItem_ItemIdAndUnit_UnitIdAndColour_ColourId(Integer itemId, Integer unitId, Integer colourId);

    Optional<Product> findByItem_ItemIdAndUnit_UnitIdAndColourIsNull(Integer itemId, Integer unitId);
    
}