package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.thz.house_paint.model.entity.Product;
import com.thz.house_paint.model.entity.Sale;

public record SaleItemForm(
Sale sale,
Product product,
int quantity,
BigDecimal unitCostPrice,
BigDecimal unitSalesPrice,
BigDecimal discountPrice,
LocalDate saleDate) {

}
