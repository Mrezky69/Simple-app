package com.project.shop.controllers;


import com.project.shop.dto.*;
import com.project.shop.service.OrderService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/tambah")
    public OrderResponseDTO addCustomer(@RequestBody OrderRequestDTO request) {
        return orderService.addOrder(request);
    }

    @GetMapping("/list")
    public List<OrderResponseDTO> getAllCustomers() {
        return orderService.listOrders();
    }

    @GetMapping("/detail/{id}")
    public OrderResponseDTO getCustomerDetail(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/update/{id}")
    public OrderResponseDTO updateCustomer(@PathVariable Long id, @RequestBody OrderRequestDTO request) {
        return orderService.updateOrder(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("delete berhasil");
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateOrderReport() {
        try {
            byte[] pdfBytes = orderService.generateOrderReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "order_report.pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
