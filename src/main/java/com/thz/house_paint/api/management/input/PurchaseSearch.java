package com.thz.house_paint.api.management.input;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.thz.house_paint.model.entity.Purchase;
import com.thz.house_paint.model.entity.PurchaseItem;
import com.thz.house_paint.model.entity.Purchase_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public record PurchaseSearch(UUID purchaseId, String supplierName, LocalDate purchaseDate) {

	public List<Predicate> where(CriteriaBuilder cb, Root<PurchaseItem> root){
		var params = new ArrayList<Predicate>();
		
		Join<PurchaseItem, Purchase> purchaseJoin = root.join("purchase");
		
		if(this.purchaseId != null) {
			params.add(cb.equal(purchaseJoin.get(Purchase_.purchaseId), this.purchaseId));
		}
		
		if(StringUtils.hasLength(this.supplierName)) {
			var supplierName = this.supplierName.toLowerCase().concat("%");
			params.add(cb.like(cb.lower(purchaseJoin.get(Purchase_.supplierName)), supplierName));
		}
		
		if(this.purchaseDate != null) {
			params.add(cb.equal(purchaseJoin.get(Purchase_.purchaseDate), this.purchaseDate));
		}
		
		return params;
	}
}