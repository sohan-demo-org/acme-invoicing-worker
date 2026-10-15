package com.acme.invoicing;

import java.math.BigDecimal;
import java.util.List;

/** Entry point: invoices a batch of sample orders and prints them. */
public final class InvoicingWorker {
    private InvoicingWorker() {
    }

    public static List<Order> sampleBatch() {
        return List.of(
                new Order("1001", "ada@example.com", "US-CA", "WELCOME10", List.of(
                        new Order.Line("SKU-1", "Widget", new BigDecimal("19.99"), 2),
                        new Order.Line("SKU-7", "Gadget", new BigDecimal("49.50"), 1))),
                new Order("1002", "grace@example.com", "GB", null, List.of(
                        new Order.Line("SKU-3", "Sprocket", new BigDecimal("4.25"), 12))));
    }

    public static void main(String[] args) {
        InvoiceGenerator generator = new InvoiceGenerator();
        for (Order order : sampleBatch()) {
            System.out.println(InvoiceRenderer.render(generator.generate(order)));
        }
    }
}
