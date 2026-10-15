package com.acme.invoicing;

import com.acme.pricing.DiscountCode;
import com.acme.pricing.LineItem;
import com.acme.pricing.PricingEngine;
import com.acme.pricing.TaxRules;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** Builds an {@link Invoice} from an {@link Order} using the shared pricing engine. */
public class InvoiceGenerator {
    private static final Map<String, DiscountCode> PROMOS = Map.of(
            "WELCOME10", DiscountCode.percent("WELCOME10", BigDecimal.TEN),
            "SAVE5", DiscountCode.fixed("SAVE5", new BigDecimal("5.00")));

    private final PricingEngine engine = new PricingEngine();
    private final Function<String, String> numbering;

    public InvoiceGenerator(Function<String, String> numbering) {
        this.numbering = numbering;
    }

    public InvoiceGenerator() {
        this(orderId -> "INV-" + orderId);
    }

    public Invoice generate(Order order) {
        List<LineItem> items = order.lines().stream()
                .map(l -> new LineItem(l.sku(), l.unitPrice(), l.quantity()))
                .toList();

        List<Invoice.InvoiceLine> lines = order.lines().stream()
                .map(l -> new Invoice.InvoiceLine(l.sku(), l.description(), l.quantity(), l.unitPrice(),
                        l.unitPrice().multiply(BigDecimal.valueOf(l.quantity())).setScale(2, RoundingMode.HALF_UP)))
                .toList();

        DiscountCode promo = order.promoCode() == null ? null : PROMOS.get(order.promoCode());
        BigDecimal subtotal = engine.subtotal(items);
        BigDecimal amountDue = engine.calculateTotal(items, TaxRules.rateFor(order.region()), promo);

        return new Invoice(numbering.apply(order.orderId()), order.orderId(), order.customerEmail(),
                lines, subtotal, amountDue);
    }
}
