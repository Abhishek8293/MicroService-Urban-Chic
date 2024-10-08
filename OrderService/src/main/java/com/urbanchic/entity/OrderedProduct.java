package com.urbanchic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordered_product_table")
public class OrderedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String productId;
    @Column(nullable = false)
    private Integer productQuantity;
    @Column(nullable = false)
    private Double productPrice;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id",referencedColumnName = "orderId")
    private Order order;

}
