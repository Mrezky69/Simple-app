package com.project.shop.service;

import java.util.*;
import com.project.shop.dto.*;
import com.project.shop.models.*;
import com.project.shop.repositories.*;
import net.sf.jasperreports.engine.*;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;

import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ItemRepository itemRepository){
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public OrderResponseDTO addOrder(OrderRequestDTO req){
        Customers customers = customerRepository.findById(req.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Customer Not Found"));
        if(customers.getIsActive().equals(0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Active");
        }
        Items items = itemRepository.findById(req.getItemId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Item Not Found"));

        if(items.getIsAvailable().equals(0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barang Not Found");
        }

        if(req.getQuantity() > items.getStock()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock Tidak Mencukupi");
        }
        Orders order = new Orders();
        order.setOrderCode(req.getOrderCode());
        order.setOrderDate(new Date());
        order.setTotalPrice(((long) items.getPrice() * req.getQuantity()));
        order.setCustomersId(customers);
        order.setItemsId(items);
        order.setQuantity(req.getQuantity());
        orderRepository.save(order);

        items.setStock(items.getStock() - req.getQuantity());
        if(req.getQuantity() == items.getStock()){
            items.setIsAvailable(0);
        }
        itemRepository.save(items);

        customers.setLastOrder(new Date());
        customerRepository.save(customers);

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .customer(new CustomerReponseDTO(order.getCustomersId().getId(), order.getCustomersId().getName(), order.getCustomersId().getAddress(), order.getCustomersId().getCode(), order.getCustomersId().getPhone(), order.getCustomersId().getIsActive(), order.getCustomersId().getLastOrder(), order.getCustomersId().getPic()))
                .item(new ItemResponseDTO(items.getId(), items.getItemsName(), items.getItemsCode(), items.getStock(), items.getPrice(), items.getIsAvailable(), items.getLastReStock()))
                .build();
    }

    public List<OrderResponseDTO> listOrders() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(order -> OrderResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .customer(new CustomerReponseDTO(order.getCustomersId().getId(), order.getCustomersId().getName(), order.getCustomersId().getAddress(), order.getCustomersId().getCode(), order.getCustomersId().getPhone(), order.getCustomersId().getIsActive(), order.getCustomersId().getLastOrder(), order.getCustomersId().getPic()))
                .item(new ItemResponseDTO(order.getItemsId().getId(), order.getItemsId().getItemsName(), order.getItemsId().getItemsCode(), order.getItemsId().getStock(), order.getItemsId().getPrice(), order.getItemsId().getIsAvailable(), order.getItemsId().getLastReStock()))
                .quantity(order.getQuantity())
                .build()).collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .customer(new CustomerReponseDTO(order.getCustomersId().getId(), order.getCustomersId().getName(), order.getCustomersId().getAddress(), order.getCustomersId().getCode(), order.getCustomersId().getPhone(), order.getCustomersId().getIsActive(), order.getCustomersId().getLastOrder(), order.getCustomersId().getPic()))
                .item(new ItemResponseDTO(order.getItemsId().getId(), order.getItemsId().getItemsName(), order.getItemsId().getItemsCode(), order.getItemsId().getStock(), order.getItemsId().getPrice(), order.getItemsId().getIsAvailable(), order.getItemsId().getLastReStock()))
                .quantity(order.getQuantity())
                .build();
    }

    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO req) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        Customers customers = customerRepository.findById(req.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Customer Not Found"));
        if(customers.getIsActive().equals(0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Active");
        }
        Items items = itemRepository.findById(req.getItemId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data Item Not Found"));
        if(items.getIsAvailable().equals(0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Barang Not Found");
        }

        if(req.getQuantity() > items.getStock()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock Tidak Mencukupi");
        }

        order.setOrderCode(req.getOrderCode());
        order.setOrderDate(new Date());
        order.setTotalPrice(((long) items.getPrice() * req.getQuantity()));
        order.setCustomersId(customers);
        order.setItemsId(items);
        order.setQuantity(req.getQuantity());
        orderRepository.save(order);

        Items itemLama = null;
        if (!req.getItemId().equals(req.getItemIdLama())) {
            itemLama = itemRepository.findById(req.getItemIdLama())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old item not found"));
        }

        if (itemLama != null) {
            itemLama.setStock(itemLama.getStock() + req.getQuantityLama());
            if (itemLama.getStock() > 0) {
                itemLama.setIsAvailable(1);
            }
            itemRepository.save(itemLama);
        }

        items.setStock(items.getStock() - req.getQuantity());
        if(req.getQuantity() == items.getStock()){
            items.setIsAvailable(0);
        }
        itemRepository.save(items);

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .customer(new CustomerReponseDTO(customers.getId(), customers.getName(), customers.getAddress(), customers.getCode(), customers.getPhone(), customers.getIsActive(), customers.getLastOrder(), customers.getPic()))
                .item(new ItemResponseDTO(items.getId(), items.getItemsName(), items.getItemsCode(), items.getStock(), items.getPrice(), items.getIsAvailable(), items.getLastReStock()))
                .build();
    }

    public void deleteOrder(Long id) {
        Orders order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        orderRepository.delete(order);
    }

    public byte[] generateOrderReport() throws JRException {
        List<Orders> orders = orderRepository.findAll();
        List<OrderResponseDTO> orderResponseDTOS = orders.stream().map(order -> OrderResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .customer(new CustomerReponseDTO(order.getCustomersId().getId(), order.getCustomersId().getName(), order.getCustomersId().getAddress(), order.getCustomersId().getCode(), order.getCustomersId().getPhone(), order.getCustomersId().getIsActive(), order.getCustomersId().getLastOrder(), order.getCustomersId().getPic()))
                .item(new ItemResponseDTO(order.getItemsId().getId(), order.getItemsId().getItemsName(), order.getItemsId().getItemsCode(), order.getItemsId().getStock(), order.getItemsId().getPrice(), order.getItemsId().getIsAvailable(), order.getItemsId().getLastReStock()))
                .quantity(order.getQuantity())
                .build()).collect(Collectors.toList());

        InputStream reportStream = getClass().getResourceAsStream("/templates/order_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orderResponseDTOS);
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
