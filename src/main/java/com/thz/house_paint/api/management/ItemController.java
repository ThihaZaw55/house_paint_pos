package com.thz.house_paint.api.management;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thz.house_paint.api.management.input.ItemForm;
import com.thz.house_paint.api.management.output.ApiResponse;
import com.thz.house_paint.api.management.output.ItemDTO;
import com.thz.house_paint.api.management.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ItemDTO>> createItem(@Valid @RequestBody ItemForm form) {
        ItemDTO dto = itemService.createItem(form);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Item created successfully", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getAllItems() {
        List<ItemDTO> Items = itemService.getAllItem();
        return ResponseEntity.ok(new ApiResponse<>(true, "Items retrieved successfully", Items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDTO>> getItemById(@PathVariable int id) {
        ItemDTO dto = itemService.getItemById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item retrieved successfully", dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDTO>> updateItem(
            @PathVariable int id, 
            @Valid @RequestBody ItemForm updatedItem) {
        ItemDTO dto = itemService.updateItem(id, updatedItem);
        return ResponseEntity.ok(new ApiResponse<>(true, "Successfully updated Item", dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable int id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Item deleted successfully", null)
        );
    }
}