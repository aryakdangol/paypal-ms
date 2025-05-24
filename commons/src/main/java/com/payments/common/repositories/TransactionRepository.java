package com.payments.common.repositories;

import com.payments.common.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByOrderIdAndUserId(String orderId, Long userId);

    List<Transaction> findAllByUserId(Long userId);

}
