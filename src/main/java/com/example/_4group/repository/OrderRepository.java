package com.example._4group.repository;

import com.example._4group.entity.Order;
import com.example._4group.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Restoranın bütün sifarişləri
    Page<Order> findByRestoran_Id(Long restaurantId, Pageable pageable);

    // Restoranın müəyyən statuslu sifarişləri
    Page<Order> findByRestoran_IdAndStatus(
            Long restaurantId,
            OrderStatus status,
            Pageable pageable
    );
}
