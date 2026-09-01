package com.thz.house_paint.utils.exceptions;

import com.thz.house_paint.api.management.output.ProductDTO;
import lombok.Getter;

@Getter
public class ProductAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final ProductDTO existingProduct;

    public ProductAlreadyExistsException(String message, ProductDTO existingProduct) {
        super(message);
        this.existingProduct = existingProduct;
    }
}