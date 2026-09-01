package com.thz.house_paint.api.management.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thz.house_paint.api.management.input.ItemForm;
import com.thz.house_paint.api.management.output.ItemDTO;
import com.thz.house_paint.model.entity.Item;
import com.thz.house_paint.model.repository.ItemRepo;
import com.thz.house_paint.utils.exceptions.custom.DuplicateResourceException;
import com.thz.house_paint.utils.exceptions.custom.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepo itemRepo;
    
    @Transactional
    public ItemDTO createItem(ItemForm itemForm) {
        if(itemRepo.existsByItemName(itemForm.itemName())) {
            throw new DuplicateResourceException("Item already exists: %s".formatted(itemForm.itemName()));
        }
        Item item = itemForm.toEntity();
        return ItemDTO.toDTO(itemRepo.save(item));
    }
    
    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItem(){
        return itemRepo.findByOrderByItemIdAsc()
                .stream()
                .map(ItemDTO::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ItemDTO getItemById(int id) {
        return itemRepo.findById(id)
                .map(ItemDTO::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + id));
    }
    
    @Transactional
    public ItemDTO updateItem(int id, ItemForm updateItem) {
        // 👈 ID ရှာမတွေ့ပါက ResourceNotFoundException ပစ်ပေးရပါမည်
        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + id));
        
        item.setItemName(updateItem.itemName());
        return ItemDTO.toDTO(itemRepo.save(item));
    }
    
    @Transactional
    public void deleteItem(int id) {
        if(!itemRepo.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Item not found with ID: " + id);
        }
        itemRepo.deleteById(id);
    }
}