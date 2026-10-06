package com.thz.house_paint.api.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thz.house_paint.api.management.input.ProductForm;
import com.thz.house_paint.api.management.input.UpdateProductForm;
import com.thz.house_paint.api.management.output.ProductDTO;
import com.thz.house_paint.model.entity.Colour;
import com.thz.house_paint.model.entity.Item;
import com.thz.house_paint.model.entity.Product;
import com.thz.house_paint.model.entity.Unit;
import com.thz.house_paint.model.repository.ColorRepo;
import com.thz.house_paint.model.repository.ItemRepo;
import com.thz.house_paint.model.repository.ProductRepo;
import com.thz.house_paint.model.repository.UnitRepo;
import com.thz.house_paint.utils.exceptions.ProductAlreadyExistsException;
import com.thz.house_paint.utils.exceptions.custom.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepo productRepo;
    private final ItemRepo itemRepo;
    private final UnitRepo unitRepo;
    private final ColorRepo colorRepo;
    private final FileStorageService fileStorageService;
    
    // ==========================================
    // 1. CREATE PRODUCT (အသစ်ဆောက်ခြင်း)
    // ==========================================
    public ProductDTO createProduct(ProductForm form, MultipartFile imageFile) {
        if (form == null) return null;

        // 1. Check if product already exists in DB
        Optional<Product> existingProductOpt = productRepo.findByItem_ItemIdAndUnit_UnitIdAndColourIsNull(form.itemId(), form.unitId());

        if (existingProductOpt.isPresent()) {
            ProductDTO existingDTO = ProductDTO.toDTO(existingProductOpt.get());
            throw new ProductAlreadyExistsException(
                "Product already exists in the system.", 
                existingDTO
            );
        }

        // 2. Continue with normal creation if not exists
        Item item = fetchItem(form.itemId());
        Unit unit = fetchUnit(form.unitId());
        Colour colour = fetchColor(form.colourId());

        String imageUrl = fileStorageService.saveFile(imageFile);
        Product product = form.toEntity(item, unit, colour, imageUrl);
        Product savedProduct = productRepo.save(product);

        return ProductDTO.toDTO(savedProduct);
    }

    // ==========================================
    // 2. UPDATE PRODUCT BY ID (အဟောင်းကို ပြင်ဆင်ခြင်း)
    // ==========================================
    public ProductDTO updateProduct(int id, UpdateProductForm updateProduct, MultipartFile imageFile) {
        if (updateProduct == null) return null;

        if(!productRepo.existsById(id)) {
        	throw new ResourceNotFoundException("There is no product with id " + id);
        }
        
     // 1. Check if product already exists in DB
//        Optional<Product> existingProduct = (form.colourId() == null)
//                ? productRepo.findByItem_ItemIdAndUnit_UnitIdAndColourIsNull(form.itemId(), form.unitId())
//                : productRepo.findByItem_ItemIdAndUnit_UnitIdAndColour_ColourId(form.itemId(), form.unitId(), form.colourId());

        
        // Fetch Existing Product
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID: " + id));

//        // Fetch Relational Entities
//        Item item = fetchItem(form.itemId());
//        Unit unit = fetchUnit(form.unitId());
        Colour colour = fetchColor(updateProduct.colourId());

        // Update Entity Fields
//        existingProduct.setItem(item);
//        existingProduct.setUnit(unit);
        existingProduct.setColour(colour);
        existingProduct.setCostPrice(updateProduct.costPrice());
        existingProduct.setSalePrice(updateProduct.salePrice());
        existingProduct.setStockQuantity(updateProduct.stockQuantity());

        // Handle Image Upload (Update ONLY if a new image file is provided)
        MultipartFile newImageFile = imageFile;
        if (newImageFile != null && !newImageFile.isEmpty()) {
        	if (existingProduct.getImageUrl() != null) {
                fileStorageService.deleteFile(existingProduct.getImageUrl());
            }
            String newImageUrl = fileStorageService.saveFile(newImageFile);
            existingProduct.setImageUrl(newImageUrl);
        }

        Product updatedProduct = productRepo.save(existingProduct);
        return ProductDTO.toDTO(updatedProduct);
    }

    // ==========================================
    // 3. READ METHODS
    // ==========================================
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(ProductDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(int id) {
//        return safeCall(productRepo.findById(id).map(ProductDTO::toDTO))
//                .apply("product").apply("id").apply(id);
    	return productRepo.findById(id)
    			.map(ProductDTO::toDTO)
    			.orElseThrow(() -> new ResourceNotFoundException("There is no product with id " + id));
    }

    // ==========================================
    // 5. INVENTORY STOCK & COST UPDATE (Weighted Average)
    // ==========================================
    public void updateProductStockAndCost(Product product, int buyQty, BigDecimal unitCostPrice, BigDecimal unitSalesPrice) {
        int currentStock = product.getStockQuantity();
        BigDecimal currentCost = product.getCostPrice();

        BigDecimal currentTotalValue = currentCost.multiply(BigDecimal.valueOf(currentStock));
        BigDecimal newPurchaseValue = unitCostPrice.multiply(BigDecimal.valueOf(buyQty));

        int newStock = currentStock + buyQty;

        BigDecimal newAverageCost = BigDecimal.ZERO;
        if (newStock > 0) {
            newAverageCost = currentTotalValue.add(newPurchaseValue)
                    .divide(BigDecimal.valueOf(newStock), 2, RoundingMode.HALF_UP);
        }

        product.setStockQuantity(newStock);
        product.setCostPrice(newAverageCost);

        if (unitSalesPrice != null && unitSalesPrice.compareTo(BigDecimal.ZERO) > 0) {
            product.setSalePrice(unitSalesPrice);
        }

        productRepo.save(product);
    }

    // ==========================================
    // 4. DELETE METHOD
    // ==========================================
    public void deleteProduct(int id) {
    	if(!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Product not found with ID: " + id);
        }
    	
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));

        if (product.getImageUrl() != null) {
            fileStorageService.deleteFile(product.getImageUrl());
        }

        productRepo.delete(product);
    }
    
    // ==========================================
    // HELPER METHODS (DRY Principle)
    // ==========================================
    private Item fetchItem(Integer itemId) {
        return itemRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));
    }

    private Unit fetchUnit(Integer unitId) {
        return unitRepo.findById(unitId)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with ID: " + unitId));
    }

    private Colour fetchColor(Integer colorId) {
        if (colorId == null) return null;
        return colorRepo.findById(colorId)
                .orElseThrow(() -> new ResourceNotFoundException("Color not found with ID: " + colorId));
    }
}