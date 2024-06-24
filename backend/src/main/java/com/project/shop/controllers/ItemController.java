package com.project.shop.controllers;


import com.project.shop.dto.*;
import com.project.shop.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemsService itemsService;

    @Autowired
    public ItemController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping("/list")
    public List<ItemResponseDTO> getAllCustomers() {
        return itemsService.listItems();
    }

    @GetMapping("/detail/{id}")
    public ItemResponseDTO getCustomerDetail(@PathVariable Long id) {
        return itemsService.getDetailItem(id);
    }

    @PostMapping("/tambah")
    public ItemResponseDTO addCustomer(@RequestBody ItemRequestDTO request) {
        return itemsService.addItem(request);
    }

    @PutMapping("/update/{id}")
    public ItemResponseDTO updateCustomer(@PathVariable Long id, @RequestBody ItemRequestDTO request) {
        return itemsService.updateItem(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        itemsService.deleteItem(id);
        return ResponseEntity.ok("delete berhasil");
    }
}
