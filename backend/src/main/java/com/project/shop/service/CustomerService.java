package com.project.shop.service;

import com.project.shop.dto.*;
import com.project.shop.models.Customers;
import com.project.shop.models.Items;
import com.project.shop.models.Orders;
import com.project.shop.repositories.CustomerRepository;
import com.project.shop.repositories.ItemRepository;
import com.project.shop.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ImageService imageService;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Value("${file.upload-dir}")
    public String uploadDir;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ImageService imageService, OrderRepository orderRepository, ItemRepository itemRepository){
        this.customerRepository = customerRepository;
        this.imageService = imageService;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    private CustomerReponseDTO response(Customers req){
        return CustomerReponseDTO.builder()
                .id(req.getId())
                .name(req.getName())
                .address(req.getAddress())
                .code(req.getCode())
                .phone(req.getPhone())
                .isActive(req.getIsActive())
                .lastOrder(req.getLastOrder())
                .pic(imageService.convertImagetoBase64(req))
                .build();
    }

    private Customers newData(Customers newCustomer, CustomerRequestDTO req){
        newCustomer.setName(req.getName());
        newCustomer.setAddress(req.getAddress());
        newCustomer.setPhone(req.getPhone());
        newCustomer.setCode(req.getCode());
        newCustomer.setIsActive(req.getIsActive());
        if (req.getPic() != null && !req.getPic().isEmpty()) {
            String filePath = imageService.saveBase64Image(req.getPic(), uploadDir);
            if(newCustomer.getId() != null){
                imageService.deleteImage(newCustomer.getPic());
            }
            newCustomer.setPic(filePath);
        }
        customerRepository.save(newCustomer);
        return newCustomer;
    }

    public List<CustomerReponseDTO> listCustomer(){
        return customerRepository.findAll().stream().map(this::response).collect(Collectors.toList());
    }

    public CustomerReponseDTO getDetailCustomer(Long id){
        Customers existingCustomer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Customer Not Found"));
        return response(existingCustomer);
    }

    public void deleteCustomer(Long id) {
        Customers customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer data not found"));
        customer.setIsActive(0);
        customerRepository.save(customer);
    }

    public CustomerReponseDTO addCustomer(CustomerRequestDTO req){
        Customers newCustomer = new Customers();
        return response(newData(newCustomer, req));
    }

    public CustomerReponseDTO updateCustomer(Long id, CustomerRequestDTO req){
        Customers existingCustomer = customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Customer Not Found"));
        return response(newData(existingCustomer, req));
    }
}
