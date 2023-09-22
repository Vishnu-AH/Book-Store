package com.pace.bookstore.repository;

import com.pace.bookstore.entity.Orders;
import com.pace.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Orders, UUID> {

    @Query("SELECT o FROM Orders o WHERE DATE(o.createdAt) >= :startDate AND DATE(o.createdAt) <= :endDate")
    List<Orders> getOrdersUsingRangeOfDate(Date startDate, Date endDate);

    @Query("select o.user from Orders o where o.books.id=:bookId")
    List<User> findUsersByBookId(UUID bookId);
}
