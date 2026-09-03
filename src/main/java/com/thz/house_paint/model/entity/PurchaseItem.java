package com.thz.house_paint.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "purchase_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "purchase_item_id")
    private UUID purchaseItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    @JsonIgnoreProperties("items")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "buy_quantity", nullable = false)
    private int buyQuantity;

    @Column(name = "unit_cost_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCostPrice;

    @Column(name = "unit_sales_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitSalesPrice;

}