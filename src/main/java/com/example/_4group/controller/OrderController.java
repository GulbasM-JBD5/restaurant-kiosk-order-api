package com.example._4group.controller;

import com.example._4group.dto.OrderRequest;
import com.example._4group.dto.OrderResponse;
import com.example._4group.enums.OrderStatus;
import com.example._4group.projection.ProductSalesProjection;
import com.example._4group.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {

        orderService.cancelOrder(id);

        return ResponseEntity.ok("Sifariş uğurla ləğv edildi!");
    }
    @GetMapping("/restaurants/{restaurantId}/orders")
    public ResponseEntity<Page<OrderResponse>> getRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                orderService.getRestaurantOrders(restaurantId, status, page, size)
        );
    }

    @GetMapping("/restaurants/{restaurantId}/top-selling-products")
    public ResponseEntity<List<ProductSalesProjection>> getTopSellingProducts(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                orderService.getTopSellingProducts(restaurantId)
        );
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<String> changeOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        orderService.changeOrderStatus(id, status);

        return ResponseEntity.ok("Sifarişin statusu uğurla dəyişdirildi!");
    }
}