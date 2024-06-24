package com.project.shop.controllers;

import com.project.shop.dto.*;
import com.project.shop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public List<CustomerReponseDTO> getAllCustomers() {
        return customerService.listCustomer();
    }

    @GetMapping("/detail/{id}")
    public CustomerReponseDTO getCustomerDetail(@PathVariable Long id) {
        return customerService.getDetailCustomer(id);
    }

    @PostMapping("/tambah")
    public CustomerReponseDTO addCustomer(@RequestBody CustomerRequestDTO request) {
        return customerService.addCustomer(request);
    }

    @PutMapping("/update/{id}")
    public CustomerReponseDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO request) {
        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("delete berhasil");
    }
}
