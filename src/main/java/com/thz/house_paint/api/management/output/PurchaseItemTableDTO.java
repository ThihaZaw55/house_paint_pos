package com.thz.house_paint.api.management.output;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.thz.house_paint.model.entity.Purchase;
import com.thz.house_paint.model.entity.PurchaseItem;
import com.thz.house_paint.model.entity.PurchaseItem_;
import com.thz.house_paint.model.entity.Purchase_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

public record PurchaseItemTableDTO(
	    LocalDate purchaseDate,
	    String itemName,
	    String unitName,
	    int buyQuantity,
	    BigDecimal costPrice,
	    BigDecimal salePrice
	) {
	public static void select(CriteriaBuilder cb, CriteriaQuery<PurchaseItemTableDTO> cq, Root<PurchaseItem> root) {
        Join<PurchaseItem, Purchase> purchase = root.join("purchase");
        var product = root.join("product");
        var item = product.join("item");
        var unit = product.join("unit");

        cq.select(cb.construct(
            PurchaseItemTableDTO.class,
            purchase.get(Purchase_.purchaseDate),
            item.get("itemName"), // item ရဲ့ name field ကို ယူရန်
            unit.get("unitName"), // unit ရဲ့ name field ကို ယူရန်
            root.get(PurchaseItem_.buyQuantity),
            root.get(PurchaseItem_.costPrice),
            root.get(PurchaseItem_.salePrice)
        ));
    }
	}
