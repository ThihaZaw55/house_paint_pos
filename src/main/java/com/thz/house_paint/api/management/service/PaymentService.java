package com.thz.house_paint.api.management.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.thz.house_paint.api.management.input.SaleForm;
import com.thz.house_paint.api.management.input.SaleItemForm;
import com.thz.house_paint.model.entity.Payment;
import com.thz.house_paint.model.entity.Product;
import com.thz.house_paint.model.entity.Sale;
import com.thz.house_paint.model.entity.SaleItem;
import com.thz.house_paint.model.repository.PaymentRepo;
import com.thz.house_paint.model.repository.ProductRepo;
//import com.thz.house_paint.model.repository.SaleRepo; // Sale Repository လိုအပ်ပါက ထည့်ပါ

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final ProductRepo productRepository;
    // private final SaleRepo saleRepository; // Sale ကို သီးသန့်သိမ်းရန် Repository လိုအပ်ပါက ထည့်ပါ

    // 1. CREATE / SAVE PAYMENT (Order Complete & Stock Deduct)
    @Transactional
    public Payment savePayment(SaleForm request) {
        // 1. Sale Header တည်ဆောက်ခြင်း
        Sale sale = new Sale();
        sale.setInvoice(request.invoice());
        sale.setCustomerName(request.customerName());
        sale.setTotalAmount(request.totalAmount());
        sale.setSaleDate(request.saleDate());
        
        // 2. Sale Items များကို ဖြည့်စွက်ပြီး Product Stock များကို နှုတ်မည်
        for (SaleItemForm itemDto : request.items()) {
            Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            
            // Stock လုံလောက်မှုရှိမရှိ စစ်ဆေးခြင်း
            if (product.getStockQuantity() < itemDto.quantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getItem());
            }
            
            // Product Stock ကို နှုတ်ခြင်း (Deduct Stock)
            product.setStockQuantity(product.getStockQuantity() - itemDto.quantity());
            productRepository.save(product);

            SaleItem saleItem = new SaleItem();
            saleItem.setSale(sale);
            saleItem.setProduct(product);
            saleItem.setQuantity(BigDecimal.valueOf(itemDto.quantity()));
            saleItem.setUnitCostPrice(product.getCostPrice());
            saleItem.setUnitSalesPrice(product.getSalePrice());
            saleItem.setDiscountPrice(BigDecimal.ZERO);
            saleItem.setSaleDate(request.saleDate());
            
            sale.getItems().add(saleItem);
        }
        
        // 3. Payment တွင် Sale ကို ချိတ်ဆက်၍ သိမ်းဆည်းခြင်း
        Payment payment = new Payment();
        payment.setSale(sale);
        payment.setAmount(request.totalAmount());
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(request.saleDate());


        return paymentRepo.save(payment); // PaymentRepo ဖြင့် သိမ်းမည်
    }

    // 2. READ (GET ALL PAYMENTS)
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    // 3. READ (GET PAYMENT BY ID)
    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    // 4. DELETE PAYMENT
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        paymentRepo.delete(payment);
    }
}