package com.thz.house_paint.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
