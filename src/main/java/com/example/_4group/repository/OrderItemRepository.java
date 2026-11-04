package com.example._4group.repository;

import com.example._4group.entity.OrderItem;
import com.example._4group.enums.OrderStatus;
import com.example._4group.projection.ProductSalesProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
    @Query("""
        SELECT
            p.id AS productId,
            p.name AS productName,
            SUM(oi.quantity) AS totalSold
        FROM OrderItem oi
        JOIN oi.order o
        JOIN oi.product p
        WHERE o.restoran.id = :restaurantId
        AND o.status <> :cancelledStatus
        GROUP BY p.id, p.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<ProductSalesProjection> findTopSellingProducts(
            @Param("restaurantId") Long restaurantId,
            @Param("cancelledStatus") OrderStatus cancelledStatus,
            Pageable pageable
    );

}
