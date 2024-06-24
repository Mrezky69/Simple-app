package com.project.shop.service;

import com.project.shop.dto.*;
import com.project.shop.models.Items;
import com.project.shop.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    private ItemResponseDTO response(Items req){
        return ItemResponseDTO.builder()
                .id(req.getId())
                .itemsName(req.getItemsName())
                .price(req.getPrice())
                .stock(req.getStock())
                .itemsCode(req.getItemsCode())
                .isAvailable(req.getIsAvailable())
                .lastReStock(req.getLastReStock())
                .build();
    }

    private Items request(Items items, ItemRequestDTO req){
        items.setItemsCode(req.getItemsCode());
        items.setItemsName(req.getItemsName());
        items.setPrice(req.getPrice());
        items.setStock(req.getStock());
        items.setIsAvailable(req.getIsAvailable());
        if(req.getStock() > items.getStock()){
            items.setLastReStock(new Date());
        }
        itemRepository.save(items);
        return items;
    }

    public List<ItemResponseDTO> listItems(){
        return itemRepository.findAll().stream().map(this::response).collect(Collectors.toList());
    }

    public ItemResponseDTO getDetailItem(Long id){
        Items existingItem = itemRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Item Not Found"));
        return response(existingItem);
    }

    public ItemResponseDTO addItem(ItemRequestDTO req){
        Items newItem = new Items();
        return response(request(newItem, req));
    }

    public ItemResponseDTO updateItem(Long id, ItemRequestDTO req){
        Items existingItem = itemRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Item Not Found"));
        return response(request(existingItem, req));
    }

    public void deleteItem(Long id){
        Items existingItem = itemRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Item Not Found"));
        existingItem.setIsAvailable(0);
        itemRepository.save(existingItem);
    }
}
