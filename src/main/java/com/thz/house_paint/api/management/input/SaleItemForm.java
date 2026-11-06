package com.thz.house_paint.api.management.input;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaleItemForm(
int productId,
int quantity,
BigDecimal unitCostPrice,
BigDecimal unitSalesPrice,
BigDecimal discountPrice,
LocalDate saleDate) {

}
