package com.thz.house_paint.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Sale;

public interface SaleRepo extends JpaRepository<Sale, UUID>{

}
