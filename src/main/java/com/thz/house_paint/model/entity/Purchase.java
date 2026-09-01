package com.thz.house_paint.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "purchase_id")
    private UUID purchaseId;
    
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "purchase_update_date", nullable = false)
    private LocalDate purchaseUpdateDate;
    
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> items = new ArrayList<>();

	public void addPurchaseItem(PurchaseItem purchaseItem) {
		if (this.items == null) {
	        this.items = new ArrayList<>();
	    }
	    this.items.add(purchaseItem);
	    purchaseItem.setPurchase(this); // PurchaseItem ဘက်သို့ပါ Purchase Object ချိတ်ပေးခြင်း
	}

}
