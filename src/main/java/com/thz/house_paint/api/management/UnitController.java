package com.thz.house_paint.api.management;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thz.house_paint.api.management.input.UnitForm;
import com.thz.house_paint.api.management.output.ApiResponse;
import com.thz.house_paint.api.management.output.UnitDTO;
import com.thz.house_paint.api.management.service.UnitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {
    
    private final UnitService unitService;

    @PostMapping
    public ResponseEntity<ApiResponse<UnitDTO>> createUnit(@Valid @RequestBody UnitForm form) {
        UnitDTO dto = unitService.createUnit(form);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Unit created successfully", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnitDTO>>> getAllUnits() {
        List<UnitDTO> units = unitService.getAllUnits();
        return ResponseEntity.ok(new ApiResponse<>(true, "Units retrieved successfully", units));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitDTO>> getUnitById(@PathVariable int id) {
        UnitDTO dto = unitService.getUnitById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Unit retrieved successfully", dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitDTO>> updateUnit(
            @PathVariable int id, 
            @Valid @RequestBody UnitForm updatedUnit) {
        UnitDTO dto = unitService.updateUnit(id, updatedUnit);
        return ResponseEntity.ok(new ApiResponse<>(true, "Successfully updated unit", dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUnit(@PathVariable int id) {
        unitService.deleteUnit(id);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Unit deleted successfully", null)
        );
    }
}