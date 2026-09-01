package com.thz.house_paint.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "original_quantity", precision = 10, scale = 2)
    private BigDecimal originalQuantity;

    @Column(name = "new_price", precision = 12, scale = 2)
    private BigDecimal newPrice;

    @Column(name = "new_quantity", precision = 10, scale = 2)
    private BigDecimal newQuantity;

    @Column(name = "action_type", length = 50)
    private String actionType;

    @Column(name = "log_date", updatable = false)
    private LocalDateTime logDate;

    @PrePersist
    protected void onCreate() {
        this.logDate = LocalDateTime.now();
    }
}
