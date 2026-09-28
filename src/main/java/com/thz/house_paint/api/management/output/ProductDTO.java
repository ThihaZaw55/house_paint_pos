package com.thz.house_paint.api.management.output;

import java.math.BigDecimal;
import com.thz.house_paint.model.entity.Product;

public record ProductDTO(
    int productId,
    int itemId,
    String itemName,
    int unitId,
    String unitName,
    String colourCode,
    String colourName,
    Integer stockQuantity,
    BigDecimal costPrice,
    BigDecimal salesPrice,
    String imagePath
) {

    public static ProductDTO toDTO(Product entity) {
        if (entity == null) return null;
        
        int itemId = (entity.getItem() != null) ? entity.getItem().getItemId() : 0;
        String itemName = (entity.getItem() != null) ? entity.getItem().getItemName() : null;

        int unitId = (entity.getUnit() != null) ? entity.getUnit().getUnitId() : 0;
        String unitName = (entity.getUnit() != null) ? entity.getUnit().getUnitName() : null;

        String colourCode = (entity.getColour() != null) ? entity.getColour().getColourCode() : null;
        String colourName = (entity.getColour() != null) ? entity.getColour().getColourName() : null;

        return new ProductDTO(
            entity.getProductId(),
            itemId,
            itemName,
            unitId,
            unitName,
            colourCode,
            colourName,
            entity.getStockQuantity(),
            entity.getCostPrice(),
            entity.getSalesPrice(),
            entity.getImageUrl()
        );
    }
}