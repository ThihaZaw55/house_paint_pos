package com.thz.house_paint.api.management;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.thz.house_paint.api.management.input.ProductForm;
import com.thz.house_paint.api.management.input.UpdateProductForm;
import com.thz.house_paint.api.management.output.ApiResponse;
import com.thz.house_paint.api.management.output.ProductDTO;
import com.thz.house_paint.api.management.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@EnableJpaAuditing
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDTO>> saveProduct(
            @Valid @RequestPart("data") ProductForm form,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        ProductDTO created = productService.createProduct(form, imageFile);
        return  ResponseEntity.status(HttpStatus.CREATED)
        		.body(new ApiResponse<ProductDTO>(true, "Product created successfully.", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
    	List<ProductDTO> dto = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Items retrieved successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    // @PostMapping မှ @PutMapping သို့ ပြောင်းလဲထားပြီး @RequestBody ကို @RequestPart သို့ ပြင်ဆင်ထားသည်
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable int id, 
            @Valid @RequestPart("data") UpdateProductForm updateProduct, 
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        ProductDTO updated = productService.updateProduct(id, updateProduct, imageFile);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponse<ProductDTO>(true, "Update Successfully", updated) );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}