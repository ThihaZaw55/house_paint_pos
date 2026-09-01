package com.thz.house_paint.api.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thz.house_paint.api.management.input.PurchaseForm;
import com.thz.house_paint.api.management.input.PurchaseItemForm;
import com.thz.house_paint.api.management.output.PurchaseDTO;
import com.thz.house_paint.model.entity.Product;
import com.thz.house_paint.model.entity.Purchase;
import com.thz.house_paint.model.entity.PurchaseItem;
import com.thz.house_paint.model.repository.ProductRepo;
import com.thz.house_paint.model.repository.PurchaseRepo;
import com.thz.house_paint.utils.exceptions.custom.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {
	
	private final PurchaseRepo purchaseRepo;
	private final ProductRepo productRepo;
	
	@Transactional
	public PurchaseDTO createPurchase(PurchaseForm form) {
        // 1. Parent Entity (Purchase) ဆောက်ခြင်း
        Purchase purchase = new Purchase();
        purchase.setSupplierName(form.supplierName());
        purchase.setPurchaseDate(form.purchaseDate());
        purchase.setPurchaseUpdateDate(LocalDate.now());

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        // 2. Child Items Array ကို Loop ပတ်၍ Entity ပြောင်းခြင်း
        for (PurchaseItemForm itemForm : form.items()) {
            Product product = productRepo.findById(itemForm.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found id: " + itemForm.productId()));

            // PurchaseItem တွင် ယခု ဝယ်ယူသည့် အရေအတွက်အတိုင်းသာ သတ်မှတ်ပါ
            PurchaseItem item = new PurchaseItem();
            item.setProduct(product);
            item.setBuyQuantity(itemForm.buyQuantity()); // 👈 Fix: Product Stock သွားမပေါင်းရပါ
            item.setUnitCostPrice(itemForm.unitCostPrice());
            item.setUnitSalesPrice(itemForm.unitSalesPrice());

            // 3. Product ၏ Stock, CostPrice (Weighted Average) နှင့် SalesPrice များကို Update လုပ်ခြင်း
            updateProductStockAndCost(
                    product, 
                    itemForm.buyQuantity(), 
                    itemForm.unitCostPrice(), 
                    itemForm.unitSalesPrice(),
                    true
            );

            // Subtotal တွက်ချက်ခြင်း
            BigDecimal subTotal = itemForm.unitCostPrice().multiply(BigDecimal.valueOf(itemForm.buyQuantity()));
            calculatedTotal = calculatedTotal.add(subTotal);

            // Parent နှင့် Child ချိတ်ဆက်ခြင်း
            purchase.addPurchaseItem(item); 
        }

        purchase.setTotalAmount(calculatedTotal);

        // CascadeType.ALL ကြောင့် Purchase ကို save လုပ်သည်နှင့် PurchaseItem များပါ DB ထဲ အလိုအလျောက် ဝင်သွားပါမည်
        Purchase savedPurchase = purchaseRepo.save(purchase);
        
        return PurchaseDTO.toDTO(savedPurchase);
    }
	
	// 2. READ BY ID
    @Transactional(readOnly = true)
    public PurchaseDTO getPurchaseById(UUID id) {
        return purchaseRepo.findById(id)
                .map(PurchaseDTO::toDTO)
                .orElse(null);
        
//        return safeCall(purchaseRepo.findById(id).map(this::toOutput))
//				.apply("Product").apply("id").apply(id);
    }

    // 3. READ ALL
    @Transactional(readOnly = true)
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepo.findAll().stream()
                .map(PurchaseDTO::toDTO)
                .toList();
    }

	public PurchaseDTO updatePurchase(UUID id, PurchaseForm form) {
		 if (form == null) return null;

	        // Fetch Existing Product
	        Purchase existingPurchase = purchaseRepo.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID: " + id));
	        
	        // Update Entity Fields
	        existingPurchase.setSupplierName(form.supplierName());
	        existingPurchase.setPurchaseUpdateDate(form.purchaseDate());
	        existingPurchase.setTotalAmount(form.totalCost());

	        Purchase updatedPurchase = purchaseRepo.save(existingPurchase);
	        return PurchaseDTO.toDTO(updatedPurchase);
	}
	
    // --- Helper Methods ---
    private void updateProductStockAndCost(Product product, int buyQty, BigDecimal newCost, BigDecimal newSalesPrice, boolean isOwn) {
        int oldStock = product.getStockQuantity();
        BigDecimal oldCost = product.getCostPrice();
        BigDecimal totalOldVal = BigDecimal.ZERO;
        BigDecimal totalNewVal = BigDecimal.ZERO;
        BigDecimal weightedCost = BigDecimal.ZERO;
        
        int updatedStock = oldStock + buyQty;
        product.setStockQuantity(updatedStock);
        if(isOwn) {
            product.setCostPrice(newCost);
            product.setSalesPrice(newSalesPrice);
        } else {
        	 // Weighted Average Cost Formula: ((OldStock * OldCost) + (BuyQty * NewCost)) / NewStock
            totalOldVal = oldCost.multiply(BigDecimal.valueOf(oldStock));
            totalNewVal = newCost.multiply(BigDecimal.valueOf(buyQty));
            weightedCost = totalOldVal.add(totalNewVal).divide(BigDecimal.valueOf(updatedStock), 2, RoundingMode.HALF_UP);
            
            product.setCostPrice(weightedCost);
            product.setSalesPrice(newSalesPrice); // ရောင်းစျေးအသစ်အတိုင်း Update ပြုလုပ်ခြင်း
        }
        productRepo.save(product);
    }

}
