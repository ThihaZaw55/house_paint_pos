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
import com.thz.house_paint.model.repository.PurchaseItemRepo;
import com.thz.house_paint.model.repository.PurchaseRepo;
import com.thz.house_paint.utils.exceptions.custom.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {
	
	private final ProductRepo productRepo;
	private final PurchaseRepo purchaseRepo;
	private final PurchaseItemRepo purchaseItemRepo;
	
	@Transactional
	public PurchaseDTO createPurchase(PurchaseForm form) {
        Purchase purchase = new Purchase();
        purchase.setSupplierName(form.supplierName());
        purchase.setPurchaseDate(form.purchaseDate());
        purchase.setPurchaseUpdateDate(LocalDate.now());

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (PurchaseItemForm itemForm : form.items()) {
            Product product = productRepo.findById(itemForm.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found id: " + itemForm.productId()));

            PurchaseItem item = new PurchaseItem();
            item.setProduct(product);
            item.setBuyQuantity(itemForm.buyQuantity());
            item.setUnitCostPrice(itemForm.unitCostPrice());
            item.setUnitSalesPrice(itemForm.unitSalesPrice());

            updateProductStockAndCost(
                    product, 
                    itemForm.buyQuantity(), 
                    itemForm.unitCostPrice(), 
                    itemForm.unitSalesPrice(),
                    true
            );

            BigDecimal subTotal = itemForm.unitCostPrice().multiply(BigDecimal.valueOf(itemForm.buyQuantity()));
            calculatedTotal = calculatedTotal.add(subTotal);

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
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with ID: " + id));
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
            product.setSalePrice(newSalesPrice);
        } else {
        	 // Weighted Average Cost Formula: ((OldStock * OldCost) + (BuyQty * NewCost)) / NewStock
            totalOldVal = oldCost.multiply(BigDecimal.valueOf(oldStock));
            totalNewVal = newCost.multiply(BigDecimal.valueOf(buyQty));
            weightedCost = totalOldVal.add(totalNewVal).divide(BigDecimal.valueOf(updatedStock), 2, RoundingMode.HALF_UP);
            
            product.setCostPrice(weightedCost);
            product.setSalePrice(newSalesPrice); // ရောင်းစျေးအသစ်အတိုင်း Update ပြုလုပ်ခြင်း
        }
        productRepo.save(product);
    }

    @Transactional
    public void deletePurchase(UUID id) {
        if(!purchaseRepo.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Purchase not found with ID: " + id);
        }

		purchaseItemRepo.deleteById(id);
        purchaseRepo.deleteById(id);
    }
}
