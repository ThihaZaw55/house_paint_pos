package com.thz.house_paint.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thz.house_paint.model.entity.Purchase;

public interface PurchaseRepo extends JpaRepository<Purchase, UUID>{

}
