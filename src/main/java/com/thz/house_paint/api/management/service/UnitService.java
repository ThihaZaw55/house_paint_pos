package com.thz.house_paint.api.management.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.thz.house_paint.api.management.input.UnitForm;
import com.thz.house_paint.api.management.output.UnitDTO;
import com.thz.house_paint.model.entity.Unit;
import com.thz.house_paint.model.repository.UnitRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepo unitRepo;
    
    @Transactional
    public UnitDTO createUnit(UnitForm unitForm) {
        if (unitRepo.existsByUnitName(unitForm.unitName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unit name already exists: " + unitForm.unitName());
        }
        Unit unit = unitForm.toEntity();
        return UnitDTO.toDto(unitRepo.save(unit));
    }

    @Transactional(readOnly = true)
    public List<UnitDTO> getAllUnits() {
        return unitRepo.findByOrderByUnitIdAsc()
                .stream()
                .map(UnitDTO::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UnitDTO getUnitById(int id) {
        return unitRepo.findById(id)
                .map(UnitDTO::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit not found with ID: " + id));
    }

    @Transactional
    public UnitDTO updateUnit(int id, UnitForm updatedUnit) {
        Unit unit = unitRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit not found with ID: " + id));
        
        unit.setUnitName(updatedUnit.unitName());
        return UnitDTO.toDto(unitRepo.save(unit));
    }

    @Transactional
    public void deleteUnit(int id) {
        if (!unitRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete. Unit not found with ID: " + id);
        }
        unitRepo.deleteById(id);
    }
}