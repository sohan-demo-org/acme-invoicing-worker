package com.acme.invoicing;

import java.math.BigDecimal;
import java.util.List;

/** The invoice document produced for an order. */
public record Invoice(
        String invoiceNumber,
        String orderId,
        String customerEmail,
        List<InvoiceLine> lines,
        BigDecimal subtotal,
        BigDecimal amountDue) {

    public record InvoiceLine(String sku, String description, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }
}
