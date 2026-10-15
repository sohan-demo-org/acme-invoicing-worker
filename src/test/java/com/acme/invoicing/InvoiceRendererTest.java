package com.acme.invoicing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class InvoiceRendererTest {
    @Test
    void rendersHeaderLinesAndTotals() {
        Invoice invoice = new Invoice("INV-1", "1", "ada@example.com",
                List.of(new Invoice.InvoiceLine("SKU-1", "Widget", 2, new BigDecimal("19.99"), new BigDecimal("39.98"))),
                new BigDecimal("39.98"), new BigDecimal("42.88"));

        String text = InvoiceRenderer.render(invoice);

        assertTrue(text.startsWith("INVOICE INV-1"));
        assertTrue(text.contains("SKU-1"));
        assertTrue(text.contains("AMOUNT DUE"));
        assertTrue(text.contains("42.88"));
    }

    @Test
    void sampleBatchRendersWithoutError() {
        InvoiceGenerator generator = new InvoiceGenerator();
        for (Order order : InvoicingWorker.sampleBatch()) {
            assertTrue(InvoiceRenderer.render(generator.generate(order)).contains("AMOUNT DUE"));
        }
    }
}
