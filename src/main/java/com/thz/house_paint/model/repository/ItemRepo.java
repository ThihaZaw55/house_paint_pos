package com.thz.house_paint.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Item;

public interface ItemRepo extends JpaRepository<Item, Integer>{

	boolean existsByItemName(String itemName);
	
	List<Item> findByOrderByItemIdAsc();
}
