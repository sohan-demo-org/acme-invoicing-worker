package com.acme.invoicing;

import java.math.BigDecimal;
import java.util.List;

/** A fulfilled order waiting to be invoiced. */
public record Order(String orderId, String customerEmail, String region, String promoCode, List<Line> lines) {
    public record Line(String sku, String description, BigDecimal unitPrice, int quantity) {
    }
}
