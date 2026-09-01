package com.thz.house_paint.api.management.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.thz.house_paint.api.management.input.PurchaseForm;
import com.thz.house_paint.api.management.input.PurchaseItemForm;
import com.thz.house_paint.api.management.output.PurchaseDTO;
import com.thz.house_paint.model.entity.Product;
import com.thz.house_paint.model.entity.Purchase;
import com.thz.house_paint.model.entity.PurchaseItem;
import com.thz.house_paint.model.repository.ProductRepo;
import com.thz.house_paint.model.repository.PurchaseItemRepo;
import com.thz.house_paint.model.repository.PurchaseRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {
	
	private final ProductRepo productRepo;
	private final ProductService productService;
	private final PurchaseRepo purchaseRepo;
	private final PurchaseItemRepo purchaseItemRepo;
	
	@Transactional
	public PurchaseDTO createPurchase(List<PurchaseItemForm> purchaseItemForms, PurchaseForm purchaseForm) {

	    // 1. Step 1: Parent Purchase ကို DB ထဲ အရင် Save ပြီး purchase_id ယူခြင်း
	    Purchase purchase = new Purchase();
	    purchase.setSupplierName(purchaseForm.supplierName());
	    purchase.setPurchaseDate(LocalDate.now());
	    purchase.setPurchaseUpdateDate(LocalDate.now());
	    purchase.setTotalAmount(BigDecimal.ZERO);
	    
	    Purchase savedPurchase = purchaseRepo.save(purchase); // 👈 ID စတင် ရရှိသွားပါပြီ

	    BigDecimal grandTotal = BigDecimal.ZERO;
	    List<PurchaseItem> itemsToSave = new ArrayList<>();

	    // 2. Step 2 & 3: PurchaseItem များကို Loop ပတ်၍ မွမ်းမံခြင်း
	    for (PurchaseItemForm itemForm : purchaseItemForms) {
	        Product product = productRepo.findById(itemForm.productId())
	                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID: " + itemForm.productId()));

	        // Item Object ဆောက်ခြင်း
	        PurchaseItem item = new PurchaseItem();
	        item.setPurchase(savedPurchase); // 🌟 ဦးစွာ ရရှိထားသော Parent Purchase ကို ချိတ်ပေးရပါမည်
	        item.setProduct(product);
	        item.setBuyQuantity(itemForm.buyQuantity());
	        item.setUnitCostPrice(itemForm.unitCostPrice());
	        item.setUnitSalesPrice(itemForm.unitSalesPrice());

	        itemsToSave.add(item);

	        // Grand Total Amount တွက်ချက်ခြင်း
	        BigDecimal itemTotal = itemForm.unitCostPrice().multiply(BigDecimal.valueOf(itemForm.buyQuantity()));
	        grandTotal = grandTotal.add(itemTotal);

	        // Product Stock ပေါင်းပေးခြင်း နှင့် Cost Price Update လုပ်ခြင်း
	        productService.updateProductStockAndCost(
	                product, 
	                itemForm.buyQuantity(), 
	                itemForm.unitCostPrice(), 
	                itemForm.unitSalesPrice()
	        );
	    }

	    // 3. Step 4: PurchaseItem များကို တစ်ပြိုင်နက် Save လုပ်ခြင်း
	    purchaseItemRepo.saveAll(itemsToSave);

	    // 4. Step 5: Purchase ၏ Total Amount ကို Update ပြန်လုပ်ပြီး Response ပြန်ပေးခြင်း
	    savedPurchase.setTotalAmount(grandTotal);
	    Purchase finalSavedPurchase = purchaseRepo.save(savedPurchase);

	    return PurchaseDTO.toDTO(finalSavedPurchase);
	}

}
