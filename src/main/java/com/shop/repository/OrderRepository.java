package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);

    @Query("select distinct o from Order o " +
            "join fetch o.orderItems oi " +
            "join fetch oi.item i " +
            "order by o.orderDate desc")
    List<Order> findRecentOrders(Pageable pageable);

    // ADMIN용 전체 주문 조회
    @Query("select o from Order o " +
            "order by o.orderDate desc"
    )
    List<Order> findAllOrders(Pageable pageable);

    @Query("select count(o) from Order o")
    Long countAllOrders();
}
