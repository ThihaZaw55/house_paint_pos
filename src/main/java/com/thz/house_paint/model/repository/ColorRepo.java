package com.thz.house_paint.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Colour;

public interface ColorRepo extends JpaRepository<Colour, Integer>{

}
