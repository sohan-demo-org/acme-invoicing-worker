package com.acme.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceGeneratorTest {
    private final InvoiceGenerator generator = new InvoiceGenerator(id -> "INV-TEST-" + id);

    @Test
    void generatesInvoiceWithTaxAndPromo() {
        Order order = new Order("42", "ada@example.com", "US-CA", "WELCOME10", List.of(
                new Order.Line("SKU-1", "Widget", new BigDecimal("19.99"), 2),
                new Order.Line("SKU-7", "Gadget", new BigDecimal("49.50"), 1)));

        Invoice invoice = generator.generate(order);

        assertEquals("INV-TEST-42", invoice.invoiceNumber());
        assertEquals(2, invoice.lines().size());
        assertEquals(new BigDecimal("39.98"), invoice.lines().get(0).lineTotal());
        assertEquals(new BigDecimal("89.48"), invoice.subtotal());
        // (89.48 - 10%) = 80.532 -> 80.53, * 1.0725 = 86.37
        assertEquals(new BigDecimal("86.37"), invoice.amountDue());
    }

    @Test
    void noPromoNoTaxRegion() {
        Order order = new Order("7", "x@example.com", "US-OR", null, List.of(
                new Order.Line("SKU-3", "Sprocket", new BigDecimal("4.25"), 12)));

        Invoice invoice = generator.generate(order);

        assertEquals(new BigDecimal("51.00"), invoice.subtotal());
        assertEquals(new BigDecimal("51.00"), invoice.amountDue());
    }

    @Test
    void unknownPromoIsIgnored() {
        Order order = new Order("8", "x@example.com", "GB", "BOGUS", List.of(
                new Order.Line("SKU-3", "Sprocket", new BigDecimal("10.00"), 1)));

        assertEquals(new BigDecimal("12.00"), generator.generate(order).amountDue());
    }
}
