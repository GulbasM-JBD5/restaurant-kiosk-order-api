
package com.example._4group.service;

import com.example._4group.dto.*;
import com.example._4group.entity.*;
import com.example._4group.enums.OrderStatus;
import com.example._4group.projection.ProductSalesProjection;
import com.example._4group.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RestoranRepository restoranRepository;
    private final OrderItemRepository orderItemRepository;
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        RestoranRepository restoranRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.restoranRepository = restoranRepository;
        this.orderItemRepository = orderItemRepository;
    }
    // CREATE ORDER
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Restoran restoran = restoranRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Restoran tapılmadı!"
                ));

        Order order = new Order();
        order.setRestoran(restoran);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Məhsul tapılmadı!"
                    ));
            if (!product.isAvailable()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Bu məhsul hazırda satışda deyil!"
                );
            }

            if (!product.getRestoran().getId().equals(restoran.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Bu məhsul seçilmiş restorana aid deyil!"
                );
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            BigDecimal subtotal = product.getPrice().multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity())
            );

            totalAmount = totalAmount.add(subtotal);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return convertToResponse(savedOrder);
    }

    // CANCEL ORDER
    public void cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sifariş tapılmadı!"
                ));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Yalnız PENDING statusunda olan sifariş ləğv edilə bilər!"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    // GET RESTAURANT ORDERS WITH FILTER AND PAGINATION
    @Transactional(readOnly = true)
    public Page<OrderResponse> getRestaurantOrders(
            Long restaurantId,
            OrderStatus status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Order> orders;

        if (status == null) {
            orders = orderRepository.findByRestoran_Id(
                    restaurantId, pageable
            );
        } else {
            orders = orderRepository.findByRestoran_IdAndStatus(
                    restaurantId, status, pageable
            );
        }

        return orders.map(this::convertToResponse);
    }

    // CONVERT ORDER TO RESPONSE
    private OrderResponse convertToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setRestaurantId(order.getRestoran().getId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getItems()) {

            OrderItemResponse itemResponse = new OrderItemResponse();

            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setUnitPrice(item.getUnitPrice());

            BigDecimal subtotal = item.getUnitPrice().multiply(
                    BigDecimal.valueOf(item.getQuantity())
            );

            itemResponse.setSubtotal(subtotal);

            itemResponses.add(itemResponse);
        }

        response.setItems(itemResponses);

        return response;
    }
public List<ProductSalesProjection> getTopSellingProducts(Long restaurantId) {

    if (!restoranRepository.existsById(restaurantId)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Restoran tapılmadı!"
        );
    }

    return orderItemRepository.findTopSellingProducts(
            restaurantId,
            OrderStatus.CANCELLED,
            PageRequest.of(0, 5)
    );
}
    // CHANGE ORDER STATUS
    public void changeOrderStatus(Long id, OrderStatus newStatus) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sifariş tapılmadı!"
                ));

        OrderStatus currentStatus = order.getStatus();

        boolean validTransition =
                (currentStatus == OrderStatus.PENDING &&
                        newStatus == OrderStatus.PREPARING)
                        ||
                        (currentStatus == OrderStatus.PREPARING &&
                                newStatus == OrderStatus.READY)
                        ||
                        (currentStatus == OrderStatus.READY &&
                                newStatus == OrderStatus.COMPLETED);

        if (!validTransition) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sifariş statusunu bu mərhələyə dəyişmək mümkün deyil!"
            );
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

}
