package org.payments.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    String username;

    @Column(name = "order_status")
    String orderStatus;

    @Column(name = "paypal_order_id", unique = true)
    String paypalOrderId;

    @Column(name = "date_created")
    LocalDateTime dateCreated;

    @Column(name = "date_modified")
    LocalDateTime dateModified;

}
