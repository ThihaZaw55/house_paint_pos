package com.thz.house_paint.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sale_id")
    private UUID saleId; // Internal Foreign Key / Index များ မြန်ဆန်စေရန်

    @Column(name = "invoice", nullable = false, unique = true, updatable = false, length = 36)
    private String invoice; // External API, QR Code နှင့် Receipt များတွင် သုံးရန်

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "sale_date", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate saleDate;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();

//    @PrePersist
//    protected void onCreate() {
//        this.saleDate = LocalDateTime.now();
//        if (this.invoice == null) {
//            this.invoice = UUID.randomUUID().toString(); // Auto generate UUID
//        }
//    }
}
