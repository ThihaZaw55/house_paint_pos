package com.thz.house_paint.api.management;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thz.house_paint.api.management.input.ProductForm;
import com.thz.house_paint.api.management.input.PurchaseForm;
import com.thz.house_paint.api.management.output.PurchaseDTO;
import com.thz.house_paint.api.management.service.PurchaseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/purchase")
@RequiredArgsConstructor
public class PurchaseController {

	private final PurchaseService purchaseService;
 
	@PostMapping
	public ResponseEntity<PurchaseDTO> createPurchase(@Valid @RequestBody PurchaseForm form) {
		return new ResponseEntity<PurchaseDTO>(purchaseService.createPurchase(form), HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable UUID id) {
		PurchaseDTO purchaseDTO = purchaseService.getPurchaseById(id);
        if (purchaseDTO == null) {
            return ResponseEntity.notFound().build();
        }
		return ResponseEntity.ok(purchaseDTO);
	}
	
	@GetMapping
	public ResponseEntity<List<PurchaseDTO>> getAllPurchase() {
		return ResponseEntity.ok(purchaseService.getAllPurchases());
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<PurchaseDTO> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductForm form) {
		PurchaseDTO updated = purchaseService.updatePurchase(id, null);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
	
	
}
