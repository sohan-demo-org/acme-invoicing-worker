package com.acme.invoicing;

/** Renders an invoice as plain text (what the worker emails / prints). */
public final class InvoiceRenderer {
    private InvoiceRenderer() {
    }

    public static String render(Invoice invoice) {
        StringBuilder sb = new StringBuilder();
        sb.append("INVOICE ").append(invoice.invoiceNumber()).append('\n');
        sb.append("Order:    ").append(invoice.orderId()).append('\n');
        sb.append("Bill to:  ").append(invoice.customerEmail()).append('\n');
        sb.append('\n');
        for (Invoice.InvoiceLine line : invoice.lines()) {
            sb.append(String.format("%-10s %-24s %3d x %8s = %9s%n",
                    line.sku(), line.description(), line.quantity(), line.unitPrice(), line.lineTotal()));
        }
        sb.append('\n');
        sb.append(String.format("%-44s %9s%n", "Subtotal", invoice.subtotal()));
        sb.append(String.format("%-44s %9s%n", "AMOUNT DUE", invoice.amountDue()));
        return sb.toString();
    }
}
