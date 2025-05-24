package com.payments.common.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transaction_user_id", columnList = "user_id"),
        @Index(name = "idx_order_id", columnList = "order_id")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "order_status")
    String orderStatus;

    @Column(name = "order_id", unique = true)
    String orderId;

    @Column(name = "date_created")
    LocalDateTime dateCreated;

    @Column(name = "date_modified")
    LocalDateTime dateModified;

}
